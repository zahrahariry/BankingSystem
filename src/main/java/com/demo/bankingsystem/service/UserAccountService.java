package com.demo.bankingsystem.service;

import com.demo.bankingsystem.dto.UserAccountDto;
import com.demo.bankingsystem.exception.ResourceNotFoundException;
import com.demo.bankingsystem.log.CustomTransactionEvent;
import com.demo.bankingsystem.mapper.UserAccountMapper;
import com.demo.bankingsystem.repository.UserAccount;
import com.demo.bankingsystem.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAccountService {

	private final ApplicationEventPublisher eventPublisher;

	private final UserAccountRepository userAccountRepository;

	private final UserAccountMapper userAccountMapper;

	private final TransactionStrategyFactory strategyFactory;

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void processTransaction(String accountNumber, TransactionType transactionType, Long amount) {
		UserAccount account = userAccountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Account not found for account number: {}", accountNumber
				));
		synchronized (account) {
			strategyFactory.getStrategy(transactionType).execute(account, amount);
			userAccountRepository.save(account);
		}
		eventPublisher.publishEvent(
				new CustomTransactionEvent(
						this,
						accountNumber,
						account.getOwnerName(),
						transactionType.name(),
						amount
				)
		);
	}

	@Transactional
	public UserAccountDto saveUserAccount (UserAccountDto userAccountRequest) {
		return userAccountMapper.toUserAccountDto(userAccountRepository.save(userAccountMapper.toUserAccount(userAccountRequest)));
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void transfer (String fromAccountNumber, String toAccountNumber, Long amount) {
		processTransaction(fromAccountNumber, TransactionType.WITHDRAW, amount);
		processTransaction(toAccountNumber, TransactionType.DEPOSIT, amount);
	}

	@Transactional(readOnly = true)
	public UserAccountDto getUserAccountInfo (String accountNumber) {
		UserAccount account = userAccountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found for account number : {}", accountNumber));
		return userAccountMapper.toUserAccountDto(account);
	}

	@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
	public Long getBalance (String accountNumber) {
		UserAccount account = userAccountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found for account number : {}", accountNumber));
		return account.getBalance();
	}

}

package com.demo.bankingsystem.service;

import com.demo.bankingsystem.exception.InsufficientBalanceException;
import com.demo.bankingsystem.repository.UserAccount;

import org.springframework.stereotype.Component;

@Component
public class WithdrawStrategy implements TransactionStrategy{

	@Override
	public void execute(UserAccount account, Long amount) {
		validateWithdrawal(account, amount);
		account.setBalance(account.getBalance() - amount);
	}

	private void validateWithdrawal(UserAccount account, Long amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Withdrawal amount must be positive");
		}
		if (account.getBalance() < amount) {
			throw new InsufficientBalanceException(
					"Insufficient balance for account: " + account.getAccountNumber()
			);
		}
	}

//	@Override
//	public void execute(UserAccount account, Long amount) {
//		if (account.getBalance() < amount) {
//			throw new InsufficientBalanceException(
//					"Insufficient balance for account number: {}",
//					account.getAccountNumber()
//			);
//		}
//		account.setBalance(account.getBalance() - amount);
//	}
}

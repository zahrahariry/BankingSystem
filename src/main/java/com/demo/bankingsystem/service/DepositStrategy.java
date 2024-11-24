package com.demo.bankingsystem.service;

import com.demo.bankingsystem.repository.UserAccount;

import org.springframework.stereotype.Component;

@Component
public class DepositStrategy implements TransactionStrategy {

	@Override
	public void execute(UserAccount account, Long amount) {
		validatePositiveAmount(amount);
		account.setBalance(account.getBalance() + amount);
	}

	private void validatePositiveAmount(Long amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Deposit amount must be positive");
		}
	}
}

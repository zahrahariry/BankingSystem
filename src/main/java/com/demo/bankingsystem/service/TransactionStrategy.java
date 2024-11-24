package com.demo.bankingsystem.service;

import com.demo.bankingsystem.repository.UserAccount;

public interface TransactionStrategy {
	void execute(UserAccount account, Long amount);
}

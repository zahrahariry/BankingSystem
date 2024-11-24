package com.demo.bankingsystem.exception;

public class InsufficientBalanceException extends RuntimeException{

	private String accountNumber;
	public InsufficientBalanceException(String message) {
		super(message);
	}

	public InsufficientBalanceException(String message, String accountNumber) {
		super(message);
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
}

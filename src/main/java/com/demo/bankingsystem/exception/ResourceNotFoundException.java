package com.demo.bankingsystem.exception;

public class ResourceNotFoundException extends RuntimeException{
	private String accountNumber;

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String message, String accountNumber) {
		super(message);
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
}

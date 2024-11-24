package com.demo.bankingsystem.service;

public enum TransactionType {

	DEPOSIT,
	WITHDRAW;
	public static TransactionType fromString(String type) {
		for (TransactionType transactionType : values()) {
			if (transactionType.name().equalsIgnoreCase(type)) {
				return transactionType;
			}
		}
		throw new IllegalArgumentException("Unsupported transaction type: " + type);
	}
}

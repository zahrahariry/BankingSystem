package com.demo.bankingsystem.service;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TransactionStrategyFactory {

	private final Map<TransactionType, TransactionStrategy> strategies;


	public TransactionStrategyFactory(DepositStrategy depositStrategy, WithdrawStrategy withdrawStrategy) {
		strategies = new EnumMap<>(TransactionType.class);
		strategies.put(TransactionType.DEPOSIT, depositStrategy);
		strategies.put(TransactionType.WITHDRAW, withdrawStrategy);
	}

	public TransactionStrategy getStrategy(TransactionType type) {
		TransactionStrategy strategy = strategies.get(type);
		if (strategy == null) {
			throw new IllegalArgumentException("Unsupported transaction type: " + type);
		}
		return strategy;
	}
}

package com.demo.bankingsystem.log;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CustomTransactionEvent extends ApplicationEvent {

	private final String accountNumber;
	private final String transactionType;
	private final Long amount;
	private final String accountOwner;
	private final LocalDateTime customTimestamp;

	public CustomTransactionEvent(Object source, String accountNumber, String accountOwner, String transactionType, Long amount) {
		super(source);
		this.accountNumber = accountNumber;
		this.transactionType = transactionType;
		this.amount = amount;
		this.accountOwner = accountOwner;
		this.customTimestamp = LocalDateTime.now();
	}

}

package com.demo.bankingsystem.log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CustomTransactionLoggerListener {

	private static final Logger logger = LoggerFactory.getLogger(CustomTransactionLoggerListener.class);

	@Async
	@EventListener
	public void handleTransactionEvent(CustomTransactionEvent event) {
		String logMessage = String.format(
				"Transaction: Type=%s, Account=%s, Amount=%d, owner=%s, Timestamp=%s",
				event.getTransactionType(),
				event.getAccountNumber(),
				event.getAmount(),
				event.getAccountOwner(),
				event.getTimestamp()
		);

		try {
			Path logFile = Paths.get("transactions.log");
			Files.writeString(logFile, logMessage + System.lineSeparator(),
					StandardOpenOption.CREATE,
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			logger.error("Failed to write transaction log", e);
		}
	}
}

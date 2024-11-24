package com.demo.bankingsystem.controller;

import com.demo.bankingsystem.dto.BalanceResponse;
import com.demo.bankingsystem.dto.UserAccountDto;
import com.demo.bankingsystem.service.TransactionType;
import com.demo.bankingsystem.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user-account")
@RequiredArgsConstructor
@Tag(name = "User Account Controller", description = "User account management APIs")
public class UserAccountController {

	private final UserAccountService userAccountService;

	@PostMapping
	@Operation(summary = "save user account")
	public ResponseEntity<UserAccountDto> saveUserAccount (@RequestBody @Valid UserAccountDto userAccountDto) {
		log.info("going to save user account by request : {}", userAccountDto);
		UserAccountDto createdAccount = userAccountService.saveUserAccount(userAccountDto);
		return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
	}

	@PostMapping("/{accountNumber}/transaction")
	@Operation(summary = "do transaction")
	public ResponseEntity<Void> doTransaction (@PathVariable(name = "accountNumber") String accountNumber,
			@RequestParam Long amount, @RequestParam TransactionType transactionType) {
		log.info("going to do transaction type with account number : {} by amount : {} and type : {}", accountNumber, amount, transactionType);
		userAccountService.processTransaction(accountNumber, transactionType, amount);
		return ResponseEntity.ok().build();
	}

	@PostMapping("{fromAccountNumber}/{toAccountNumber}/transfer")
	@Operation(summary = "transfer between accounts")
	public ResponseEntity<Void> transferFunds(@PathVariable(name = "fromAccountNumber") String fromAccountNumber, @PathVariable(name = "toAccountNumber") String toAccountNumber,
			@RequestParam Long amount) {
		log.info("going to transfer from account number : {} to account number : {} by amount : {}", fromAccountNumber, toAccountNumber, amount);
		userAccountService.transfer(fromAccountNumber, toAccountNumber, amount);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{accountNumber}")
	@Operation(summary = "get account info")
	public ResponseEntity<UserAccountDto> getAccountInfo(@PathVariable(name = "accountNumber") String accountNumber) {
		log.info("going to get account info for account number : {}", accountNumber);
		UserAccountDto accountInfo = userAccountService.getUserAccountInfo(accountNumber);
		return ResponseEntity.ok(accountInfo);
	}

	@GetMapping("/{accountNumber}/balance")
	@Operation(summary = "get account balance")
	public ResponseEntity<BalanceResponse> getAccountBalance(@PathVariable(name = "accountNumber") String accountNumber) {
		log.info("going to get balance from account number : {}", accountNumber);
		Long balance = userAccountService.getBalance(accountNumber);
		return ResponseEntity.ok(new BalanceResponse(balance));
	}
}

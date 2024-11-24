package com.demo.bankingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class UserAccountDto {

	@NotBlank
	private String accountNumber;

	private String ownerName;

	@NotNull
	private Long balance;

}

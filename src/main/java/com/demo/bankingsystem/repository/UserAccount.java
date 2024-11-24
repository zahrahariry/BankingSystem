package com.demo.bankingsystem.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@ToString
public class UserAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "account_number", unique = true)
	private String accountNumber;

	@Column(name = "owner_name")
	private String ownerName;

	@Column(name = "balance", nullable = false)
	private Long balance;

	@Version
	private Long version;
}

package com.demo.bankingsystem;

import java.util.Optional;

import com.demo.bankingsystem.dto.BalanceResponse;
import com.demo.bankingsystem.dto.UserAccountDto;
import com.demo.bankingsystem.repository.UserAccount;
import com.demo.bankingsystem.repository.UserAccountRepository;
import com.demo.bankingsystem.service.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAccountControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@BeforeEach
	void setUp() {
		userAccountRepository.deleteAll();
	}

	@Test
	public void testCreateUserAccount_Success() {
		UserAccountDto userAccountDto = createUserAccountDto("testAccountNumber", "testAccountOwner", 1000L);

		ResponseEntity<UserAccountDto> userAccountResponse = restTemplate.postForEntity(
				createURLWithPort("/user-account"),userAccountDto, UserAccountDto.class
		);

		assertThat(userAccountResponse).isNotNull();
		assertThat(userAccountResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		UserAccountDto responseDto = userAccountResponse.getBody();
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getAccountNumber()).isEqualTo("testAccountNumber");
		assertThat(responseDto.getBalance()).isEqualTo(1000L);
		assertThat(responseDto.getOwnerName()).isEqualTo("testAccountOwner");
	}

	@Test
	public void testCreateUserAccount_balanceIsInvalid_Fail() {
		UserAccountDto userAccountDto = createUserAccountDto("testAccountNumber", "testAccountOwner", null);

		ResponseEntity<UserAccountDto> userAccountResponse = restTemplate.postForEntity(
				createURLWithPort("/user-account"),userAccountDto, UserAccountDto.class
		);

		assertThat(userAccountResponse).isNotNull();
		assertThat(userAccountResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		UserAccountDto responseDto = userAccountResponse.getBody();
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.getAccountNumber()).isNull();
		assertThat(responseDto.getBalance()).isNull();
		assertThat(responseDto.getOwnerName()).isNull();
	}

	@Test
	public void testDoTransaction_deposit_success () {

		UserAccount userAccount = createUserAccount("testUserAccountNumber", "testAccountOwner", 6666L);
		userAccountRepository.save(userAccount);
		String url = createURLWithPort("/user-account") + "/{accountNumber}/transaction?amount={amount}&transactionType={transactionType}";

		ResponseEntity<Void> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				null,
				Void.class,
				"testUserAccountNumber",
				1000L,
				TransactionType.DEPOSIT
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Optional<UserAccount> updatedUserAccount = userAccountRepository.findByAccountNumber("testUserAccountNumber");
		assertThat(updatedUserAccount).isNotNull();
		assertThat(updatedUserAccount).isPresent();
		assertThat(updatedUserAccount.get().getBalance()).isNotNull();
		assertThat(updatedUserAccount.get().getBalance()).isEqualTo(7666L);

	}

	@Test
	public void testDoTransaction_deposit_fail () {

		UserAccount userAccount = createUserAccount("testUserAccountNumber1", "testAccountOwner", 6666L);
		userAccountRepository.save(userAccount);
		String url = createURLWithPort("/user-account") + "/{accountNumber}/transaction?amount={amount}&transactionType={transactionType}";

		ResponseEntity<Void> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				null,
				Void.class,
				"testUserAccountNumber",
				1000L,
				TransactionType.DEPOSIT
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void testDoTransaction_withdraw_success () {

		UserAccount userAccount = createUserAccount("testUserAccountNumber", "testAccountOwner", 6666L);
		userAccountRepository.save(userAccount);
		String url = createURLWithPort("/user-account") + "/{accountNumber}/transaction?amount={amount}&transactionType={transactionType}";

		ResponseEntity<Void> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				null,
				Void.class,
				"testUserAccountNumber",
				1000L,
				TransactionType.WITHDRAW
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Optional<UserAccount> updatedUserAccount = userAccountRepository.findByAccountNumber("testUserAccountNumber");
		assertThat(updatedUserAccount).isNotNull();
		assertThat(updatedUserAccount).isPresent();
		assertThat(updatedUserAccount.get().getBalance()).isNotNull();
		assertThat(updatedUserAccount.get().getBalance()).isEqualTo(5666L);
	}

	@Test
	public void testDoTransaction_withdraw_fail () {

		UserAccount userAccount = createUserAccount("testUserAccountNumber1", "testAccountOwner", 6666L);
		userAccountRepository.save(userAccount);
		String url = createURLWithPort("/user-account") + "/{accountNumber}/transaction?amount={amount}&transactionType={transactionType}";

		ResponseEntity<Void> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				null,
				Void.class,
				"testUserAccountNumber",
				1000L,
				TransactionType.WITHDRAW
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void testDoTransaction_transfer_success () {

		UserAccount fromUserAccount = createUserAccount("fromTestUserAccountNumber", "fromTestAccountOwner", 10000L);
		userAccountRepository.save(fromUserAccount);
		UserAccount toUserAccount = createUserAccount("toTestUserAccountNumber", "toTestAccountOwner", 20000L);
		userAccountRepository.save(toUserAccount);
		userAccountRepository.flush();

		String url = createURLWithPort("/user-account") + "/{fromAccountNumber}/{toAccountNumber}/transfer?amount={amount}";

		ResponseEntity<Void> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				null,
				Void.class,
				"fromTestUserAccountNumber","toTestUserAccountNumber",
				1000L
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Optional<UserAccount> updatedFromUserAccount = userAccountRepository.findByAccountNumber("fromTestUserAccountNumber");
		assertThat(updatedFromUserAccount).isNotNull();
		assertThat(updatedFromUserAccount).isPresent();
		assertThat(updatedFromUserAccount.get().getBalance()).isNotNull();
		assertThat(updatedFromUserAccount.get().getBalance()).isEqualTo(9000L);

		Optional<UserAccount> updatedToUserAccount = userAccountRepository.findByAccountNumber("toTestUserAccountNumber");
		assertThat(updatedToUserAccount).isNotNull();
		assertThat(updatedToUserAccount).isPresent();
		assertThat(updatedToUserAccount.get().getBalance()).isNotNull();
		assertThat(updatedToUserAccount.get().getBalance()).isEqualTo(21000L);
	}

	@Test
	public void testDoTransaction_transfer_fromUserAccountNotExists_fail () {

		UserAccount fromUserAccount = createUserAccount("fromTestUserAccountNumber", "fromTestAccountOwner", 10000L);
		userAccountRepository.save(fromUserAccount);
		UserAccount toUserAccount = createUserAccount("toTestUserAccountNumber", "toTestAccountOwner", 20000L);
		userAccountRepository.save(toUserAccount);
		userAccountRepository.flush();

		String url = createURLWithPort("/user-account") + "/{fromAccountNumber}/{toAccountNumber}/transfer?amount={amount}";

		ResponseEntity<Void> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				null,
				Void.class,
				"fromTestUserAccountNumber1","toTestUserAccountNumber",
				1000L
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void testDoTransaction_transfer_toUserAccountNotExists_fail () {

		UserAccount fromUserAccount = createUserAccount("fromTestUserAccountNumber", "fromTestAccountOwner", 10000L);
		userAccountRepository.save(fromUserAccount);
		UserAccount toUserAccount = createUserAccount("toTestUserAccountNumber", "toTestAccountOwner", 20000L);
		userAccountRepository.save(toUserAccount);
		userAccountRepository.flush();

		String url = createURLWithPort("/user-account") + "/{fromAccountNumber}/{toAccountNumber}/transfer?amount={amount}";

		ResponseEntity<Void> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				null,
				Void.class,
				"fromTestUserAccountNumber","toTestUserAccountNumber1",
				1000L
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void testGetAccountInfo_success () {

		UserAccount userAccount = createUserAccount("testUserAccountNumber", "testAccountOwner", 10000L);
		userAccountRepository.save(userAccount);
		userAccountRepository.flush();

		String url = createURLWithPort("/user-account") + "/{accountNumber}";

		ResponseEntity<UserAccountDto> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				null,
				UserAccountDto.class,
				"testUserAccountNumber"
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		UserAccountDto userAccountDto = response.getBody();
		assertThat(userAccountDto).isNotNull();
		assertThat(userAccountDto.getAccountNumber()).isEqualTo("testUserAccountNumber");
		assertThat(userAccountDto.getBalance()).isEqualTo(10000L);
		assertThat(userAccountDto.getOwnerName()).isEqualTo("testAccountOwner");
	}

	@Test
	public void testGetAccountInfo_fail () {

		UserAccount userAccount = createUserAccount("testUserAccountNumber", "testAccountOwner", 10000L);
		userAccountRepository.save(userAccount);
		userAccountRepository.flush();

		String url = createURLWithPort("/user-account") + "/{accountNumber}";

		ResponseEntity<UserAccountDto> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				null,
				UserAccountDto.class,
				"testUserAccountNumber1"
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void testGetAccountBalance_success () {

		UserAccount userAccount = createUserAccount("testUserAccountNumber", "testAccountOwner", 50000L);
		userAccountRepository.save(userAccount);
		userAccountRepository.flush();

		String url = createURLWithPort("/user-account") + "/{accountNumber}/balance";

		ResponseEntity<BalanceResponse> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				null,
				BalanceResponse.class,
				"testUserAccountNumber"
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		BalanceResponse balanceResponse = response.getBody();
		assertThat(balanceResponse).isNotNull();
		assertThat(balanceResponse.getBalance()).isNotNull();
		assertThat(balanceResponse.getBalance()).isEqualTo(50000L);
	}

	@Test
	public void testGetAccountBalance_fail () {

		UserAccount userAccount = createUserAccount("testUserAccountNumber", "testAccountOwner", 50000L);
		userAccountRepository.save(userAccount);
		userAccountRepository.flush();

		String url = createURLWithPort("/user-account") + "/{accountNumber}/balance";

		ResponseEntity<BalanceResponse> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				null,
				BalanceResponse.class,
				"testUserAccountNumber1"
		);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	private UserAccountDto createUserAccountDto(String accountNumber, String accountOwner, Long balance) {
		UserAccountDto userAccountDto = new UserAccountDto();
		userAccountDto.setAccountNumber(accountNumber);
		userAccountDto.setBalance(balance);
		userAccountDto.setOwnerName(accountOwner);
		return userAccountDto;
	}

	private UserAccount createUserAccount (String accountNumber, String accountOwner, Long balance){
		UserAccount userAccount = new UserAccount();
		userAccount.setAccountNumber(accountNumber);
		userAccount.setBalance(balance);
		userAccount.setOwnerName(accountOwner);
		return userAccount;
	}


}

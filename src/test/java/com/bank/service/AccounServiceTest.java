package com.bank.service;

import com.bank.domain.Account;
import com.bank.domain.Transfer;
import com.bank.domain.TransferType;
import com.bank.dto.TransferDTO;
import com.bank.exception.FunctionalException;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransferRepository;
import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AccounServiceTest {

	@Mock
	AccountRepository accountRepositoryMock;

	@Mock
	TransferRepository transferRepositoryMock;

	@InjectMocks
	AccountService accountServiceImpl;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void testDeposit_ok() throws FunctionalException {

		// PREPARE
		Account account = new Account.Builder().id(1L).balance(BigDecimal.ZERO).build();

		Transfer transfer = new Transfer.Builder()
				.id(1L).type(TransferType.DEPOSIT)
				.transfertDate(LocalDateTime.now())
				.transfertAmount(BigDecimal.TEN)
				.account(account).build();

		TransferDTO dto = new TransferDTO();
		dto.setAccountId(1L);
		dto.setTransferAmount(BigDecimal.TEN);

		when(accountRepositoryMock.findOne(1L)).thenReturn(account);
		when(transferRepositoryMock.save(any(Transfer.class))).thenReturn(transfer);


		// ACTION
		Account accountAfterDepot = accountServiceImpl.deposit(dto);

		// CHECK
		Assertions.assertThat(accountAfterDepot.getId()).isEqualTo(1L);
		Assertions.assertThat(accountAfterDepot.getBalance()).isEqualTo(BigDecimal.TEN);
		Assertions.assertThat(accountAfterDepot.getTransfers().size()).isEqualTo(1);

		Transfer transfer1 = accountAfterDepot.getTransfers().stream().findFirst().get();
		Assertions.assertThat(transfer1.getTransferAmount()).isEqualTo(BigDecimal.TEN);
		Assertions.assertThat(transfer1.getType()).isEqualTo(TransferType.DEPOSIT);
		Assertions.assertThat(transfer1.getTransferDate().toLocalDate()).isEqualTo(LocalDate.now());

	}

	@Test(expected = FunctionalException.class)
	public void testDeposit_ko_montantNegatif() throws FunctionalException {

		// PREPARE
		Account account = new Account.Builder().id(1L).balance(BigDecimal.ZERO).build();

		TransferDTO dto = new TransferDTO();
		dto.setAccountId(1L);
		dto.setTransferAmount(BigDecimal.TEN.negate());

		when(accountRepositoryMock.findOne(1L)).thenReturn(account);

		// ACTION
		accountServiceImpl.deposit(dto);

	}

	@Test
	public void testRetrieve_ok() throws FunctionalException {

		// PREPARE
		Account account = new Account.Builder().id(1L).balance(BigDecimal.TEN).build();

		Transfer transfer = new Transfer.Builder()
				.id(1L).type(TransferType.DEPOSIT)
				.transfertDate(LocalDateTime.now())
				.transfertAmount(BigDecimal.TEN)
				.account(account).build();
		account.addTransfer(transfer);

		Transfer transfer2 = new Transfer.Builder()
				.id(2L).type(TransferType.WITHDRAWAL)
				.transfertDate(LocalDateTime.now())
				.transfertAmount(BigDecimal.TEN.negate())
				.account(account).build();

		TransferDTO dto = new TransferDTO();
		dto.setAccountId(1L);
		dto.setTransferAmount(BigDecimal.TEN.negate());

		when(accountRepositoryMock.findOne(1L)).thenReturn(account);
		when(transferRepositoryMock.save(any(Transfer.class))).thenReturn(transfer2);

		// ACTION
		Account accountAfterDepot = accountServiceImpl.retrieve(dto);

		// CHECK
		Assertions.assertThat(accountAfterDepot.getId()).isEqualTo(1L);
		Assertions.assertThat(accountAfterDepot.getBalance()).isEqualTo(BigDecimal.ZERO);
		Assertions.assertThat(accountAfterDepot.getTransfers().size()).isEqualTo(2);

		Transfer transferTwoResult = accountAfterDepot.getTransfers().stream().filter(t -> t.getType() == TransferType.WITHDRAWAL).findFirst().get();
		Assertions.assertThat(transferTwoResult.getTransferAmount()).isEqualTo(BigDecimal.TEN.negate());
		Assertions.assertThat(transferTwoResult.getType()).isEqualTo(TransferType.WITHDRAWAL);
		Assertions.assertThat(transferTwoResult.getTransferDate().toLocalDate()).isEqualTo(LocalDate.now());

	}

	@Test(expected = FunctionalException.class)
	public void testRetrieve_ko_montantPositif() throws FunctionalException {

		// PREPARE
		Account account = new Account.Builder().id(1L).balance(BigDecimal.ZERO).build();

		TransferDTO dto = new TransferDTO();
		dto.setAccountId(1L);
		dto.setTransferAmount(BigDecimal.TEN);

		when(accountRepositoryMock.findOne(1L)).thenReturn(account);

		// ACTION
		accountServiceImpl.retrieve(dto);

	}

	@Test(expected = FunctionalException.class)
	public void testRetrieve_ko_montantInsuffisant() throws FunctionalException {

		// PREPARE
		Account account = new Account.Builder().id(1L).balance(BigDecimal.TEN).build();
		Transfer transfer = new Transfer.Builder()
				.id(1L).type(TransferType.DEPOSIT)
				.transfertDate(LocalDateTime.now())
				.transfertAmount(BigDecimal.TEN)
				.account(account).build();
		account.addTransfer(transfer);

		TransferDTO dto = new TransferDTO();
		dto.setAccountId(1L);
		dto.setTransferAmount(new BigDecimal(-20));

		when(accountRepositoryMock.findOne(1L)).thenReturn(account);

		// ACTION
		accountServiceImpl.retrieve(dto);

	}


	@Test
	public void testConsult_ok()  {

		// PREPARE
		Account account = new Account.Builder().id(1L).balance(new BigDecimal(20)).build();

		LocalDateTime firstJune = LocalDateTime.of(2017, 6, 1, 0, 0);
		LocalDateTime firstAugust = LocalDateTime.of(2017, 8, 1, 0, 0);

		Transfer transfer = new Transfer.Builder()
				.id(1L).type(TransferType.DEPOSIT)
				.transfertDate(firstJune)
				.transfertAmount(BigDecimal.TEN)
				.account(account).build();
		account.addTransfer(transfer);

		Transfer transfer2 = new Transfer.Builder()
				.id(2L).type(TransferType.DEPOSIT)
				.transfertDate(firstAugust)
				.transfertAmount(BigDecimal.TEN)
				.account(account).build();
		account.addTransfer(transfer2);

		when(accountRepositoryMock.findByIdWithTransfer(1L)).thenReturn(account);

		// ACTION
		String s = accountServiceImpl.consult(1L);

		String resultExpected = "Historique : Type / Date / Montant\n" +
				"DEPOSIT 01/06/2017 10 \n" +
				"DEPOSIT 01/08/2017 10 \n" +
				"Balance 20";
		// CHECK
        Assertions.assertThat(s).isEqualTo(resultExpected);

	}

}

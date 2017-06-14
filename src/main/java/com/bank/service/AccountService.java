package com.bank.service;

import com.bank.domain.Account;
import com.bank.domain.Transfer;
import com.bank.domain.TransferType;
import com.bank.dto.TransferDTO;
import com.bank.exception.FunctionalException;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AccountService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TransferRepository transferRepository;


	@Transactional
	public Account deposit(TransferDTO transferDTO) throws FunctionalException {
		if (transferDTO.getTransferAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new FunctionalException("Le montant déposé ne peut pas être négatif.");
		}
		Account account = accountRepository.findOne(transferDTO.getAccountId());
		saveTransferAndUpdateAccountBalance(transferDTO, account, TransferType.DEPOSIT);
		return account;
	}


	@Transactional
	public Account retrieve(TransferDTO transferDTO) throws FunctionalException {
		if (transferDTO.getTransferAmount().compareTo(BigDecimal.ZERO) > 0) {
			throw new FunctionalException("Le montant retiré doit être négatif.");
		}
		Account account = accountRepository.findOne(transferDTO.getAccountId());

		if (account.getBalance().compareTo(transferDTO.getTransferAmount().negate()) < 0) {
			throw new FunctionalException("Le montant retiré dépasse le solde de votre compte.");
		}

		saveTransferAndUpdateAccountBalance(transferDTO, account, TransferType.WITHDRAWAL);
		return account;
	}

	@Transactional
	public String consult(Long accountId)  {
		Account account = accountRepository.findByIdWithTransfer(accountId);
		List<Transfer> transfers = new ArrayList<>(account.getTransfers());
		transfers.sort(Comparator.comparing(Transfer::getTransferDate));
		StringBuilder result = new StringBuilder();
		result.append("Historique : Type / Date / Montant\n");
		for (Transfer transfer : transfers) {
			StringJoiner sj = new StringJoiner(" ");
			sj.add(transfer.getType().name());
			sj.add(transfer.getTransferDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy")));
			sj.add(transfer.getTransferAmount().toString());
			sj.add("\n");
			result.append(sj);
		}
		result.append("Balance " + account.getBalance().toString());
		return result.toString();
	}

	private void saveTransferAndUpdateAccountBalance(TransferDTO transferDTO, Account account, TransferType type) {
		Transfer transfer = new Transfer.Builder()
				.transfertDate(LocalDateTime.now())
				.type(type)
				.transfertAmount(transferDTO.getTransferAmount())
				.account(account)
				.build();
		transfer = transferRepository.save(transfer);

		// optionel si on ne veut pas récupérer tous les transferts du compte
		account.addTransfer(transfer);

		account.updateBalance(transfer);
	}

}

package com.bank.controller;

import com.bank.domain.Account;
import com.bank.dto.TransferDTO;
import com.bank.exception.FunctionalException;
import com.bank.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

	private static final Logger LOGGER = LogManager.getLogger(AccountController.class.getName());

	@Autowired
	AccountService accountService;

	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	public @ResponseBody Account deposit(@RequestBody TransferDTO transferDTO) throws FunctionalException {
		LOGGER.info("Requete de depot {}", transferDTO);
		return accountService.deposit(transferDTO);
	}

	@RequestMapping(value = "/retrieve", method = RequestMethod.POST)
	public @ResponseBody Account withdraw(@RequestBody TransferDTO transferDTO) throws FunctionalException {
		LOGGER.info("Requete de retrait {}", transferDTO);
		return accountService.retrieve(transferDTO);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody String findById(@PathVariable Long id) {
		LOGGER.info("Requete de consultation {}", id);
		return accountService.consult(id);
	}
}

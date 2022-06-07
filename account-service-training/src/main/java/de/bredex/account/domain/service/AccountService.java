package de.bredex.account.domain.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.bredex.account.domain.model.Account;
import de.bredex.account.domain.spi.AccountEntity;
import de.bredex.account.domain.spi.AccountRepository;

@Service
public final class AccountService {

    private final AccountRepository repository;

    public AccountService(final AccountRepository repository) {
	this.repository = repository;
    }

    public final List<Account> getAccounts() {
	final List<Account> accounts = new LinkedList<>();

	repository.findAll().forEach(account -> accounts
		.add(new Account(account.getNumber(), account.getFirstName(), account.getLastName())));

	return Collections.unmodifiableList(accounts);
    }

    public Account getAccount(final String number) {
	final AccountEntity entity = repository.findByNumber(number).orElseThrow(() -> new NoSuchAccountException(number));

	return new Account(entity.getNumber(), entity.getFirstName(), entity.getLastName());
    }

    public final Account createAccount(final Account account) {
	final String accountNumber = nextAccountNumber();
	final AccountEntity savedAccount = repository
		.save(new AccountEntity(accountNumber, account.getFirstName(), account.getLastName()));
	return new Account(savedAccount.getNumber(), savedAccount.getFirstName(), savedAccount.getLastName());
    }

    private final String nextAccountNumber() {
	final Integer nextNumber = getAccounts().stream().mapToInt(account -> Integer.valueOf(account.getNumber()))
		.max().orElse(10000);
	return String.format("%04d", nextNumber + 1);
    }
    
    public class NoSuchAccountException extends RuntimeException {
	
	private static final long serialVersionUID = 8348460552161487667L;

	public NoSuchAccountException(final String number) {
	    super("No account found with number '" + number + "'");
	}
    }
}

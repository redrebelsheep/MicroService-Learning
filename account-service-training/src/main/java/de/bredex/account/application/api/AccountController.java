package de.bredex.account.application.api;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.bredex.account.domain.model.Account;
import de.bredex.account.domain.service.AccountService;

@RestController
public final class AccountController {

    private final AccountService service;

    public AccountController(final AccountService service) {
	this.service = service;
    }

    @GetMapping("/api/v1/account")
    public final ResponseEntity<List<AccountResponse>> getAccounts() {
	return ResponseEntity.ok(service.getAccounts().stream()
		.map(account -> new AccountResponse(account.getNumber(), account.getFirstName(), account.getLastName()))
		.collect(Collectors.toList()));
    }

    @GetMapping("/api/v1/account/{number}")
    public final ResponseEntity<AccountResponse> getAccount(@PathVariable final String number) {
	final Account account = service.getAccount(number);

	return ResponseEntity
		.ok(new AccountResponse(account.getNumber(), account.getFirstName(), account.getLastName()));
    }

    @PostMapping("/api/v1/account")
    public final ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
	final Account newAccount = service.createAccount(new Account(request.getFirstName(), request.getLastName()));
	return ResponseEntity.created(URI.create("/api/v1/account/" + newAccount.getNumber()))
		.body(new AccountResponse(newAccount.getNumber(), newAccount.getFirstName(), newAccount.getLastName()));
    }
}

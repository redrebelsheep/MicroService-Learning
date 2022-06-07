package de.bredex.account.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.bredex.account.domain.model.Account;
import de.bredex.account.domain.service.AccountService.NoSuchAccountException;
import de.bredex.account.domain.spi.AccountEntity;
import de.bredex.account.domain.spi.AccountRepository;

public class AccountServiceTest {

    private AccountRepository repository = mock(AccountRepository.class);
    
    private AccountService service;
    
    @BeforeEach
    public void setUp() {
	service = new AccountService(repository);
    }
    
    @Test
    public void getAccounts_returns_accounts() {
	List<AccountEntity> storedAccounts = new LinkedList<>();
	storedAccounts.add(new AccountEntity("10001", "Max", "Mustermann"));
	storedAccounts.add(new AccountEntity("10002", "Petra", "Musterfrau"));
	when(repository.findAll()).thenReturn(storedAccounts);
	
	List<Account> accounts = service.getAccounts();
	
	assertThat(accounts.size()).isEqualTo(2);
    }
    
    @Test
    public void getAccount_returns_account() {
	final AccountEntity entity = new AccountEntity("10001", "Max", "Mustermann");
	when(repository.findByNumber(any())).thenReturn(Optional.of(entity));
	
	Account account = service.getAccount("10001");
	
	assertThat(account).isNotNull();
    }
    
    @Test
    public void getAccount_throws_exception_for_non_existing_account() {
	when(repository.findByNumber(any())).thenReturn(Optional.empty());
	
	assertThrows(NoSuchAccountException.class, () -> service.getAccount("10001"));
    }
}

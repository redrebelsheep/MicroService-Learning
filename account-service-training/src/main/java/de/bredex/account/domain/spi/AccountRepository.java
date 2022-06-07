package de.bredex.account.domain.spi;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Integer>{
    
    public Optional<AccountEntity> findByNumber(final String number);
}

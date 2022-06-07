package de.bredex.inventory.domain.spi;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<BookEntity, Integer> {

    public Optional<BookEntity> findByIsbn(final String isbn);
}

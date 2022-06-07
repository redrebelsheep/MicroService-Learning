package de.bredex.inventory.domain.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.bredex.inventory.domain.model.Book;
import de.bredex.inventory.domain.spi.BookEntity;
import de.bredex.inventory.domain.spi.BookRepository;

@Service
public final class InventoryService {

    private final BookRepository repository;
    
    public InventoryService(final BookRepository repository) {
	this.repository = repository;
    }
    
    public final List<Book> getBooks() {
	final List<Book> books = new LinkedList<>();
	repository.findAll().forEach(book -> books.add(new Book(book.getIsbn(), book.getGenre(), book.getTitle(), book.getAuthor())));
	return books;
    }

    public Book getBook(final String isbn) {
	final BookEntity entity = repository.findByIsbn(isbn).orElseThrow(() -> new NoSuchBookException(isbn));
	return new Book(entity.getIsbn(), entity.getGenre(), entity.getTitle(), entity.getAuthor());
    }
    
    public class NoSuchBookException extends RuntimeException {
	
	private static final long serialVersionUID = -7782072066835327849L;

	public NoSuchBookException(final String isbn) {
	    super("No book found with isbn '" + isbn + "'");
	}
    }
}

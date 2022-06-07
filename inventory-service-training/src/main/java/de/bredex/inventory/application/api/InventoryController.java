package de.bredex.inventory.application.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.bredex.inventory.domain.model.Book;
import de.bredex.inventory.domain.service.InventoryService;

@RestController
public final class InventoryController {

    private final InventoryService service;

    public InventoryController(final InventoryService service) {
	this.service = service;
    }

    @GetMapping("/api/v1/inventory")
    public final ResponseEntity<List<BookResponse>> getBooks() {
	return ResponseEntity.ok(service.getBooks().stream()
		.map(book -> new BookResponse(book.getIsbn(), book.getGenre(), book.getTitle(), book.getAuthor()))
		.collect(Collectors.toList()));
    }
    
    @GetMapping("/api/v1/inventory/{isbn}")
    public final ResponseEntity<BookResponse> getBook(@PathVariable final String isbn) {
	final Book book = service.getBook(isbn);
	return ResponseEntity.ok(new BookResponse(book.getIsbn(), book.getGenre(), book.getTitle(), book.getAuthor()));
    }
}

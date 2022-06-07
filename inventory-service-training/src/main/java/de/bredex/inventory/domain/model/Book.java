package de.bredex.inventory.domain.model;

public final class Book {

    private final String isbn;
    private final String genre;
    private final String title;
    private final String author;

    public Book(final String isbn, final String genre, final String title, final String author) {
	this.isbn = isbn;
	this.genre = genre;
	this.title = title;
	this.author = author;
    }

    public final String getIsbn() {
	return isbn;
    }

    public final String getGenre() {
	return genre;
    }

    public final String getTitle() {
	return title;
    }

    public final String getAuthor() {
	return author;
    }
}

package de.bredex.inventory.domain.spi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String isbn;
    private String genre;
    private String title;
    private String author;
    
    public BookEntity() {}
    
    public BookEntity(final String isbn, final String genre, final String title, final String author) {
	this.isbn = isbn;
	this.genre = genre;
	this.title = title;
	this.author = author;
    }
    
    public String getIsbn() {
	return isbn;
    }
    
    public String getGenre() {
	return genre;
    }
    
    public String getTitle() {
	return title;
    }
    
    public String getAuthor() {
	return author;
    }
}

package com.ReadMe.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	
	@Id
	@Column(name = "isbn")
	private String isbn;
	
	@Column(name = "book_cover")
	private String book_cover;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "publisher")
	private String publisher;
	
	@Column(name = "published")
	private String published;
	
	@Column(name = "genre")
	private String genre;
	
	@Column(name = "price")
	private double price;
	
	@Column(name = "summary")
	private String summary;

	public Book(String cover, String title, String author, String publisher, String published,
			String genre, double price, String summary) {
		super();
		this.book_cover = cover;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.published = published;
		this.genre = genre;
		this.price = price;
		this.summary = summary;
	}

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", cover=" + book_cover + ", title=" + title
				+ ", author=" + author + ", publisher=" + publisher + ", published=" + published
				+ ", genre=" + genre + ", price=" + price + ", summary=" + summary + "]";
	}
}

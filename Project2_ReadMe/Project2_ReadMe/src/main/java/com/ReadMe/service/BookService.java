package com.ReadMe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ReadMe.model.Book;
import com.ReadMe.repository.BookRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor @AllArgsConstructor(onConstructor=@__(@Autowired))
public class BookService {
	private BookRepository bRepo;
	
	public List<Book> getAllBooks() {
		return bRepo.findAll();
	}
	
	public void insertBook(Book book) {
		bRepo.save(book);
	}
	
	public void deleteBook(Book book) {
		bRepo.delete(book);
	}
	
	public Optional<Book> getBookById(String isbn) {
		return bRepo.findById(isbn);
	}
	
	public List<Book> getBookByIsbn(String isbn) {
		return bRepo.findByIsbn(isbn);
	}
	
	public List<Book> getBookByTitle(String title) {
		return bRepo.findByTitle(title);
	}
	
	public List<Book> getBookByAuthor(String author) {
		return bRepo.findByAuthor(author);
	}
	
	public List<Book> getBookByPublisher(String publisher) {
		return bRepo.findByPublisher(publisher);
	}
	
	public List<Book> getBookByGenre(String genre) {
		return bRepo.findByGenre(genre);
	}
	
}

package com.ReadMe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ReadMe.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
	
	public List<Book> findAll();
	public Optional<Book> findById(String isbn);
	public List<Book> findByIsbn(String isbn);
	public List<Book> findByTitle(String title);
	public List<Book> findByAuthor(String author);
	public List<Book> findByPublisher(String publisher);
	public List<Book> findByGenre(String genre);
	
}

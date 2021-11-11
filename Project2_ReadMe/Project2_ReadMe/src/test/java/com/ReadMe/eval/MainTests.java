package com.ReadMe.eval;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ReadMe.controller.FrontController;
import com.ReadMe.model.Book;
import com.ReadMe.model.User;
import com.ReadMe.service.BookService;
import com.ReadMe.service.EmailSenderService;
import com.ReadMe.service.UserService;

@SpringBootTest
class MainTests {

	@Test
	void contextLoads() {
	}
	
	@Mock
	private UserService uServ;
	@Mock
	private BookService bServ;
	@Mock
	private EmailSenderService eServ;
	private FrontController control;
	List<Book> books;
	Book alice, clap, fire, potter;
	User testuser;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		control = new FrontController(uServ, bServ, eServ);
		books = new ArrayList<>();
		alice = new Book("9781503222687", null, "Alice's Adventures in Wonderland", "Lewis Carroll", "Macmillan", "26 November 1965", "Fantasy", 5.97, "Alice's Adventures in Wonderland by Lewis Carroll is a story about Alice who falls down a rabbit hole and lands into a fantasy world that is full of weird, wonderful people and animals.");
		clap = new Book("9780062882783", null, "Clap When You Land", "Elizabeth Acevedo", "HarperCollins", "05 May 2020", "Literature Fiction", 15.99, "Camino Rios lives for the summers when her father visits her in the Dominican Republic. But this time, on the day when his plane is supposed to land, Camino arrives at the airport to see crowds of crying people…");
		fire = new Book("9780062662859", null, "With The Fire On High", "Elizabeth Acevedo", "HarperCollins", "07 May 2019", "Literature Fiction", 10.99, "Ever since she got pregnant freshman year, Emoni Santiago’s life has been about making the tough decisions—doing what has to be done for her daughter and her abuela.");
		potter = new Book("9780747546245", null, "Harry Potter and the Goblet of Fire", "J.K. Rowling", "Pottermore Publishing", "08 July 2000", "Fantasy", 12.99, "It follows Harry Potter, a wizard in his fourth year at Hogwarts School of Witchcraft and Wizardry, and the mystery surrounding the entry of Harry's name into the Triwizard Tournament, in which he is forced to compete.");
		testuser = new User("username", "password", "firstname", "lastname", "test@testemail.com", "role", null, null, null);
	}
	
	@Test
	public void testGetBookByTitle() {
		books.add(alice);
		when(bServ.getBookByTitle("Alice's Adventures in Wonderland")).thenReturn(books);
		assertEquals(control.getBookByTitle("Alice's Adventures in Wonderland"), new ResponseEntity<List<Book>>(books, HttpStatus.OK));
	}
	
	@Test
	public void testCantGetBookByInvalidTitle() {
		when(bServ.getBookByTitle("no book here")).thenReturn(null);
		assertEquals(control.getBookByTitle("no book here"), new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void testGetBookByAuthor() {
		books.add(clap);
		books.add(fire);
		when(bServ.getBookByAuthor("Elizabeth Acevedo")).thenReturn(books);
		assertEquals(control.getBookByAuthor("Elizabeth Acevedo"), new ResponseEntity<List<Book>>(books, HttpStatus.OK));
	}
	
	@Test
	public void testCantGetBookByInvalidAuthor() {
		when(bServ.getBookByAuthor("no author here")).thenReturn(null);
		assertEquals(control.getBookByAuthor("no author here"), new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void testGetBookByPublisher() {
		books.add(clap);
		books.add(fire);
		when(bServ.getBookByPublisher("HarperCollins")).thenReturn(books);
		assertEquals(control.getBookByPublisher("HarperCollins"), new ResponseEntity<List<Book>>(books, HttpStatus.OK));
	}
	
	@Test
	public void testCantGetBookByInvalidPublisher() {
		when(bServ.getBookByPublisher("no publisher here")).thenReturn(null);
		assertEquals(control.getBookByPublisher("no publisher here"), new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void testGetBookByGenre() {
		books.add(alice);
		books.add(potter);
		when(bServ.getBookByGenre("Fantasy")).thenReturn(books);
		assertEquals(control.getBookByGenre("Fantasy"), new ResponseEntity<List<Book>>(books, HttpStatus.OK));
	}
	
	@Test
	public void testCantGetBookByInvalidGenre() {
		when(bServ.getBookByGenre("no genre here")).thenReturn(null);
		assertEquals(control.getBookByGenre("no genre here"), new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void testGetUserByUsername() {
		when(uServ.getUserByUsername("username")).thenReturn(testuser);
		assertEquals(control.getUserByUsername("username"), new ResponseEntity<>(testuser, HttpStatus.OK));
	}
	
	@Test
	public void testCantGetUserByInvalidUsername() {
		when(uServ.getUserByUsername("invalid")).thenReturn(null);
		assertEquals(control.getUserByUsername("invalid"), new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void testInsertUser() {
		when(uServ.getUserByUsername("username")).thenReturn(null);
		control.insertUser(testuser);
		verify(uServ).insertUser(testuser);
	}

}

package com.eval.bankapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.bankapp.dao.AccountDaoImpl;
import com.bankapp.dao.BankAppDBConnection;
import com.bankapp.dao.UserDaoImpl;
import com.bankapp.model.Account;
import com.bankapp.model.User;

@TestInstance(Lifecycle.PER_CLASS)
public class BankAppTests {
	private BankAppDBConnection bankCon = new BankAppDBConnection();
	private UserDaoImpl userDao = new UserDaoImpl(bankCon);
	private AccountDaoImpl accountDao = new AccountDaoImpl(bankCon);
	private User customer1 = new User("user123", "P4$$w0rd", "Customer", "John", "Apple", "12/31/1990", "1234567890");
	private User customer2 = new User("user456", "P4$$w0rd", "Customer", "Jane", "Doe", "02/08/1993", "3216540987");
	private Account regular = new Account("Regular", true, 100);
	private Account joint = new Account("Joint", true, 50);

	@BeforeAll
	public void setUp() {
		try (Connection con = bankCon.getDBConnection()) {
			System.out.println("Connection made.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Inserting test users into database.");
		userDao.insert(customer1);
		userDao.insert(customer2);
		System.out.println("Inserting test accounts into database.");
		accountDao.insert(regular);
		accountDao.insertJunction(customer1.getUsername(), accountDao.getAccountid());
		accountDao.insert(joint);
		accountDao.insertJunction(customer1.getUsername(), accountDao.getAccountid());
		accountDao.insertJunction(customer2.getUsername(), accountDao.getAccountid());
	}

	@Test
	public void testUserRetrieval() {
		User u = userDao.getPersonalInfo("user123");
		assertEquals("Customer", u.getUserType());
		assertEquals("John", u.getFirstName());
		assertEquals("Apple", u.getLastName());
		assertEquals("12/31/1990", u.getDob());
		assertEquals("1234567890", u.getPhoneNumber());
		System.out.println("------------------------------");
		System.out.println("User retrieval test passed.");
		System.out.println("------------------------------");
	}

	@Test
	public void testAccountRetrieval() {
		Account a = accountDao.getAccountDetails(accountDao.getAccountid());
		assertEquals("Joint", a.getAccountType());
		assertTrue(a.getStatus());
		List<String> owners = Arrays.asList("user123", "user456");
		assertEquals(owners, accountDao.getOwners(accountDao.getAccountid()));
		System.out.println("------------------------------");
		System.out.println("Account retrieval test passed.");
		System.out.println("------------------------------");
	}

	@Test
	public void testTransactions() {
		// DEPOSIT
		double amount = 1234.56;
		int id = accountDao.getAccountid();
		Account a = accountDao.getAccountDetails(id);
		accountDao.update(
				new Account(id, a.getAccountType(), a.getStatus(), a.getBalance() + amount));
		assertEquals(1284.56, accountDao.getAccountDetails(id).getBalance());

		// WITHDRAW
		amount = 500;
		a = accountDao.getAccountDetails(id);
		accountDao.update(new Account(id, a.getAccountType(), a.getStatus(), a.getBalance() - amount));
		assertEquals(784.56, accountDao.getAccountDetails(id).getBalance());
		System.out.println("------------------------------");
		System.out.println("Transactions test passed.");
		System.out.println("------------------------------");
	}

	@Test
	public void testTransfer() {
		double amount = 43.21;
		List<Account> accountList = accountDao.getAll();
		int idx = accountList.size();
		Account a1 = accountList.get(idx - 2);
		Account a2 = accountList.get(idx - 1);
		accountDao.update(new Account(a1.getAccountid(), a1.getAccountType(), a1.getStatus(), a1.getBalance() - amount));
		accountDao.update(new Account(a2.getAccountid(), a2.getAccountType(), a2.getStatus(), a2.getBalance() + amount));
		assertEquals(56.79, accountDao.getAccountDetails(a1.getAccountid()).getBalance());
		assertEquals(827.77, accountDao.getAccountDetails(a2.getAccountid()).getBalance());
		System.out.println("------------------------------");
		System.out.println("Transfer test passed.");
		System.out.println("------------------------------");
	}

	@Test
	public void testInvalidUsername() {
		User u = userDao.getPersonalInfo("asdfghjkl");
		assertEquals("", u.getFirstName());
		assertEquals("", u.getLastName());
		assertEquals("", u.getDob());
		assertEquals("", u.getPhoneNumber());
		System.out.println("------------------------------");
		System.out.println("Invalid username test passed.");
		System.out.println("------------------------------");
	}

	@AfterAll
	public void tearDown() {
		userDao.delete(customer1);
		userDao.delete(customer2);
		System.out.println("Deleted test users from database.");
		List<Account> accountList = accountDao.getAll();
		int idx = accountList.size();
		Account a1 = accountList.get(idx - 2);
		Account a2 = accountList.get(idx - 1);
		accountDao.delete(a1);
		accountDao.delete(a2);
		System.out.println("Deleted test accounts from database.");
	}
}

package com.bankapp.screens;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bankapp.dao.AccountDaoImpl;
import com.bankapp.dao.UserDaoImpl;
import com.bankapp.model.Account;
import com.bankapp.model.User;

public class CustomerServicesScreen implements Screen {
	private AccountDaoImpl accountDao = new AccountDaoImpl(BankApp.con);
	private UserDaoImpl userDao = new UserDaoImpl(BankApp.con);
	private List<User> userList = userDao.getAll();
	private static final Logger log = Logger.getLogger(CustomerServicesScreen.class);

	@Override
	public void render(Scanner sc) {
		log.trace("CustomerServicesScreen.java running...");
		int answer = -1;
		Account account = null;
		boolean running = true;
		int id = 0;
		List<Integer> accountIds = accountDao.getUserAccountIds(BankApp.u.getUsername());
		List<Boolean> accountStatuses = accountDao.getUserAccountStatuses(BankApp.u.getUsername());
		double amount = 0;
		
		loop: while (true) {
			System.out.println("=======================================");
			System.out.println("Welcome to the Customer Services Screen");
			System.out.println("       Please indicate an action       ");
			System.out.println("=======================================");
			System.out.println("(1) Open a new bank account.\n"
					+ "(2) Open a new joint account.\n" 
					+ "(3) Deposit funds.\n"
					+ "(4) Withdraw funds.\n" 
					+ "(5) Transfer funds.\n"
					+ "(0) Log out.");
			
			try {
				answer = sc.nextInt();
			} catch (InputMismatchException ime) {
				log.fatal("An InputMismatchException occurred.");
				System.out.println("Error! Invalid input. Try again.");
				sc.nextLine();
			}
			
			switch (answer) {
				case 1:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName()
									+ " selected to open a new bank account.");
					account = new Account("Regular", false, 0.00);
					accountDao.insert(account);
					accountDao.insertJunction(BankApp.u.getUsername(), accountDao.getAccountid());
					log.info("User " + BankApp.u.getUsername() + " applied for a new bank account.");
					break;
				
				case 2:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to open a new joint account.");
					List<String> usernames = new ArrayList<String>();
					for (User u: userList) {
						if (u.getUserType().equals("Customer")) { 
							usernames.add(u.getUsername());
						}
					}
					String username = null;
					while (running) {
						System.out.println("Indicate the user you'd like to open the account with: (username only)");
						try {
							username = sc.next();
							if (!usernames.contains(username)) {
								System.out.println("Error! No customer exists with username " + username + ". Try again.");
								sc.nextLine();
							} else {
								account = new Account("Joint", false, 0.00);
								accountDao.insert(account);
								id = accountDao.getAccountid();
								accountDao.insertJunction(BankApp.u.getUsername(), id); // update junction table with current user
								accountDao.insertJunction(username, id); // update junction table with provided user
								log.info("Users " + BankApp.u.getUsername() + " and " + username + " applied for a new joint bank account.");
								running = false;
							}
						} catch (InputMismatchException ime) {
							log.fatal("An InputMismatchException occurred.");
							System.out.println("Error! Invalid input. Try again.");
							sc.nextLine();
						}
					}
					break;
				
				case 3:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to deposit funds.");
					if (!accountStatuses.contains(true)) { // no approved accounts
						System.out.println("Error! A deposit request requires at least 1 approved account.");
					} else {
						account = null;
						running = true;
						while (running) {
							System.out.println("Choose an account to deposit into: " + accountIds);
							try {
								id = sc.nextInt();
								if (!accountIds.contains(id)) {
									System.out.println("Error! Invalid id. Try again.");
									sc.nextLine();
								} else {
									account = accountDao.getAccountDetails(id);
									if (account.getStatus() == false) {
										System.out.println("The account has not been approved yet. Select a different account.");
										sc.nextLine();
									} else {
										running = false;
									}
								}
							} catch (InputMismatchException ime) {
								log.fatal("An InputMismatchException occurred.");
								System.out.println("Error! Invalid input. Try again.");
								sc.nextLine();
							}
						}
						running = true;
						while (running) {
							System.out.println("Enter an amount to deposit: "); 
							try {
								amount = sc.nextDouble();
								if (amount < 0) {
									System.out.println("Error! Negative amount not allowed.");
									sc.nextLine();
								} else {
									// update database 
									accountDao.update(new Account(id, account.getAccountType(), account.getStatus(), account.getBalance() + amount));
									log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " deposited $" + amount + " into account " + id);
									running = false;
								}
							} catch (InputMismatchException ime) {
								log.fatal("An InputMismatchException occurred.");
								System.out.println("Error! Invalid input. Try again.");
								sc.nextLine();
							} 
						}
					}
					break;
				
				case 4:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to withdraw funds.");
					if (!accountStatuses.contains(true)) { // no approved accounts
						System.out.println("Error! A withdraw request requires at least 1 approved account.");
					} else {
						account = null;
						running = true;
						while (running) {
							System.out.println("Choose an account to withdraw from: " + accountIds);
							try {
								id = sc.nextInt();
								if (!accountIds.contains(id)) {
									System.out.println("Error! Invalid id. Try again.");
									sc.nextLine();
								} else {
									account = accountDao.getAccountDetails(id);
									if (account.getStatus() == false) {
										System.out.println("The account has not been approved yet. Select a different account.");
										sc.nextLine();
									} else {
										running = false;
									}
								}
							} catch (InputMismatchException ime) {
								log.fatal("An InputMismatchException occurred.");
								System.out.println("Error! Invalid input. Try again.");
								sc.nextLine();
							}
						}
						running = true;
						while (running) {
							System.out.println("Enter an amount to withdraw: "); 
							try {
								amount = sc.nextDouble();
								if (amount > 0 && amount <= account.getBalance()) {
									// update database 
									accountDao.update(new Account(id, account.getAccountType(), account.getStatus(), account.getBalance() - amount));
									log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " withdrew $" + amount + " from account " + id);
									running = false;
								} else {
									if (amount < 0) {
										System.out.println("Error! Negative amount not allowed.");
										sc.nextLine();
									} else if (amount > account.getBalance()) {
										System.out.println("Error! You do not have sufficient funds to complete this action.");
										sc.nextLine();
									} 
								}
							} catch (InputMismatchException ime) {
								log.fatal("An InputMismatchException occurred.");
								System.out.println("Error! Invalid input. Try again.");
								sc.nextLine();
							} 
						}
					}
					break;
				
				case 5:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to transfer funds.");
					int count = 0;
					for (boolean b : accountStatuses) {
						if (b) {
							count++;
						}
					}
					if (count < 2) { // not enough approved accounts
						System.out.println("Error! A transfer request requires at least 2 approved accounts.");
					} else {
						Account a1 = null, a2 = null;
						int id1 = 0, id2 = 0;
						running = true;
						while (running) {
							System.out.println("Choose an account to transfer from: " + accountIds);
							try {
								id1 = sc.nextInt();
								if (!accountIds.contains(id1)) {
									System.out.println("Error! Invalid id. Try again.");
									sc.nextLine();
								} else {
									a1 = accountDao.getAccountDetails(id1);
									if (a1.getStatus() == false) {
										System.out.println("The account has not been approved yet. Select a different account.");
										sc.nextLine();
									} else {
										if (a1.getBalance() == 0) {
											System.out.println("This account has a balance of $0.00. Select a different account.");
											sc.nextLine();
										} else {
											accountIds.remove(accountIds.indexOf(id1)); // remove source account from the list
											running = false;
										}
									}
								}
							} catch (InputMismatchException ime) {
								log.fatal("An InputMismatchException occurred.");
								System.out.println("Error! Invalid input. Try again.");
								sc.nextLine();
							}
						}
						running = true;
						while (running) {
							System.out.println("Choose an account to transfer to: " + accountIds);
							try {
								id2 = sc.nextInt();
								if (!accountIds.contains(id2)) {
									System.out.println("Error! Invalid id. Try again.");
									sc.nextLine();
								} else {
									a2 = accountDao.getAccountDetails(id2);
									if (a2.getStatus() == false) {
										System.out.println("The account has not been approved yet. Select a different account.");
										sc.nextLine();
									} else {
										running = false;
									}
								}
							} catch (InputMismatchException ime) {
								log.fatal("An InputMismatchException occurred.");
								System.out.println("Error! Invalid input. Try again.");
								sc.nextLine();
							}
						}
						running = true;
						while (running) {
							System.out.println("Enter an amount to transfer: "); 
							try {
								amount = sc.nextDouble();
								if (amount > 0 && amount < a1.getBalance()) {
									// update database 
									accountDao.update(new Account(id1, a1.getAccountType(), a1.getStatus(), a1.getBalance() - amount));
									accountDao.update(new Account(id2, a2.getAccountType(), a2.getStatus(), a2.getBalance() + amount));
									log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " transferred $" + amount + " from account " + id1 + " to account " + id2);
									running = false;
								} else {
									if (amount < 0) {
										System.out.println("Error! Negative amount not allowed.");
										sc.nextLine();
									} 
									if (amount > a1.getBalance()) {
										System.out.println("Error! You do not have sufficient funds to complete this action.");
										sc.nextLine();
									}
								}
							} catch (InputMismatchException ime) {
								log.fatal("An InputMismatchException occurred.");
								System.out.println("Error! Invalid input. Try again.");
								sc.nextLine();
							} 
						}
					}
					break;
				
				case 0:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to log out.");
					break loop;
			}
		} // end of loop
		BankApp.u = null;
		log.trace("End of CustomerServicesScreen.java...");
	}
}

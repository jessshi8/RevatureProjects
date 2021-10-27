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

public class EmployeeServicesScreen implements Screen {
	private UserDaoImpl userDao = new UserDaoImpl(BankApp.con);
	private AccountDaoImpl accountDao = new AccountDaoImpl(BankApp.con);
	private static final Logger log = Logger.getLogger(EmployeeServicesScreen.class);
	
	@Override
	public void render(Scanner sc) {
		log.trace("EmployeeServicesScreen.java running...");
		User user = null;
		Account account = null;
		int answer = -1, option = -1, id = 0;
		boolean running = false;
		
		loop: while (true) {
			List<User> userList = userDao.getAll();
			List<Account> accountList = accountDao.getAll();
			List<Integer> pendingAccounts = accountDao.getPendingAccounts();
			List<Integer> approvedAccounts = accountDao.getApprovedAccounts();

			System.out.println("=======================================");
			System.out.println("Welcome to the Employee Services Screen");
			System.out.println("       Please indicate an action       ");
			System.out.println("=======================================");
			System.out.println("(1) Approve open application.\n"
					+ "(2) Deny open application.\n" 
					+ "(3) View account information.\n"
					+ "(4) View account balances.\n" 
					+ "(5) View personal information.\n"
					+ "(0) Log out.");
			
			try {
				answer = sc.nextInt();
			} catch (InputMismatchException ime) {
				log.fatal("An InputMismatchException occurred.");
				System.out.println("Invalid input. Try again.");
				sc.nextLine();
			}
			
			switch (answer) {
				case 1:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to approve an open application.");
					if (pendingAccounts.size() == 0) {
						System.out.println("There are no pending accounts at this time.");
					} else {
						running = true;
						while (running) {
							System.out.println("Select from the list of pending accounts: " + pendingAccounts);
							try {
								id = sc.nextInt();
								if (!pendingAccounts.contains(id)) {
									System.out.println("Error! Invalid id. Try again.");
									sc.nextLine();
								} else { 
									account = accountDao.getAccountDetails(id);
									accountDao.update(new Account(id, account.getAccountType(), true, account.getBalance()));
									log.info("Account " + id + " was approved by " + BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + ".");
									running = false;
								}
							} catch (InputMismatchException ime) {
								log.fatal("An InputMismatchException occurred.");
								System.out.println("Error! Invalid id. Try again.");
								sc.nextLine();
							}
						}
					}
					
					break;
				
				case 2:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to deny an open application.");
					if (pendingAccounts.size() == 0) {
						System.out.println("There are no pending accounts at this time.");
					} else {
						running = true;
						while (running) {
							System.out.println("Select from the list of pending accounts: " + pendingAccounts);
							try {
								id = sc.nextInt();
								if (!pendingAccounts.contains(id)) {
									System.out.println("Error! Invalid id. Try again.");
									sc.nextLine();
								} else {
									account = accountDao.getAccountDetails(id);
									accountDao.deleteJunction(id);
									accountDao.delete(new Account(id, account.getAccountType(), account.getStatus(), account.getBalance()));
									log.info("Account " + id + " was denied by " + BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + ".");
									running = false;
								}
							} catch (InputMismatchException ime) {
								log.fatal("An InputMismatchException occurred.");
								System.out.println("Error! Invalid id. Try again.");
								sc.nextLine();
							}
						}
					}
					break;
				
				case 3:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to view account information.");
					if (accountList.size() == 0) {
						System.out.println("No accounts exist in the database.");
					} else {
						running = true;
						while (running) {
							System.out.println("View (1) all or (2) specific account information?");
							try {
								option = sc.nextInt();
								if (option == 1) { 
									String type = "";
									for (Account a : accountList) {
										if (a.getStatus()) {
											type = "Approved";
										} else {
											type = "Pending";
										}
										System.out.printf("Account " + a.getAccountid() + ": Type = " + a.getAccountType()
												+ ", Status = " + type + ", Balance = $%.2f" + ", Owned by " 
												+ accountDao.getOwners(a.getAccountid()) + "\n", a.getBalance());
									}
									running = false;
								}
								if (option == 2) {
									boolean running2 = true;
									int accountId = 0;
									while (running2) {
										System.out.println("Enter the id of the account you wish to view: ");
										try {
											accountId = sc.nextInt();
											if (!approvedAccounts.contains(accountId) && !pendingAccounts.contains(accountId)) {
												System.out.println("Error! Invalid id. Try again.");
												sc.nextLine();
											} else {
												account = accountDao.getAccountDetails(accountId);
												String accountStatus = "";
												if (account.getStatus()) {
													accountStatus = "Approved";
												} else {
													accountStatus = "Pending";
												}
												System.out.printf("Account " + accountId + " Information:" 
														+ "\n---------------------------------------"
														+ "\nType: " + account.getAccountType()		
														+ "\nStatus: " + accountStatus
														+ "\nBalance: $%.2f"
														+ "\nOwned by: " + accountDao.getOwners(accountId)
														+ "\n---------------------------------------\n", account.getBalance());
												running2 = false;
											}
										} catch (InputMismatchException ime) {
											log.fatal("An InputMismatchException occurred.");
											System.out.println("Error! Invalid input. Try again.");
											sc.nextLine();
										}
									}
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
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to view account balances.");
					if (accountList.size() == 0) {
						System.out.println("No accounts exist in the database.");
					} else {
						running = true;
						while (running) {
							System.out.println("View (1) all or (2) specific account balance(s)?");
							try {
								option = sc.nextInt();
								if (option == 1) { 
									for (Account a : accountList) {
										System.out.printf("Account " + a.getAccountid() + ": $%.2f\n", a.getBalance());
									}
									running = false;
								}
								if (option == 2) {
									boolean running2 = true;
									int accountId = 0;
									while (running2) {
										System.out.println("Enter the id of the account you wish to view: ");
										try {
											accountId = sc.nextInt();
											if (!approvedAccounts.contains(accountId) && !pendingAccounts.contains(accountId)) {
												System.out.println("Error! Invalid id. Try again.");
												sc.nextLine();
											} else {
												account = accountDao.getAccountDetails(accountId);
												System.out.printf("Account " + accountId + " Balance = $%.2f\n", account.getBalance());
												running2 = false;
											}
										} catch (InputMismatchException ime) {
											log.fatal("An InputMismatchException occurred.");
											System.out.println("Error! Invalid input. Try again.");
											sc.nextLine();
										}
									}
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
				
				case 5:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to view personal information.");
					if (userList.size() == 0) {
						System.out.println("No users exist in the database.");
					} else {
						running = true;
						while (running) {
							System.out.println("View (1) all or (2) specific personal information?");
							try {
								option = sc.nextInt();
								if (option == 1) { 
									for (User u : userList) {
										System.out.println("[" + u.getUsername() + "] " + "Name: " + u.getFirstName() 
											+ " " + u.getLastName() + ", DOB: " + u.getDob() + ", Phone Number: " + u.getPhoneNumber());
									}
									running = false;
								}
								if (option == 2) {
									boolean running2 = true;
									String username = "";
									while (running2) {
										System.out.println("Provide a username: ");
										try {
											username = sc.next();
											List<String> usernames = new ArrayList<>();
											for (User u : userList) {
												usernames.add(u.getUsername());
											}
											if (!usernames.contains(username)) {
												System.out.println("Error! Username does not exist. Try again.");
												sc.nextLine();
											} else {
												user = userDao.getPersonalInfo(username);
												System.out.println("Personal Information of [" + username + "]" 
														+ "\n---------------------------------------"
														+ "\nType: " + user.getUserType() 
														+ "\nName: " + user.getFirstName() + " " + user.getLastName()
														+ "\nDate of Birth: " + user.getDob()
														+ "\nPhone Number: " + user.getPhoneNumber()
														+ "\n---------------------------------------\n");
											}
											running2 = false;
										} catch (InputMismatchException ime) {
											log.fatal("An InputMismatchException occurred.");
											System.out.println("Error! Invalid input. Try again.");
											sc.nextLine();
										}
									}
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
				
				case 0:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to log out.");
					break loop;
			}
		} // end of loop
		BankApp.u = null;
		log.trace("End of EmployeeServicesScreen.java...");
	}
}

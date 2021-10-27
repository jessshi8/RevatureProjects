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

public class AdminServicesScreen implements Screen {
	private UserDaoImpl userDao = new UserDaoImpl(BankApp.con);
	private AccountDaoImpl accountDao = new AccountDaoImpl(BankApp.con);
	private static final Logger log = Logger.getLogger(AdminServicesScreen.class);
	
	@Override
	public void render(Scanner sc) {
		log.trace("AdminServicesScreen.java running...");
		User user = null;
		Account account = null;
		int answer = -1, option = -1, id = 0;
		boolean running = false;
		double amount = 0;

		loop: while (true) {
			List<User> userList = userDao.getAll();
			List<Account> accountList = accountDao.getAll();
			List<Integer> pendingAccounts = accountDao.getPendingAccounts();
			List<Integer> approvedAccounts = accountDao.getApprovedAccounts();
			int count = 0;
			for (Account a : accountList) {
				if (a.getStatus()) {
					count++;
				}
			}
			System.out.println("=====================================");
			System.out.println("Welcome to the Admin Services Screen ");
			System.out.println("      Please indicate an action      ");
			System.out.println("=====================================");
			System.out.println("(1) Approve open application.\n"
					+ "(2) Deny open application.\n" 
					+ "(3) Cancel account.\n"
					+ "(4) Deposit funds.\n" 
					+ "(5) Withdraw funds.\n"
					+ "(6) Transfer funds.\n"
					+ "(7) View user information.\n"
					+ "(8) View account information.\n"
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
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to cancel an account.");
					if (accountList.size() == 0) {
						System.out.println("No accounts exist in the database.");
					} else {
						running = true;
						while (running) {
							System.out.println("Enter an account id: ");
							try {
								id = sc.nextInt();
								if (!pendingAccounts.contains(id) && !approvedAccounts.contains(id)) {
									System.out.println("Error! Invalid id. Try again.");
									sc.nextLine();
								} else {
									account = accountDao.getAccountDetails(id);
									accountDao.deleteJunction(id);
									accountDao.delete(new Account(id, account.getAccountType(), account.getStatus(), account.getBalance()));
									log.info("Account " + id + " was cancelled by " + BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + ".");
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
				
				case 4:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to deposit funds.");
					if (count < 1) {
						System.out.println("Error! A deposit request requires at least 1 approved account.");
					} else {
						account = null;
						running = true;
						while (running) {
							System.out.println("Enter the account id you wish to deposit into: ");
							try {
								id = sc.nextInt();
								if (!approvedAccounts.contains(id)) {
									System.out.println("The account has not been approved yet. Select a different account.");
									sc.nextLine();
								} else {
									account = accountDao.getAccountDetails(id);
									running = false;
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
				
				case 5:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to withdraw funds.");
					if (count < 1) {
						System.out.println("Error! A withdraw request requires at least 1 approved account.");
					} else {
						account = null;
						running = true;
						while (running) {
							System.out.println("Enter the account id you wish to withdraw from: ");
							try {
								id = sc.nextInt();
								if (!approvedAccounts.contains(id)) {
									System.out.println("The account has not been approved yet. Select a different account.");
									sc.nextLine();
								} else {
									account = accountDao.getAccountDetails(id);
									running = false;
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
									System.out.printf("Transaction successful. Account " + id + " has a balance of $%.2f\n", account.getBalance() - amount);
									running = false;
								} else {
									if (amount < 0) {
										System.out.println("Error! Negative amount not allowed.");
										sc.nextLine();
									} 
									if (amount > account.getBalance()) {
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
				
				case 6:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to transfer funds.");
					if (count < 2) {
						System.out.println("Error! A transfer request requires at least 2 approved accounts.");
					} else {
						Account a1 = null, a2 = null;
						int id1 = 0, id2 = 0;
						running = true;
						while (running) {
							System.out.println("Enter the account id you wish to transfer funds from: ");
							try {
								id1 = sc.nextInt();
								if (!approvedAccounts.contains(id1)) {
									System.out.println("The account has not been approved yet. Select a different account.");
									sc.nextLine();
								} else {
									a1 = accountDao.getAccountDetails(id1);
									if (a1.getBalance() == 0) {
										System.out.println("This account has a balance of $0.00. Select a different account.");
										sc.nextLine();
									} else {
										approvedAccounts.remove(approvedAccounts.indexOf(id1)); // remove source account from approved list
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
							System.out.println("Enter the account id you wish to transfer funds to: ");
							try {
								id2 = sc.nextInt();
								if (!approvedAccounts.contains(id2)) {
									System.out.println("The account has not been approved yet. Select a different account.");
									sc.nextLine();
								} else {
									a2 = accountDao.getAccountDetails(id2);
									running = false;
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
										System.out.println("Error! The source account does not have sufficient funds to complete this action.");
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
				
				case 7:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to view user information.");
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
				
				case 8:
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
				
				case 0:
					log.info(BankApp.u.getFirstName() + " " + BankApp.u.getLastName() + " selected to log out.");
					break loop;
			}
		} // end of loop
		BankApp.u = null;
		log.trace("End of AdminServicesScreen.java...");
	}
}

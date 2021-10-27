package com.bankapp.screens;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bankapp.dao.UserDaoImpl;
import com.bankapp.model.User;

public class LandingScreen implements Screen {
	static String userType;
	private static final Logger log = Logger.getLogger(LandingScreen.class);
	private UserDaoImpl userDao = new UserDaoImpl(BankApp.con);
	private List<User> userList = userDao.getAll();

	@Override
	public void render(Scanner sc) {
		log.trace("LandingScreen.java running...");
		int answer = -1;
		
		loop: while (true) {
			System.out.println("==================================");
			System.out.println("Welcome to the Bank Landing Screen");
			System.out.println("     Please indicate an action    ");
			System.out.println("==================================");
			System.out.println("(1) Log into an existing account.\n"
					+ "(2) Create a customer account.\n"
					+ "(3) Create an employee account.\n"
					+ "(4) Create an admin account.\n"
					+ "(0) Exit the system.");
			
			try {
				answer = sc.nextInt();
			} catch (InputMismatchException ime) {
				log.fatal("An InputMismatchException occurred.");
				System.out.println("Error! Invalid input. Try again.");
				sc.nextLine();
			}
			
			switch (answer) {
			case 1:
				log.info("User selected to log into an existing account.");
				if (userList.size() == 0) {
					System.out.println("No users exist in the database.");
				} else {
					new LoginScreen().render(sc);
				}
				break;
			case 2:
				log.info("User selected to create a customer account.");
				userType = "Customer";
				new UserCredentialsScreen().render(sc);
				break;
			case 3:
				log.info("User selected to create an employee account.");
				userType = "Employee";
				new UserCredentialsScreen().render(sc);
				break;
			case 4:
				log.info("User selected to create an admin account.");
				userType = "Admin";
				new UserCredentialsScreen().render(sc);
				break;
			case 0:
				log.info("User selected to exit the application.");
				break loop;
			}
			if (BankApp.u != null) {
				log.info("Login successful.");
				if (BankApp.u.getUserType().equals("Customer")) {
					log.info("Rerouting to Customer Services Screen.");
					new CustomerServicesScreen().render(sc);
				} else if (BankApp.u.getUserType().equals("Employee")) {
					log.info("Rerouting to Employee Services Screen.");
					new EmployeeServicesScreen().render(sc);
				} else {
					log.info("Rerouting to Admin Services Screen.");
					new AdminServicesScreen().render(sc);
				}
			}
		} // end of loop
		BankApp.done = true; // user selected exit
		log.trace("End of LandingScreen.java...");
	}
}

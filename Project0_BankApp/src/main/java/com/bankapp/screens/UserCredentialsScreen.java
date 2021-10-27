package com.bankapp.screens;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.bankapp.dao.UserDaoImpl;
import com.bankapp.model.User;

public class UserCredentialsScreen implements Screen {
	private static final Logger log = Logger.getLogger(UserCredentialsScreen.class);
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String dob;
	private String phoneNumber;
	
	@Override
	public void render(Scanner sc) {
		log.trace("UserCredentialsScreen.java running...");
		boolean running = true;
		
		System.out.println("======================================");
		System.out.println("Welcome to the User Credentials Screen");
		System.out.println("Please fill out the information below");
		System.out.println("======================================");
		
		while (running) {
			System.out.println("First Name: ");
			try {
				firstName = sc.next("[A-Z]{1}[a-z]*");
				running = false;
			} catch (InputMismatchException ime) {
				log.fatal("An InputMismatchException occurred.");
				System.out.println("Error! The name you enter should be properly capitalized and only contain letters.");
				sc.nextLine();
			}
		}
		
		running = true;
		while (running) {
			System.out.println("Last Name: ");
			try {
				lastName = sc.next("[A-Z]{1}[a-z]*");
				running = false;
			} catch (InputMismatchException ime) {
				log.fatal("An InputMismatchException occurred.");
				System.out.println("Error! The name you enter should be properly capitalized and only contain letters.");
				sc.nextLine();
			}
		}
		
		running = true;
		while (running) {
			System.out.println("Date of Birth: (MM/DD/YYYY)");
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			try {
				dob = sc.next();
				format.parse(dob);
				int month = Integer.parseInt(dob.split("/")[0]);
				int day = Integer.parseInt(dob.split("/")[1]);
				int year = Integer.parseInt(dob.split("/")[2]);
				
				if (month <= 12 && day <= 31 && year <= 2021) {
					if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
						if (day <= 31) {
							running = false;
						} else {
							System.out.println("Error! The date you entered is not valid.");
							sc.nextLine();
						}
					} else if (month == 4 || month == 6 || month == 9 || month == 11) {
						if (day <= 30) {
							running = false;
						} else {
							System.out.println("Error! The date you entered is not valid.");
							sc.nextLine();
						}
					} else if (month == 2) {
						if (day <= 28 && (year % 4) != 0) {
							running = false;
						} else if (day <= 29 && (year % 4) == 0) {
							running = false;
						} else {
							System.out.println("Error! The date you entered is not valid.");
							sc.nextLine();
						}
					} else {
						System.out.println("Error! The date you entered is not valid.");
						sc.nextLine();
					}
				} else {
					System.out.println("Error! The date you entered is not valid.");
					sc.nextLine();
				}
			} catch (ParseException e) {
				log.fatal("A ParseException occurred.");
				System.out.println("Error! The date you entered is not formatted correctly.");
				sc.nextLine();
			}	
		}
		
		running = true;
		while (running)
		{
			System.out.println("Phone Number: ");
			try {
				phoneNumber = sc.next("[0-9]{10}");
				running = false;
			} catch (InputMismatchException ime) {
				log.fatal("An InputMismatchException occurred.");
				System.out.println("Error! The number you enter should only contain numbers.");
				sc.nextLine();
			}
		}
		
		running = true;
		while (running) {
			System.out.println("Create a username: ");
			try {
				username = sc.next("[a-zA-Z0-9]+");
				running = false;
			} catch (InputMismatchException ime) {
				log.fatal("An InputMismatchException occurred.");
				System.out.println("Error! The username you enter should only contain letters and numbers.");
				sc.nextLine();
			}
		}
		
		running = true;
		while (running) {
			System.out.println("Create a password: (must contain at least 8 to 12 characters, one digit, \n" +
					"one uppercase letter, one lowercase letter, and one special character)");
			try {
				String regex = "^(?=.*[0-9])"
	                       + "(?=.*[a-z])(?=.*[A-Z])"
	                       + "(?=.*[@#$%^&+=])"
	                       + "(?=\\S+$).{8,20}$";
				Pattern p = Pattern.compile(regex);
				password = sc.next();
				Matcher m = p.matcher(password);
				if (m.matches()) {
					running = false;
				} else {
					System.out.println("Error! The password you entered is not a valid password.");
					System.out.println("");
					sc.nextLine();
				}
			} catch (InputMismatchException ime) {
				log.fatal("An InputMismatchException occurred.");
				System.out.println("Error! The password you entered is not a valid password.");
				sc.nextLine();
			}
		}
		
		// add user to database
		User u = new User(username.toLowerCase(), password, LandingScreen.userType, firstName, lastName, dob, phoneNumber);
		UserDaoImpl userDao = new UserDaoImpl(BankApp.con);
		userDao.insert(u);
		log.info(u.getFirstName() + " " + u.getLastName() + " (" + u.getUsername() + ") was added to the database.");
		
		// return to Landing Screen
		log.trace("End of UserCredentialsScreen.java...");
	}
}

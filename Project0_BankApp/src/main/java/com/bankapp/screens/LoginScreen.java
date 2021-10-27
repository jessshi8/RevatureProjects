package com.bankapp.screens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bankapp.dao.UserDaoImpl;
import com.bankapp.model.User;

public class LoginScreen implements Screen {
	private UserDaoImpl userDao = new UserDaoImpl(BankApp.con);
	private List<User> userList = userDao.getAll();
	private static final Logger log = Logger.getLogger(LoginScreen.class);
	
	@Override
	public void render(Scanner sc) {
		log.trace("LoginScreen.java running...");
		boolean running = true;
		String username = null, password = null;
		Map<String, String> logins = new HashMap<String, String>();
		for (User u : userList) {
			logins.put(u.getUsername(), u.getPassword());
		}
		
		System.out.println("=====================================");
		System.out.println("   Welcome to the Bank Login Screen  ");
		System.out.println(" Please log in with your credentials ");
		System.out.println("=====================================");
		
		while (running) {
			System.out.println("Enter your username: ");
			username = sc.next();
			if (!logins.containsKey(username)) {
				System.out.println("Error! Username failed. Please recheck the username and try again.");
				sc.nextLine();
			} else {
				running = false;
			}
		}
		
		running = true;
		while (running) {
			System.out.println("Enter your password: ");
			password = sc.next();
			if (!logins.get(username).equals(password)) {
				System.out.println("Error! Password failed. Please recheck the password and try again.");
				sc.nextLine();
			} else {
				running = false;
			}
		}
		
		// Login Successful
		for (User u : userList) {
			if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
				BankApp.u = u;
			}
		}
		log.trace("End of LoginScreen.java...");
	}
}

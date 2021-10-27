package com.bankapp.screens;

import java.util.Scanner;
import org.apache.log4j.Logger;

import com.bankapp.dao.BankAppDBConnection;
import com.bankapp.model.User;

public class BankApp {
	public static BankAppDBConnection con = new BankAppDBConnection();
	public static User u = null;
	public static boolean done = false;
	private static final Logger log = Logger.getLogger(BankApp.class);
	
	public static void main(String[] args) {
		log.trace("BankApp.java running...");
		Scanner sc = new Scanner(System.in);
		Screen landingScreen = new LandingScreen();
		
		while (!done) {
			landingScreen.render(sc);
		}
		
		sc.close();
		log.info("Bank application closed.");
		log.trace("End of BankApp.java...");
	}
}

package com.ers.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.ers.dao.DBConnection;
import com.ers.dao.RequestDaoImpl;
import com.ers.dao.UserDaoImpl;
import com.ers.model.Request;
import com.ers.model.User;
import com.ers.service.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Controller {
	private static final Logger log = Logger.getLogger(Controller.class);
	private static DBConnection dbCon = new DBConnection();
	private static UserDaoImpl uDao = new UserDaoImpl(dbCon);
	private static RequestDaoImpl rDao = new RequestDaoImpl(dbCon);
	private static Service serv = new Service(uDao, rDao);
	
	// JSONDispatcher Functions
	public static void getSessionUser(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		System.out.println("in controller getSessionUser");
		User u = (User)req.getSession().getAttribute("currentUser"); // grabbing the current user that is stored in the session from login
		res.getWriter().write(new ObjectMapper().writeValueAsString(u)); // sending the logged in user as a JSON to the front-end
	}
	
	public static void getTickets(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		System.out.println("in controller getSessionTickets");
		List<Request> tickets = (List<Request>) req.getSession().getAttribute("tickets");
		res.getWriter().write(new ObjectMapper().writeValueAsString(tickets));
	}
	
	public static void getDetails(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		System.out.println("in controller, getDetails");
		List<List<String>> details = (List<List<String>>) req.getSession().getAttribute("details"); 
		res.getWriter().write(new ObjectMapper().writeValueAsString(details));
	}
	
	public static void getAllDetails(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		System.out.println("in controller, getAllDetails");
		List<List<String>> details = (List<List<String>>) req.getSession().getAttribute("details"); 
		res.getWriter().write(new ObjectMapper().writeValueAsString(details));
	}
	
	// ViewDispatcher Functions
	public static String login(HttpServletRequest req) { // called by index.html
		System.out.println("in controller login");
		
		if (!req.getMethod().equals("POST")) { // prevent login if user is not using an HTTP post
			return "html/fail.html";
		} 
		// process out the information that is sent in the request
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String encrypted = sha256(password);
		User u = serv.login(username, encrypted);
		if (u == null) {
			return "wrongcredentials.change";
		} else {
			req.getSession().setAttribute("currentUser", u);
			if (u.getRoleId() == 0) {
				log.info("Manager login successful");
				// redirect to manager home page
				return "html/manager-home.html";
			} else {
				log.info("Employee login successful");
				// redirect to employee home page
				return "html/employee-home.html";
			}
		}
	}
	
	public static String logout(HttpServletRequest req) {
		log.info("Logout successful");
		HttpSession sesh = req.getSession();
		sesh.invalidate();
		return "html/redirect-index.html";
	}
	
	public static String register(HttpServletRequest req) { // called by register.html
		System.out.println("in controller register");
		
		if (!req.getMethod().equals("POST")) { // prevent submission if user is not using an HTTP post
			return "html/fail.html";
		} 
		// process out the information that is sent in the request
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String encrypted = sha256(password);
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String email = req.getParameter("email");

		serv.insertUser(new User(username, encrypted, firstname, lastname, email, 1));
		return "html/redirect-index.html";
	}
	
	public static String addRequest(HttpServletRequest req) { // called by employee-home.html
		System.out.println("in controller addRequest");
		User u = (User) req.getSession().getAttribute("currentUser");
		
		if (!req.getMethod().equals("POST")) { // prevent submission if user is not using an HTTP post
			return "html/fail.html";
		} 
		
		// process out the information that is sent in the request
		String amountPart = req.getParameter("amount");
		double amount = Double.parseDouble(amountPart);
		
		String description = req.getParameter("description");
		
		Part receiptPart = null;
		try {
			receiptPart = req.getPart("receipt");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ServletException e1) {
			e1.printStackTrace();
		}
		// converting receiptPart to byte array
		InputStream in = null;
		BufferedImage img = null;
		ByteArrayOutputStream baos = null;
		byte[] receipt = null;
		if (receiptPart != null) {
			try {
				in = receiptPart.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (in != null) {
			try {
				img = ImageIO.read(in);
				baos = new ByteArrayOutputStream();
				ImageIO.write(img, "jpg", baos);
				receipt = baos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (receipt == null) {
			return "html/fail.html";
		}
		
		int type = -1;
		switch(req.getParameter("request-type")) {
			case "travel":
				type = 1;
				break;
			case "mileage":
				type = 2;
				break;
			case "lodging":
				type = 3;
				break;
			case "meals":
				type = 4;
				break;
			case "medical":
				type = 5;
				break;
			case "other":
				type = 6;
		}
		
		// getting the submitted time
		java.util.Date date = new java.util.Date();
		java.sql.Timestamp currTime = new java.sql.Timestamp(date.getTime());
		
		// add reimbursement request to database
		serv.insertRequest(new Request(amount, currTime, null, description, receipt, u.getUserId(), 0, 1, type));
		
		// update junction table
		serv.addRelationship(u.getUsername(), serv.getRecentRequest());
		
		return "html/employee-home.html";
	}
	
	public static String viewHistory(HttpServletRequest req) { // called by employee-home.html
		System.out.println("in controller viewHistory");
		log.info("Employee viewed ticket history");
		User u = (User) req.getSession().getAttribute("currentUser");
		
		List<Request> rList = serv.getPastTickets(u.getUsername());
		List<String> typeDetails = new ArrayList<>();
		List<String> resolverDetails = new ArrayList<>();
		List<String> statusDetails = new ArrayList<>();
		for (Request r : rList) {
			typeDetails.add(serv.getType(r.getTypeid()));
			resolverDetails.add(serv.getResolver(r.getResolverId()));
			statusDetails.add(serv.getStatus(r.getStatusid()));
		}
		List<List<String>> details = Arrays.asList(typeDetails, resolverDetails, statusDetails);
		req.getSession().setAttribute("tickets", rList);
		req.getSession().setAttribute("details", details);
		saveUser(req);
		return "html/view-session-tickets.html";
	}
	
	public static String filterPending(HttpServletRequest req) {
		System.out.println("in controller filterPending");
		log.info("Manager filtered requests by pending status");
		List<Request> pendingTickets = serv.getFilteredTickets(1);
		List<String> typeDetails = new ArrayList<>();
		List<String> authorDetails = new ArrayList<>();
		List<String> resolverDetails = new ArrayList<>();
		List<String> statusDetails = new ArrayList<>();
		for (Request r : pendingTickets) {
			typeDetails.add(serv.getType(r.getTypeid()));
			authorDetails.add(serv.getAuthor(r.getAuthorId()));
			resolverDetails.add(serv.getResolver(r.getResolverId()));
			statusDetails.add(serv.getStatus(r.getStatusid()));
		}
		List<List<String>> details = Arrays.asList(typeDetails, authorDetails, resolverDetails, statusDetails);
		req.getSession().setAttribute("details", details);
		req.getSession().setAttribute("tickets", pendingTickets);
		saveUser(req);
		return "html/view-pending-tickets.html";
	}
	
	public static String filterApproved(HttpServletRequest req) {
		System.out.println("in controller filterApproved");
		log.info("Manager filtered requests by approved status");
		List<Request> approvedTickets = serv.getFilteredTickets(2);
		List<String> typeDetails = new ArrayList<>();
		List<String> authorDetails = new ArrayList<>();
		List<String> resolverDetails = new ArrayList<>();
		List<String> statusDetails = new ArrayList<>();
		for (Request r : approvedTickets) {
			typeDetails.add(serv.getType(r.getTypeid()));
			authorDetails.add(serv.getAuthor(r.getAuthorId()));
			resolverDetails.add(serv.getResolver(r.getResolverId()));
			statusDetails.add(serv.getStatus(r.getStatusid()));
		}
		List<List<String>> details = Arrays.asList(typeDetails, authorDetails, resolverDetails, statusDetails);
		req.getSession().setAttribute("details", details);
		req.getSession().setAttribute("tickets", approvedTickets);
		saveUser(req);
		return "html/view-approved-tickets.html";
	}
	
	public static String filterDenied(HttpServletRequest req) {
		System.out.println("in controller filterDenied");
		log.info("Manager filtered requests by denied status");
		List<Request> deniedTickets = serv.getFilteredTickets(0);
		List<String> typeDetails = new ArrayList<>();
		List<String> authorDetails = new ArrayList<>();
		List<String> resolverDetails = new ArrayList<>();
		List<String> statusDetails = new ArrayList<>();
		for (Request r : deniedTickets) {
			typeDetails.add(serv.getType(r.getTypeid()));
			authorDetails.add(serv.getAuthor(r.getAuthorId()));
			resolverDetails.add(serv.getResolver(r.getResolverId()));
			statusDetails.add(serv.getStatus(r.getStatusid()));
		}
		List<List<String>> details = Arrays.asList(typeDetails, authorDetails, resolverDetails, statusDetails);
		req.getSession().setAttribute("details", details);
		req.getSession().setAttribute("tickets", deniedTickets);
		saveUser(req);
		return "html/view-denied-tickets.html";
	}
	
	public static String viewTickets(HttpServletRequest req) { // called by manager-home.html
		System.out.println("in controller viewTickets");
		log.info("Manager viewed all reimbursement requests");
		List<Request> rList = serv.getAllRequests();
		List<String> typeDetails = new ArrayList<>();
		List<String> authorDetails = new ArrayList<>();
		List<String> resolverDetails = new ArrayList<>();
		List<String> statusDetails = new ArrayList<>();
		for (Request r : rList) {
			typeDetails.add(serv.getType(r.getTypeid()));
			authorDetails.add(serv.getAuthor(r.getAuthorId()));
			resolverDetails.add(serv.getResolver(r.getResolverId()));
			statusDetails.add(serv.getStatus(r.getStatusid()));
		}
		List<List<String>> details = Arrays.asList(typeDetails, authorDetails, resolverDetails, statusDetails);
		req.getSession().setAttribute("tickets", rList);
		req.getSession().setAttribute("details", details);
		saveUser(req);
		return "html/view-all-tickets.html";
	}

	public static String redirectEmployeeHome(HttpServletRequest req) {
		saveUser(req);
		return "html/employee-home.html";
	}
	
	public static String redirectManagerHome(HttpServletRequest req) {
		saveUser(req);
		return "html/manager-home.html";
	}

	public static String approve(HttpServletRequest req) {
		System.out.println("in controller approve");
		User u = (User) req.getSession().getAttribute("currentUser");
		
		if (!req.getMethod().equals("POST")) { // prevent submission if user is not using an HTTP post
			return "html/fail.html";
		} 
		// process out the information that is sent in the request
		String id = req.getParameter("id1");
		Request r = serv.getRequestInfo(Integer.parseInt(id));
		// update status, resolver, and resolved time
		r.setStatusid(2);
		r.setResolverId(u.getUserId());
		java.util.Date date = new java.util.Date();
		java.sql.Timestamp currTime = new java.sql.Timestamp(date.getTime());
		r.setResolved(currTime);
		serv.updateRequest(r);
		log.info("Manager sucessfully approved the request");
		saveUser(req);
		return viewTickets(req);
	}
	
	public static String deny(HttpServletRequest req) {
		System.out.println("in controller deny");
		User u = (User) req.getSession().getAttribute("currentUser");
		
		if (!req.getMethod().equals("POST")) { // prevent submission if user is not using an HTTP post
			return "html/fail.html";
		} 
		// process out the information that is sent in the request
		String id = req.getParameter("id2");
		Request r = serv.getRequestInfo(Integer.parseInt(id));
		// update status, resolver, and resolved time
		r.setStatusid(0);
		r.setResolverId(u.getUserId());
		java.util.Date date = new java.util.Date();
		java.sql.Timestamp currTime = new java.sql.Timestamp(date.getTime());
		r.setResolved(currTime);
		serv.updateRequest(r);
		log.info("Manager successfully denied the request");
		return viewTickets(req);
	}
	
	public static void saveUser(HttpServletRequest req) {
		User u = (User) req.getSession().getAttribute("currentUser");
		req.getSession().setAttribute("currentUser", u);
	}	
	
	private static String sha256(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			final byte[] hash = digest.digest(base.getBytes("UTF-8"));
			final StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < hash.length; i++) {
				final String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
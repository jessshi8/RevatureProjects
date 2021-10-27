package com.ers.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ers.controller.Controller;
import com.ers.dao.DBConnection;

public class ViewDispatcher {
	private static final Logger log = Logger.getLogger(ViewDispatcher.class);
	
	public String process(HttpServletRequest req, HttpServletResponse res) {
		switch (req.getRequestURI()) {
		case "/Project1_ERS/login.change":
			System.out.println("in view dispatcher, login.change");
			return Controller.login(req);

		case "/Project1_ERS/logout.change":
			System.out.println("in view dispatcher, logout.change");
			return Controller.logout(req);

		case "/Project1_ERS/register.change":
			System.out.println("in view dispatcher, register.change");
			return Controller.register(req);

		case "/Project1_ERS/viewHistory.change":
			System.out.println("in view dispatcher, viewHistory.change");
			return Controller.viewHistory(req);

		case "/Project1_ERS/request.change":
			System.out.println("in view dispatcher, request.change");
			return Controller.addRequest(req);

		case "/Project1_ERS/viewTickets.change":
			System.out.println("in view dispatcher, viewTickets.change");
			return Controller.viewTickets(req);

		case "/Project1_ERS/employeeHome.change":
			System.out.println("in view dispatcher, employeeHome.change");
			return Controller.redirectEmployeeHome(req);

		case "/Project1_ERS/managerHome.change":
			System.out.println("in view dispatcher, managerHome.change");
			return Controller.redirectManagerHome(req);

		case "/Project1_ERS/approve.change":
			System.out.println("in view dispatcher, approve.change");
			return Controller.approve(req);

		case "/Project1_ERS/deny.change":
			System.out.println("in view dispatcher, deny.change");
			return Controller.deny(req);
		
		case "/Project1_ERS/filterPending.change":
			System.out.println("in view dispatcher, filterPending.change");
			return Controller.filterPending(req);

		case "/Project1_ERS/filterApproved.change":
			System.out.println("in view dispatcher, filterApproved.change");
			return Controller.filterApproved(req);

		case "/Project1_ERS/filterDenied.change":
			System.out.println("in view dispatcher, filterPending.change");
			return Controller.filterDenied(req);
		 
		default:
			System.out.println("in view dispatcher, default");
			return "html/fail.html";
		}
	}
}

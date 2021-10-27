package com.ers.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ers.controller.Controller;
import com.ers.dao.DBConnection;
import com.ers.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONDispatcher {
	private static final Logger log = Logger.getLogger(JSONDispatcher.class);
	
	public void process(HttpServletRequest req, HttpServletResponse res) throws IOException {
		switch(req.getRequestURI()) {
			case "/Project1_ERS/getSessionUser.json":
				System.out.println("in json dispatcher, getSessionUser");
				try {
					Controller.getSessionUser(req, res);
				} catch(JsonProcessingException e) {
					log.fatal("A JsonProcessingException occurred.");
				} catch(IOException e) {
					log.fatal("An IOException occurred.");
				}
				break;
				
			case "/Project1_ERS/getTickets.json":
				System.out.println("in json dispatcher, getSessionTickets");
				try {
					Controller.getTickets(req, res);
				} catch(JsonProcessingException e) {
					log.fatal("A JsonProcessingException occurred.");
				} catch(IOException e) {
					log.fatal("An IOException occurred.");
				}
				break;
				
			case "/Project1_ERS/getDetails.json":
				System.out.println("in json dispatcher, getDetails");
				try {
					Controller.getDetails(req, res);
				} catch(JsonProcessingException e) {
					log.fatal("A JsonProcessingException occurred.");
				} catch(IOException e) {
					log.fatal("An IOException occurred.");
				}
				break;
				
			case "/Project1_ERS/getAllDetails.json":
				System.out.println("in json dispatcher, getAllDetails");
				try {
					Controller.getAllDetails(req, res);
				} catch(JsonProcessingException e) {
					log.fatal("A JsonProcessingException occurred.");
				} catch(IOException e) {
					log.fatal("An IOException occurred.");
				}
				break;
				
			default: 
				System.out.println("in json dispatcher, default");
				res.getWriter().write(new ObjectMapper().writeValueAsString(new User()));
		}
	}
}

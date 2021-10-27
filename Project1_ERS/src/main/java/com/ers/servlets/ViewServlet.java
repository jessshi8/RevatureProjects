package com.ers.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
public class ViewServlet extends HttpServlet {
	public ViewDispatcher disp = new ViewDispatcher();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		req.getRequestDispatcher(disp.process(req, res)).forward(req, res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		req.getRequestDispatcher(disp.process(req, res)).forward(req, res);
	}
}

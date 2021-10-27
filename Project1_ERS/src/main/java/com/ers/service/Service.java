package com.ers.service;

import java.util.ArrayList;
import java.util.List;

import com.ers.dao.RequestDaoImpl;
import com.ers.dao.UserDaoImpl;
import com.ers.model.Request;
import com.ers.model.User;

public class Service {
	private final UserDaoImpl uDao;
	private final RequestDaoImpl rDao;
	
	public Service() {
		this.uDao = new UserDaoImpl();
		this.rDao = new RequestDaoImpl();
	}

	public Service(UserDaoImpl uDao, RequestDaoImpl rDao) {
		super();
		this.uDao = uDao;
		this.rDao = rDao;
	}
	
	public List<User> getAllUsers() {
		return uDao.getAll();
	}
	
	public List<Request> getAllRequests() {
		return rDao.getAll();
	}
	
	public int getRecentRequest() {
		return rDao.getNewRequestId();
	}
	
	public void insertUser(User u) {
		uDao.insert(u);
	}
	
	public void insertRequest(Request r) {
		rDao.insert(r);
	}
	
	public void updateRequest(Request r) {
		rDao.update(r);
	}
	
	public void addRelationship(String username, int id) {
		rDao.insertJunction(username, id);
	}
	
	public User login(String username, String password) {
		User u = uDao.getUserByUsername(username);
		if (u != null) {
			if (u.getPassword().equals(password)) {
				return u;
			}
		}
		return null;
	}
	
	public Request getRequestInfo(int id) {
		return rDao.getRequestById(id);
	}
	
	public List<Request> getFilteredTickets(int id) {
		return rDao.filterRequests(id);
	}
	
	public List<Request> getPastTickets(String username) {
		List<Integer> idList = rDao.getUserRequestIds(username);
		List<Request> rList = new ArrayList<>();
		for (int i : idList) {
			rList.add(rDao.getRequestById(i));
		}
		return rList;
	}
	
	public String getAuthor(int id) {
		return uDao.getLegalNameById(id);
	}
	
	public String getResolver(int id) {
		return uDao.getLegalNameById(id);
	}
	
	public String getType(int id) {
		return rDao.getRequestType(id);
	}
	
	public String getStatus(int id) {
		return rDao.getRequestStatus(id);
	}
}

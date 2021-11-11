package com.ReadMe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ReadMe.model.User;
import com.ReadMe.repository.UserRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class UserService {
	private UserRepository uRepo;

	@Autowired
	public UserService(UserRepository uRepo) {
		super();
		this.uRepo = uRepo;
	}

	public List<User>getAllUsers(){
		return uRepo.findAll();
		
	}
	
	public void updateUser(User user) {
		uRepo.save(user);
	}
	
	public void insertUser(User user) {
		uRepo.save(user);
	}
	
	public User getUserByUsername(String username) {
		return uRepo.findByUsername(username);
	}
	 
	public User getUserByUserPassword(String password) {
		return uRepo.findByPassword(password);
	}
	
	public User getUserByUsernameAndPassword(String username, String password) {
		return uRepo.findByUsernameAndPassword(username, password);
	}
	    
	public User getUserByUserFirstname(String firstname) {
		return uRepo.findByFirstname(firstname);
	}
	    
	public User getUserByUserLastname(String lastname) {
		return uRepo.findByLastname(lastname);
	}
	    
	public User getUserByEmail(String email) {
		return uRepo.findByEmail(email);
	}
	    
	public User getUserByRoleId(String roleid) {
		return uRepo.findByRoleid(roleid);
	}
	    
	public void deleteUser(User user) {
		uRepo.delete(user);
	}
	
}

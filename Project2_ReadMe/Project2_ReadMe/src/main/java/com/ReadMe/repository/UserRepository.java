package com.ReadMe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ReadMe.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, String>{
	
	public List<User> findAll();
	public User findByUsername(String username);
	public User findByPassword(String password);
	public User findByUsernameAndPassword(String userame, String password);
	public User findByFirstname(String firstname);
	public User findByLastname(String lastname);
	public User findByEmail(String email);
	public User findByRoleid(String roleid);
       
}
package com.ers.eval;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ers.dao.RequestDaoImpl;
import com.ers.dao.UserDaoImpl;
import com.ers.model.Request;
import com.ers.model.User;
import com.ers.service.Service;

@TestInstance(Lifecycle.PER_CLASS)
public class ERSTest {
	
	@Mock
	private UserDaoImpl uDao;
	private RequestDaoImpl rDao;
	private Service testServ;
	private User testUser, testManager;
	private Request testRequest1;
	private List<User> allUsers;
	
	@BeforeAll
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testServ = new Service(uDao, rDao);
		testUser = new User(123, "user123", "password", "Test", "User", "testuser@email.com", 1);
		testManager = new User(1, "manager123", "password", "Test", "Manager", "testmanager@email.com", 0);
		testRequest1 = new Request(123.45, null, null, "no description", null, testUser.getUserId(), testManager.getUserId(), 1, 1);
		
		allUsers = new ArrayList<User>();
		allUsers.add(testUser);
		allUsers.add(testManager);
		
		when(uDao.getUserByUsername("user123")).thenReturn(testUser);
		when(uDao.getUserByUsername("manager123")).thenReturn(testManager);
		when(uDao.getUserRole(0)).thenReturn("MANAGER");
		when(uDao.getUserRole(1)).thenReturn("EMPLOYEE");
		when(uDao.getLegalNameById(testUser.getUserId())).thenReturn(testUser.getFirstname() + " " + testUser.getLastname());
		when(uDao.getLegalNameById(testManager.getUserId())).thenReturn(testManager.getFirstname() + " " + testManager.getLastname());
		when(testServ.getAllUsers()).thenReturn(allUsers);
		when(testServ.getAuthor(testRequest1.getAuthorId())).thenReturn(testUser.getFirstname() + " " + testUser.getLastname());
		when(testServ.getResolver(testRequest1.getResolverId())).thenReturn(testManager.getFirstname() + " " + testManager.getLastname());
	}
	
	@Test // login success test
	public void testGetUserSuccess() {
		assertEquals(testServ.login("user123", "password"), testUser);	
	}
	
	@Test // login failure test
	public void testGetUserFailure() {
		assertEquals(testServ.login("lkjhgfdsa", "asdfghjkl"), null);	
	}
	
	@Test
	public void testGetUserRole() {
		assertEquals(uDao.getUserRole(testUser.getRoleId()), "EMPLOYEE");
		assertEquals(uDao.getUserRole(testManager.getRoleId()), "MANAGER");
	}
	
	@Test
	public void testGetUserIdentity() {
		String testUserIdentity = testUser.getFirstname() + " " + testUser.getLastname();
		assertEquals(uDao.getLegalNameById(testUser.getUserId()), testUserIdentity);
		String testManagerIdentity = testManager.getFirstname() + " " + testManager.getLastname();
		assertEquals(uDao.getLegalNameById(testManager.getUserId()), testManagerIdentity);
	}
	
	@Test
	public void testGetUsers() {
		assertNotNull(testServ.getAllUsers());
	}
	
	@Test
	public void testGetRequestInfo() {
		String author = testUser.getFirstname() + " " + testUser.getLastname();
		String resolver = testManager.getFirstname() + " " + testManager.getLastname();
		assertEquals(testServ.getAuthor(testRequest1.getAuthorId()), author);
		assertEquals(testServ.getResolver(testRequest1.getResolverId()), resolver);
	}
}

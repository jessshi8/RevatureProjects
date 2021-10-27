package com.bankapp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bankapp.model.User;

public class UserDaoImpl implements GenericDao<User> {
	private BankAppDBConnection bankCon;
	private static final Logger log = Logger.getLogger(UserDaoImpl.class);
	
	public UserDaoImpl() {}
	
	public UserDaoImpl(BankAppDBConnection bankCon) {
		this.bankCon = bankCon;
	}

	@Override
	public List<User> getAll() {
		List<User> userList = new ArrayList<User>();
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "select * from users order by username asc";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				userList.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), 
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
			}
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return userList;
	}

	@Override
	public void update(User entity) {
		// TODO Auto-generated method stub
	}

	@Override
	public void insert(User entity) {
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "{? = call insert_user(?, ?, ?, ?, ?, ?, ?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, entity.getUsername());
			cs.setString(3, entity.getPassword());
			cs.setString(4, entity.getUserType());
			cs.setString(5, entity.getFirstName());
			cs.setString(6, entity.getLastName());
			cs.setString(7, entity.getDob());
			cs.setString(8, entity.getPhoneNumber());
			cs.execute();
			log.info(cs.getString(1));
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}	
	}

	@Override
	public void delete(User entity) {
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "delete from users where username=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, entity.getUsername());
			ps.execute();
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}	
	}
	
	// Deletes from junction table when a user is removed
	public void deleteJunction(String username) {
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "delete from user_account_xref where username=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.execute();
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}
	
	// Retrieves user details of specified username
	public User getPersonalInfo(String username) {
		String password = "", type = "", firstName = "", 
				lastName = "", dob = "", phoneNumber = "";
		
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "select * from users where username=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				password = rs.getString(2);
				type = rs.getString(3);
				firstName = rs.getString(4);
				lastName = rs.getString(5);
				dob = rs.getString(6);
				phoneNumber = rs.getString(7);
			}
			return new User(username, password, type, firstName, lastName, dob, phoneNumber);
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return null;
	}
}

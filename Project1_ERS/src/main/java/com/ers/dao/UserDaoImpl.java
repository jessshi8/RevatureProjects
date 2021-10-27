package com.ers.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ers.model.User;

public class UserDaoImpl implements GenericDao<User> {
	private DBConnection dbCon;
	private static final Logger log = Logger.getLogger(UserDaoImpl.class);
	
	public UserDaoImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public UserDaoImpl(DBConnection dbCon) {
		this.dbCon = dbCon;
	}
	
	@Override
	public List<User> getAll() {
		List<User> userList = new ArrayList<User>();

		try (Connection con = dbCon.getDBConnection()) {
			String sql = "select * from users order by userid asc";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int userId = rs.getInt(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String firstname = rs.getString(4);
				String lastname = rs.getString(5);
				String email = rs.getString(6);
				int roleId = rs.getInt(7);
				userList.add(new User(userId, username, password, firstname, lastname, email, roleId));
			}
			return userList;
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return null;
	}

	@Override
	public void update(User entity) {
		try (Connection con = dbCon.getDBConnection()) {
			String sql="update users set username=?, password=?, firstname=?, lastname=?, email=? where userid=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, entity.getUsername());
			ps.setString(2, entity.getPassword());
			ps.setString(3, entity.getFirstname());
			ps.setString(4, entity.getLastname());
			ps.setString(5, entity.getEmail());
			ps.setInt(6, entity.getUserId());
			ps.execute();
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}

	@Override
	public void insert(User entity) {
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "{? = call insert_user(?, ?, ?, ?, ?, ?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, entity.getUsername());	// username
			cs.setString(3, entity.getPassword());	// password
			cs.setString(4, entity.getFirstname());	// firstname
			cs.setString(5, entity.getLastname());	// lastname
			cs.setString(6, entity.getEmail());		// email
			cs.setInt(7, entity.getRoleId());		// roleId
			cs.execute();
			log.info(cs.getString(1));
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}

	@Override
	public void delete(User entity) {
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "{? = call delete_user(?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setInt(2, entity.getUserId());		
			cs.execute();
			log.info(cs.getString(1));
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}
	
	public User getUserByUsername(String username) {
		UserDaoImpl uDao = new UserDaoImpl(dbCon);
		for (User u : uDao.getAll()) {
			if (u.getUsername().equals(username)) {
				return u;
			}
		}
		return null;
	}
	
	public String getUserRole(int id) {
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "{? = call get_user_role(?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setInt(2, id);		
			cs.execute();
			return cs.getString(1);
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return null;
	}
	
	public String getLegalNameById(int id) {
		String name = "";
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "select firstname, lastname from users where userid=" + id;
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				name = rs.getString(1) + " " + rs.getString(2);
			}
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return name;
	}
}

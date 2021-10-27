package com.ers.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ers.model.Request;

public class RequestDaoImpl implements GenericDao<Request> {
	private DBConnection dbCon;
	private static final Logger log = Logger.getLogger(RequestDaoImpl.class);

	public RequestDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	public RequestDaoImpl(DBConnection dbCon) {
		this.dbCon = dbCon;
	}

	@Override
	public List<Request> getAll() {
		List<Request> requestList = new ArrayList<>();
	
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "select * from requests order by requestid asc";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int requestId = rs.getInt(1);
				double amount = rs.getDouble(2);
				Timestamp submitted = rs.getTimestamp(3);
				Timestamp resolved = rs.getTimestamp(4);
				String description = rs.getString(5);
				byte[] receipt = rs.getBytes(6);
				int authorId = rs.getInt(7);
				int resolverId = rs.getInt(8);
				int statusId = rs.getInt(9);
				int typeId = rs.getInt(10);
				requestList.add(new Request(requestId, amount, submitted, resolved, 
						description, receipt, authorId, resolverId, statusId, typeId));
			}
			return requestList;
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return null;
	}

	@Override
	public void update(Request entity) {
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "update requests set resolved=?, resolverid=?, statusid=? where requestid=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setTimestamp(1, entity.getResolved());
			ps.setInt(2, entity.getResolverId());
			ps.setInt(3, entity.getStatusid());
			ps.setInt(4, entity.getRequestid());
			ps.execute();
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}

	@Override
	public void insert(Request entity) {
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "{? = call insert_request(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setDouble(2, entity.getAmount()); 		// amount
			cs.setTimestamp(3, entity.getSubmitted());	// submitted
			cs.setTimestamp(4, entity.getResolved());	// resolved
			cs.setString(5, entity.getDescription());	// description
			cs.setBytes(6, entity.getReceipt());		// receipt
			cs.setInt(7, entity.getAuthorId()); 		// authorId
			cs.setInt(8, entity.getResolverId()); 		// resolverId
			cs.setInt(9, entity.getStatusid());			// statusId
			cs.setInt(10, entity.getTypeid());			// typeId
			cs.execute();
			log.info(cs.getString(1));
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}

	@Override
	public void delete(Request entity) {
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "{? = call delete_request(?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setInt(2, entity.getRequestid());		
			cs.execute();
			log.info(cs.getString(1));
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}
	
	// Returns the status of the request
	public String getRequestStatus(int id) {
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "{? = call get_request_status(?)}";
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
	
	// Returns the type of the request
	public String getRequestType(int id) {
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "{? = call get_request_type(?)}";
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
	
	// Retrieves the request ids from a specific user
	public List<Integer> getUserRequestIds(String username) {
		List<Integer> idList = new ArrayList<>();
		
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "select requestid from user_request_xref where username=? order by requestid asc";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				idList.add(new Integer(rs.getInt(1)));
			}
			return idList;
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return null;
	}
	
	// Retrieves the request by id
	public Request getRequestById(int id) {
		Request r = null;
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "select * from requests where requestid=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				r = new Request(rs.getInt(1), rs.getDouble(2), rs.getTimestamp(3), 
						rs.getTimestamp(4), rs.getString(5), rs.getBytes(6), rs.getInt(7), 
						rs.getInt(8), rs.getInt(9), rs.getInt(10));
			}
			return r;
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return null;
	}
	
	// Retrieves the requests by status
	public List<Request> filterRequests(int id) {
		List<Request> rList = new ArrayList<>();
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "select * from requests where statusid=? order by requestid asc";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rList.add(new Request(rs.getInt(1), rs.getDouble(2), rs.getTimestamp(3), 
						rs.getTimestamp(4), rs.getString(5), rs.getBytes(6), rs.getInt(7), 
						rs.getInt(8), rs.getInt(9), rs.getInt(10)));
			}
			return rList;
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return null;
	}
	
	// Retrieves the most recently added request id
	public int getNewRequestId() {
		int id = 0;
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "select max(requestid) from requests";
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				id = rs.getInt(1);
			}
			return id;
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return id;
	}
	
	// Updates junction table when a request is created under a specific user
	public void insertJunction(String username, int id) {
		try (Connection con = dbCon.getDBConnection()) {
			String sql = "{? = call add_relationship(?, ?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, username);
			cs.setInt(3, id);
			cs.execute();
			log.info(cs.getString(1));
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}
}

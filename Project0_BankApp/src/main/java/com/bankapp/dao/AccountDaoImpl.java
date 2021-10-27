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
import com.bankapp.model.Account;

public class AccountDaoImpl implements GenericDao<Account> {
	private BankAppDBConnection bankCon;
	private static final Logger log = Logger.getLogger(AccountDaoImpl.class);
	
	public AccountDaoImpl() {}
	
	public AccountDaoImpl(BankAppDBConnection bankCon) {
		this.bankCon = bankCon;
	}
	
	@Override
	public List<Account> getAll() {
		List<Account> accountList = new ArrayList<Account>();
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "select * from accounts order by accountid asc";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				accountList.add(new Account(rs.getInt(1), rs.getString(2), rs.getBoolean(3), rs.getDouble(4)));
			}
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return accountList;
	}
	
	@Override
	public void update(Account entity) {
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "update accounts set accounttype=?, status=?, balance=? where accountid=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, entity.getAccountType());
			ps.setBoolean(2, entity.getStatus());
			ps.setDouble(3, entity.getBalance());
			ps.setInt(4, entity.getAccountid());
			ps.execute();
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}
	
	@Override
	public void insert(Account entity) {
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "{? = call insert_account(?, ?, ?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, entity.getAccountType());
			cs.setBoolean(3, entity.getStatus());
			cs.setDouble(4, entity.getBalance());
			cs.execute();
			log.info(cs.getString(1));
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}
	
	@Override
	public void delete(Account entity) {
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "delete from accounts where accountid=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, entity.getAccountid());
			ps.execute();
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}
	
	// Retrieves account details of specified account
	public Account getAccountDetails(int id) {
		String type = null;
		boolean status = false;
		double balance = 0;
		
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "select * from accounts where accountid=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				type = rs.getString(2);
				status = rs.getBoolean(3);
				balance = rs.getDouble(4);
			}
			return new Account(id, type, status, balance);
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return null;
	}
	
	// Retrieves account id of the most recently created account
	public int getAccountid() {
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "{? = call get_account_id()}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.execute();
			return cs.getInt(1);
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return 0;
	}
	
	// Retrieves account ids for specific user
	public List<Integer> getUserAccountIds(String username) {
		List<Integer> ids = new ArrayList<>();
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "select a.accountid from users u left outer join user_account_xref x on u.username = x.username \r\n"
					+ "left outer join accounts a on x.accountid = a.accountid where u.username = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return ids;
	}
	
	// Retrieves ids of pending accounts
	public List<Integer> getPendingAccounts() {
		List<Integer> ids = new ArrayList<>();
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "select accountid from accounts where status=false";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return ids;
	}
	
	// Retrieves ids of approves accounts
	public List<Integer> getApprovedAccounts() {
		List<Integer> ids = new ArrayList<>();
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "select accountid from accounts where status=true";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return ids;
	}
	
	// Retrieves account statuses for specific user
	public List<Boolean> getUserAccountStatuses(String username) {
		List<Boolean> statuses = new ArrayList<>();
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "select a.status from users u left outer join user_account_xref x on u.username = x.username \r\n"
					+ "left outer join accounts a on x.accountid = a.accountid where u.username = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				statuses.add(rs.getBoolean(1));
			}
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return statuses;
	}

	// Updates junction table when an account is created under a specific user
	public void insertJunction(String username, int id) {
		try(Connection con = bankCon.getDBConnection()){	
			String sql = "insert into user_account_xref values(?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.setInt(2, id);
			ps.execute();
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}
	
	// Deletes from junction table when an account is removed
	public void deleteJunction(int id) {
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "delete from user_account_xref where accountid=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.execute();
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
	}
	
	// Retrieves owners of account
	public List<String> getOwners(int id) {
		List<String> owners = new ArrayList<>();
		try (Connection con = bankCon.getDBConnection()) {
			String sql = "select username from user_account_xref where accountid=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				owners.add(rs.getString(1));
			}
			return owners;
		} catch (SQLException e) {
			log.fatal("A SQLException occurred.");
		}
		return null;
	}
}

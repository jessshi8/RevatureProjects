package com.bankapp.model;

public class Account {
	private int accountid;
	private String accountType;
	private boolean status;
	private double balance;
	
	public Account() {}

	public Account(int accountid, String accountType, boolean status, double balance) {
		this.accountid = accountid;
		this.accountType = accountType;
		this.status = status;
		this.balance = balance;
	}

	public Account(String accountType, boolean status, double balance) {
		this.accountType = accountType;
		this.status = status;
		this.balance = balance;
	}
	
	// Getter and setter methods
	public int getAccountid() {
		return accountid;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "Account [ID = " + accountid + ", Type = "
				+ accountType + ", Status = " + status + ", Balance = " + balance + "]";
	}
}

package com.bankapp.model;

public class User {
	private String username, password, userType, firstName, lastName, dob,
			phoneNumber;
	
	public User() {
	}

	public User(String username, String password, String userType,
			String firstName, String lastName, String dob, String phoneNumber) {
		this.username = username;
		this.password = password;
		this.userType = userType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.phoneNumber = phoneNumber;
	}

	// Getter and setter methods
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password
				+ ", userType=" + userType + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", dob=" + dob + ", phoneNumber="
				+ phoneNumber + "]";
	}
}

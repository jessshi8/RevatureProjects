package com.ers.model;

import java.sql.Timestamp;
import java.util.Arrays;

public class Request {
	private int requestId;
	private double amount;
	private Timestamp submitted, resolved;
	private String description;
	private byte[] receipt;
	private int authorId, resolverId, statusId, typeId;
	
	public Request() {
		// TODO Auto-generated constructor stub
	}

	public Request(int requestId, double amount, Timestamp submitted, Timestamp resolved,
			String description, byte[] receipt, int authorId, int resolverId, int statusId,
			int typeId) {
		super();
		this.requestId = requestId;
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.receipt = receipt;
		this.authorId = authorId;
		this.resolverId = resolverId;
		this.statusId = statusId;
		this.typeId = typeId;
	}

	public Request(double amount, Timestamp submitted, Timestamp resolved, String description, 
			byte[] receipt, int authorId, int resolverId, int statusId, int typeId) {
		super();
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.receipt = receipt;
		this.authorId = authorId;
		this.resolverId = resolverId;
		this.statusId = statusId;
		this.typeId = typeId;
	}

	public int getRequestid() {
		return requestId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Timestamp submitted) {
		this.submitted = submitted;
	}

	public Timestamp getResolved() {
		return resolved;
	}

	public void setResolved(Timestamp resolved) {
		this.resolved = resolved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getReceipt() {
		return receipt;
	}

	public void setReceipt(byte[] receipt) {
		this.receipt = receipt;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getResolverId() {
		return resolverId;
	}

	public void setResolverId(int resolverId) {
		this.resolverId = resolverId;
	}

	public int getStatusid() {
		return statusId;
	}

	public void setStatusid(int statusId) {
		this.statusId = statusId;
	}

	public int getTypeid() {
		return typeId;
	}

	public void setTypeid(int typeId) {
		this.typeId = typeId;
	}

	@Override
	public String toString() {
		return "Request [requestId=" + requestId + ", amount=" + amount + ", submitted=" + submitted
				+ ", resolved=" + resolved + ", description=" + description + ", receipt="
				+ Arrays.toString(receipt) + ", authorId=" + authorId + ", resolverId=" + resolverId
				+ ", statusId=" + statusId + ", typeId=" + typeId + "]";
	}
}

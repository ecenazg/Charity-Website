package com.website.eocs.entity;

import java.sql.Date;

public class Donation {
	private int id;
	private int amount;
	private String message;
	private Date createdDate;
	private String accountName;
	private String fundName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public Donation(int id, int amount, String message, Date createdDate, String accountName, String fundName) {
		super();
		this.id = id;
		this.amount = amount;
		this.message = message;
		this.createdDate = createdDate;
		this.accountName = accountName;
		this.fundName = fundName;
	}
	public Donation() {
		super();
	}
	
	
}



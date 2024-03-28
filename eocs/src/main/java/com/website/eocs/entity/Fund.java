package com.website.eocs.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "fund", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class Fund {
	public static final int MAX_AMOUNT = 999999999;
	@Id
	private int id;

	@Column(name = "name", unique = true)
	@Size(min = 10, max = 200, message = "The fund name has a length of 10 to 200 characters")
	private String name;


	@Size(max = 500, message = "The description has a maximum length of 500 characters")
	private String description;
	private String content;

	@Size(max = 200, message = "The image path has a maximum length of 200 characters")
	private String imgUrl;
	private String status;
	private Date createdDate;
	private Date endDate;
	private String categoryName;
	private String foundationName;

	@Min(value = 1, message = "Do not leave this field empty and the value > 0")
	@Max(value = Fund.MAX_AMOUNT, message = "The amount exceeds the limit")
	@Digits(integer = 10, fraction = 0, message = "Please enter a valid number.")
	private Integer expectedAmount;
	
	private Integer currentAmount;

	public Fund() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getFoundationName() {
		return foundationName;
	}

	public void setFoundationName(String foundationName) {
		this.foundationName = foundationName;
	}

	public Integer getExpectedAmount() {
		return expectedAmount;
	}

	public void setExpectedAmount(Integer expectedAmount) {
		this.expectedAmount = expectedAmount;
	}
	

	public Integer getCurrentAmount() {
		if(currentAmount == null) {
			currentAmount = 0;
		}
		return currentAmount;
	}

	public void setCurrentAmount(Integer currentAmount) {
		this.currentAmount = currentAmount;
	}

	public Fund(int id, String name, String description, String content, String imgUrl, String status, Date createdDate,
			Date endDate, String categoryName, String foundationName, int expectedAmount) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.content = content;
		this.imgUrl = imgUrl;
		this.status = status;
		this.createdDate = createdDate;
		this.endDate = endDate;
		this.categoryName = categoryName;
		this.foundationName = foundationName;
		this.expectedAmount = expectedAmount;
	}

	
	
	

}

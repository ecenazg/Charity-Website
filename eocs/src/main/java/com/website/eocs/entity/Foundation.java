package com.website.eocs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "foundation",  uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Foundation {
	@Id
	private int id;
	
	@Column(name = "name", unique = true)
	@Size(min = 5, max = 200, message = "The fund name has a length of 5 to 200 characters")
	private String name;
	

	@Size(max = 500, message = "The description has a maximum length of 500 characters")
	private String description;
	

	@Size(max = 100, message = "Characters exceeding the allowed number")
	@Email(message = "Invalid Email")
	private String email;
	private String status;
	
	public Foundation() {
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}

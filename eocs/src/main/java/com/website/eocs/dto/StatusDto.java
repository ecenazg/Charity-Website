package com.website.eocs.dto;

public class StatusDto {
	private String name;
	
	public StatusDto(String name) {
		super();
		this.name = name;
	}

	public StatusDto() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}

package com.website.eocs.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MapperDonation implements RowMapper<Donation> {
	@Override
	public Donation mapRow(ResultSet rs, int rowNum) throws SQLException {
		Donation donation = new Donation();
		donation.setId(rs.getInt("id"));
		donation.setAmount(rs.getInt("amount"));
		donation.setMessage(rs.getString("message"));
		donation.setCreatedDate(rs.getDate("created_date"));
		donation.setAccountName(rs.getString("username"));
		donation.setFundName(rs.getString("name"));
		return donation;
	}
}

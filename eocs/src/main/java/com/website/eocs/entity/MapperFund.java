package com.website.eocs.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MapperFund implements RowMapper<Fund>{

	@Override
	public Fund mapRow(ResultSet rs, int rowNum) throws SQLException {
		Fund fund = new Fund();
		
		fund.setId(rs.getInt("id"));
		fund.setName(rs.getString("name"));
		fund.setDescription(rs.getString("description"));
		fund.setContent(rs.getString("content"));
		fund.setImgUrl(rs.getString("img_url"));
		fund.setCreatedDate(rs.getDate("created_date"));
		fund.setEndDate(rs.getDate("end_date"));
		fund.setCategoryName(rs.getString("category_name"));
		fund.setFoundationName(rs.getString("found_name"));
		fund.setStatus(rs.getString("status"));
		fund.setExpectedAmount(rs.getInt("expected_amount"));
		return fund;
	}
}

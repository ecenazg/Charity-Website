package com.website.eocs.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MapperFoundation implements RowMapper<Foundation> {
	@Override
	public Foundation mapRow(ResultSet rs, int rowNum) throws SQLException {
		Foundation foundation = new Foundation();
		foundation.setId(rs.getInt("id"));
		foundation.setName(rs.getString("name"));
		foundation.setDescription(rs.getString("description"));
		foundation.setEmail(rs.getString("email"));
		foundation.setStatus(rs.getString("status"));
		return foundation;
	}
}

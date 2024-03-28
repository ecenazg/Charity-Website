package com.website.eocs.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MapperAccount implements RowMapper<Account>{

	@Override
	public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
		Account account = new Account();
		
		account.setId(rs.getInt("id"));
		account.setUsername(rs.getString("username"));
		account.setEmail(rs.getString("email"));
		account.setPassword(rs.getString("password"));
		account.setPhone(rs.getString("phone"));
		account.setAddress(rs.getString("address"));
		account.setRole(rs.getInt("role"));
		account.setStatus(rs.getString("status"));
		return account;
	}
}

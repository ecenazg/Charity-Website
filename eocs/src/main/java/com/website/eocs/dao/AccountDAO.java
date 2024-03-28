package com.website.eocs.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.website.eocs.entity.Account;
import com.website.eocs.entity.MapperAccount;

@Repository
public class AccountDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Account> getDataAccount() {
		List<Account> Account = new ArrayList<Account>();
		String sql = "select * from Account order by id;";
		Account = jdbcTemplate.query(sql, new MapperAccount());
		return Account;
	}

	public List<Account> getDataAccountPagination(int start, int end) {
		List<Account> Account = new ArrayList<Account>();
		String sql = "select * from Account ORDER BY Account.id limit " + start + ", " + (end - start);
		Account = jdbcTemplate.query(sql, new MapperAccount());
		return Account;
	}

	public List<Account> searchAccount(String username, String phone) {
		List<Account> searchAccount = new ArrayList<Account>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from Account\r\n" + "where 1 = 1");

		if (username != null && !username.isEmpty()) {
			sqlBuilder.append(" and username like " + "'%" + username + "%'");
		}

		if (phone != null && !phone.isEmpty()) {
			sqlBuilder.append(" and phone like " + "'%" + phone + "%'");
		}

		String sql = sqlBuilder.toString();
		searchAccount = jdbcTemplate.query(sql, new MapperAccount());
		return searchAccount;
	}

	public List<Account> searchAccountPagination(String username, String phone, int start, int end) {
		List<Account> searchAccount = new ArrayList<Account>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from Account\r\n" + "where 1 = 1");

		if (username != null && !username.isEmpty()) {
			sqlBuilder.append(" and username like " + "'%" + username + "%'");
		}

		if (phone != null && !phone.isEmpty()) {
			sqlBuilder.append(" and phone like " + "'%" + phone + "%'");
		}

		sqlBuilder.append(" limit " + start + ", " + (end - start));
		String sql = sqlBuilder.toString();
		searchAccount = jdbcTemplate.query(sql, new MapperAccount());
		return searchAccount;
	}

	public void deleteAccount(int id) {
		String sql = "update account set status = 'Inactive' where id = " + id;
		jdbcTemplate.update(sql);
	}

	public void unDeleteAccount(int id) {
		String sql = "update account set status = 'Active' where id = " + id;
		jdbcTemplate.update(sql);
	}

	@Transactional
	public void deleteMultipleAccount(List<Integer> ids) {
		for (Integer id : ids) {
			try {
				jdbcTemplate.update("update account set status = 'Inactive' where id = ?", id);
			} catch (DataAccessException e) {
				throw new RuntimeException("Failed to delete element with id " + id, e);
			}
		}
	}

	public Account getDataAccountById(int id) {
		String sql = "select * from account where id = " + id;
		Account account = jdbcTemplate.queryForObject(sql, new MapperAccount());
		return account;
	}

	public void updateAccountPassword(int id, String password) {
		String sql = "update account set password = ? where id = ?";
		jdbcTemplate.update(sql, password, id);
	}

	public void updateAccount(Account account) {
		String sql = "update account\r\n"
				+ "set account.username = ?, account.phone = ? ,account.address = ?, account.role = ?, account.status = ?\r\n"
				+ "where account.id = ?;";
		jdbcTemplate.update(sql, account.getUsername(), account.getPhone(), account.getAddress(), account.getRole(),
				account.getStatus(), account.getId());
	}

	public void insertAccount(Account account) {
		String sql = "INSERT INTO `Account` (`id`, `username`,`email`,`password`,`phone`, `address`, `role`, `status`) VALUES\r\n"
				+ "(null, ?, ?, ?, ?, ?, ?, ?);";
		jdbcTemplate.update(sql, account.getUsername(), account.getEmail(), account.getPassword(), account.getPhone(),
				account.getAddress(), account.getRole(), account.getStatus());
	}

	public boolean isUniqueAccount(String email, int id) {
		String sql = "SELECT COUNT(*) FROM account WHERE email = ? AND id <> ?";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { email, id }, Integer.class);
		return count == 0;
	}

	public boolean isUniqueAccount(String email) {
		String sql = "SELECT COUNT(*) FROM account WHERE email = ? ";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { email}, Integer.class);
		return count == 0;
	}

	public Account getDataAccountByEmail(String email) {
		String sql = "select * from account where email = '" + email + "'";
		Account account = jdbcTemplate.queryForObject(sql, new MapperAccount());
		return account;
	}
	
	public void updateUser(Account user) {
		String sql = "update account\r\n"
				+ "set account.username = ?, account.phone = ? ,account.address = ? where account.id = ?;";
		jdbcTemplate.update(sql, user.getUsername(), user.getPhone(), user.getAddress(), user.getId());
	}
}

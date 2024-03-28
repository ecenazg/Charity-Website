package com.website.eocs.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.website.eocs.entity.Donation;
import com.website.eocs.entity.MapperDonation;

@Repository
public class DonationDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Donation> getDataDonations() {
		List<Donation> donations = new ArrayList<Donation>();
		String sql = "select donation.id, donation.amount, donation.message, donation.created_date, account.username, fund.name from donation\r\n"
				+ "join account on donation.account_id = account.id\r\n" + "join fund on donation.fund_id = fund.id;";
		donations = jdbcTemplate.query(sql, new MapperDonation());
		return donations;
	}

	public List<Donation> getDataDonationsPagination(int start, int end) {
		List<Donation> donations = new ArrayList<Donation>();
		String sql = "select donation.id, donation.amount, donation.message, donation.created_date, account.username, fund.name from donation\r\n"
				+ "join account on donation.account_id = account.id\r\n"
				+ "join fund on donation.fund_id = fund.id ORDER BY donation.id limit " + start + ", " + (end - start);
		donations = jdbcTemplate.query(sql, new MapperDonation());
		return donations;
	}

	public List<Donation> searchDonations(int id, String accountName, String fundName) {
		List<Donation> searchDonations = new ArrayList<Donation>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				"select donation.id, donation.amount, donation.message, donation.created_date, account.username, fund.name from donation\r\n"
						+ "join account on donation.account_id = account.id\r\n"
						+ "join fund on donation.fund_id = fund.id\r\n" + "where 1 = 1");

		if (id >= 0) {
			sqlBuilder.append(" and donation.id = " + id);
		} else {
			sqlBuilder.append("");
		}

		if (accountName != null && !accountName.isEmpty()) {
			sqlBuilder.append(" and account.username like " + "'%" + accountName + "%'");
		}

		if (fundName != null && !fundName.isEmpty()) {
			sqlBuilder.append(" and fund.name like " + "'%" + fundName + "%'");
		}
		String sql = sqlBuilder.toString();
		searchDonations = jdbcTemplate.query(sql, new MapperDonation());
		return searchDonations;
	}

	public List<Donation> searchDonationsPagination(int id, String accountName, String fundName, int start, int end) {
		List<Donation> searchDonations = new ArrayList<Donation>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				"select donation.id, donation.amount, donation.message, donation.created_date, account.username, fund.name from donation\r\n"
						+ "join account on donation.account_id = account.id\r\n"
						+ "join fund on donation.fund_id = fund.id\r\n" + "where 1 = 1");

		if (id >= 0) {
			sqlBuilder.append(" and donation.id = " + id);
		} else {
			sqlBuilder.append("");
		}

		if (accountName != null && !accountName.isEmpty()) {
			sqlBuilder.append(" and account.username like " + "'%" + accountName + "%'");
		}

		if (fundName != null && !fundName.isEmpty()) {
			sqlBuilder.append(" and fund.name like " + "'%" + fundName + "%'");
		}
		sqlBuilder.append(" limit " + start + ", " + (end - start));
		String sql = sqlBuilder.toString();
		searchDonations = jdbcTemplate.query(sql, new MapperDonation());
		return searchDonations;
	}

	public Integer getCurrentMoneyByFund(int fundId) {
		String sql = "select sum(amount) from donation where fund_id = ?";
		Integer currentMoney = jdbcTemplate.queryForObject(sql, new Object[] { fundId }, Integer.class);
		return currentMoney;
	}

	public void createDonation(Donation donation, int accountId, int fundId) {
		String sql = "INSERT INTO `Donation` (`id`, `amount`,`message`, `created_date`, `account_id`, `fund_id`) VALUES\r\n"
				+ "(null, ?, ?, now(), ?, ?);";
		jdbcTemplate.update(sql, donation.getAmount(), donation.getMessage(), accountId, fundId);
	}

	public List<Donation> searchDonationByAccount(String accountName) {
		List<Donation> searchDonationByAccount = new ArrayList<Donation>();
		String sql = "select donation.id, donation.amount, donation.message, donation.created_date, account.username, fund.name from donation\r\n"
				+ "join account on donation.account_id = account.id\r\n" + "join fund on donation.fund_id = fund.id\r\n"
				+ "where account.username = '" + accountName + "'";

		searchDonationByAccount = jdbcTemplate.query(sql, new MapperDonation());
		return searchDonationByAccount;
	}
	
	public List<Donation> searchDonationByAccountPagination(String accountName, int start, int end) {
		List<Donation> searchDonationByAccount = new ArrayList<Donation>();
		String sql = "select donation.id, donation.amount, donation.message, donation.created_date, account.username, fund.name from donation\r\n"
				+ "join account on donation.account_id = account.id\r\n" + "join fund on donation.fund_id = fund.id\r\n"
				+ "where account.username = '" + accountName + "'  limit " + start + ", " + (end - start);

		searchDonationByAccount = jdbcTemplate.query(sql, new MapperDonation());
		return searchDonationByAccount;
	}
}

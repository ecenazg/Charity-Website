package com.website.eocs.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.website.eocs.entity.Fund;
import com.website.eocs.entity.MapperFund;

@Repository
public class FundDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Fund> getDataFund() {
		List<Fund> funds = new ArrayList<Fund>();
		String sql = "select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund\r\n"
				+ "join categories on fund.category_id = categories.id\r\n"
				+ "join foundation on fund.foundation_id = foundation.id order by fund.id";
		funds = jdbcTemplate.query(sql, new MapperFund());
		return funds;
	}

	public List<Fund> getDataFundsPagination(int start, int end) {
		List<Fund> funds = new ArrayList<Fund>();
		String sql = "select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund\r\n"
				+ "join categories on fund.category_id = categories.id\r\n"
				+ "join foundation on fund.foundation_id = foundation.id order by fund.id limit " + start + ", "
				+ (end - start);
		funds = jdbcTemplate.query(sql, new MapperFund());
		return funds;
	}

	public List<Fund> searchFunds(int id, String fundName, String categoryName) {
		List<Fund> searchFunds = new ArrayList<Fund>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				"select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund\r\n"
						+ "join categories on fund.category_id = categories.id\r\n"
						+ "join foundation on fund.foundation_id = foundation.id " + "where 1 = 1");

		if (id >= 0) {
			sqlBuilder.append(" and fund.id = " + id);
		} else {
			sqlBuilder.append("");
		}

		if (fundName != null && !fundName.isEmpty()) {
			sqlBuilder.append(" and fund.name like " + "'%" + fundName + "%'");
		}

		if (categoryName != null && !categoryName.isEmpty()) {
			sqlBuilder.append(" and categories.category_name like " + "'%" + categoryName + "%'");
		}
		sqlBuilder.append(" order by fund.id");
		String sql = sqlBuilder.toString();
		searchFunds = jdbcTemplate.query(sql, new MapperFund());
		return searchFunds;
	}

	public List<Fund> searchFundsPagination(int id, String fundName, String categoryName, int start, int end) {
		List<Fund> searchFunds = new ArrayList<Fund>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				"select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund\r\n"
						+ "join categories on fund.category_id = categories.id\r\n"
						+ "join foundation on fund.foundation_id = foundation.id " + "where 1 = 1");

		if (id >= 0) {
			sqlBuilder.append(" and fund.id = " + id);
		} else {
			sqlBuilder.append("");
		}

		if (fundName != null && !fundName.isEmpty()) {
			sqlBuilder.append(" and fund.name like " + "'%" + fundName + "%'");
		}

		if (categoryName != null && !categoryName.isEmpty()) {
			sqlBuilder.append(" and categories.category_name like " + "'%" + categoryName + "%'");
		}
		sqlBuilder.append(" order by fund.id");
		sqlBuilder.append(" limit " + start + ", " + (end - start));
		String sql = sqlBuilder.toString();
		searchFunds = jdbcTemplate.query(sql, new MapperFund());
		return searchFunds;
	}

	public void deleteFund(int id) {
		String sql = "update fund set status = 'Inactive' where id = " + id;
		jdbcTemplate.update(sql);
	}

	public void unDeleteFund(int id) {
		String sql = "update fund set status = 'Active' where id = " + id;
		jdbcTemplate.update(sql);
	}

	@Transactional
	public void deleteMultipleFunds(List<Integer> ids) {
		for (Integer id : ids) {
			try {
				jdbcTemplate.update("update fund set status = 'Inactive' where id = ?", id);
			} catch (DataAccessException e) {
				throw new RuntimeException("Failed to delete element with id " + id, e);
			}
		}
	}

	public Fund getDataFundById(int id) {
		String sql = "select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund\r\n"
				+ "join categories on fund.category_id = categories.id\r\n"
				+ "join foundation on fund.foundation_id = foundation.id where fund.id = " + id;
		Fund fund = jdbcTemplate.queryForObject(sql, new MapperFund());
		return fund;
	}

	public void updateFund(Fund fund, int categoryId, int foundationId) {
		String sql = "update fund\r\n"
				+ "set fund.name = ?, fund.description = ?, fund.content = ?, fund.img_url = ?, \r\n"
				+ "fund.end_date = ?, fund.status = ?, fund.expected_amount = ?, fund.category_id = ?, fund.foundation_id = ?\r\n"
				+ "where fund.id = ?;";
		jdbcTemplate.update(sql, fund.getName(), fund.getDescription(), fund.getContent(), fund.getImgUrl(),
				fund.getEndDate(), fund.getStatus(), fund.getExpectedAmount(), categoryId, foundationId, fund.getId());
	}

	public void insertFund(Fund fund, int categoryId, int foundationId) {
		String sql = "INSERT INTO `Fund` (`id`, `name`,`description`, `content`, `img_url`, `created_date`, `end_date`, `category_id`, `foundation_id`, `status`, `expected_amount`) VALUES\r\n"
				+ "(null, ?, ?, ?, ?, now(), ?, ?, ?, ?, ?);";
		jdbcTemplate.update(sql, fund.getName(), fund.getDescription(), fund.getContent(), fund.getImgUrl(),
				fund.getEndDate(), categoryId, foundationId, fund.getStatus(), fund.getExpectedAmount());
	}

	public boolean isUniqueFund(String name, int id) {
		String sql = "SELECT COUNT(*) FROM fund WHERE name = ? AND id <> ?";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { name, id }, Integer.class);
		return count == 0;
	}

	public List<Fund> getDataFundActive() {
		List<Fund> funds = new ArrayList<Fund>();
		String sql = "select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund \r\n"
				+ "join categories on fund.category_id = categories.id\r\n"
				+ "join foundation on fund.foundation_id = foundation.id \r\n" + "where fund.status IN ('Active', 'Finish')\r\n"
				+ "order by fund.id";
		funds = jdbcTemplate.query(sql, new MapperFund());
		return funds;
	}

	public List<Fund> getDataFundsPaginationActive(int start, int end) {
		List<Fund> funds = new ArrayList<Fund>();
		String sql = "select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund \r\n"
				+ "join categories on fund.category_id = categories.id\r\n"
				+ "join foundation on fund.foundation_id = foundation.id \r\n" + "where fund.status IN ('Active', 'Finish')\r\n"
				+ "order by fund.id limit " + start + ", " + (end - start);
		funds = jdbcTemplate.query(sql, new MapperFund());
		return funds;
	}

	public List<Fund> getDataFundByCategoriesActive(int categoryId) {
		List<Fund> funds = new ArrayList<Fund>();
		String sql = "select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund \r\n"
				+ "join categories on fund.category_id = categories.id\r\n"
				+ "join foundation on fund.foundation_id = foundation.id \r\n"
				+ "where fund.status IN ('Active', 'Finish') and category_id = " + categoryId + " " + "order by fund.id";
		funds = jdbcTemplate.query(sql, new MapperFund());
		return funds;
	}

	public List<Fund> getDataFundByFoundationActive(int foundationId) {
		List<Fund> funds = new ArrayList<Fund>();
		String sql = "select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund \r\n"
				+ "join categories on fund.category_id = categories.id\r\n"
				+ "join foundation on fund.foundation_id = foundation.id \r\n"
				+ "where fund.status IN ('Active', 'Finish') and foundation_id = " + foundationId + " " + "order by fund.id";
		funds = jdbcTemplate.query(sql, new MapperFund());
		return funds;
	}
	
	public List<Fund> userSearchFunds(String fundName, int categoryId, int foundationId) {
		List<Fund> searchFunds = new ArrayList<Fund>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				"select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund\r\n"
						+ "join categories on fund.category_id = categories.id\r\n"
						+ "join foundation on fund.foundation_id = foundation.id " + "where fund.status IN ('Active', 'Finish')");

		if (fundName != null && !fundName.isEmpty()) {
			sqlBuilder.append(" and fund.name like " + "'%" + fundName + "%'");
		}
		
		if (categoryId > 0) {
			sqlBuilder.append(" and fund.category_id = " + categoryId);
		} else {
			sqlBuilder.append("");
		}

		if (foundationId > 0) {
			sqlBuilder.append(" and fund.foundation_id = " + foundationId);
		} else {
			sqlBuilder.append("");
		}
		sqlBuilder.append(" order by fund.id");
		String sql = sqlBuilder.toString();
		searchFunds = jdbcTemplate.query(sql, new MapperFund());
		return searchFunds;
	}

	public List<Fund> userSearchFundsPagination(String fundName, int categoryId, int foundationId, int start, int end) {
		List<Fund> searchFunds = new ArrayList<Fund>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				"select fund.id, fund.name, fund.description, fund.content, fund.img_url, fund.created_date, fund.end_date, categories.category_name, foundation.name as found_name, fund.status, fund.expected_amount from fund\r\n"
						+ "join categories on fund.category_id = categories.id\r\n"
						+ "join foundation on fund.foundation_id = foundation.id " + "where fund.status IN ('Active', 'Finish')");

		if (fundName != null && !fundName.isEmpty()) {
			sqlBuilder.append(" and fund.name like " + "'%" + fundName + "%'");
		}
		
		if (categoryId > 0) {
			sqlBuilder.append(" and fund.category_id = " + categoryId);
		} else {
			sqlBuilder.append("");
		}

		if (foundationId > 0) {
			sqlBuilder.append(" and fund.foundation_id = " + foundationId);
		} else {
			sqlBuilder.append("");
		}
		sqlBuilder.append(" order by fund.id");
		sqlBuilder.append(" limit " + start + ", " + (end - start));
		String sql = sqlBuilder.toString();
		searchFunds = jdbcTemplate.query(sql, new MapperFund());
		return searchFunds;
	}
	
	public void finishFund(int id) {
		String sql = "update fund set status = 'Finish' where id = " + id;
		jdbcTemplate.update(sql);
	}
}

package com.website.eocs.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.website.eocs.entity.Foundation;
import com.website.eocs.entity.MapperFoundation;

@Repository
public class FoundationDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Foundation> getDataFoundation() {
		List<Foundation> Foundation = new ArrayList<Foundation>();
		String sql = "select * from Foundation order by id;";
		Foundation = jdbcTemplate.query(sql, new MapperFoundation());
		return Foundation;
	}

	public List<Foundation> getDataFoundationPagination(int start, int end) {
		List<Foundation> Foundation = new ArrayList<Foundation>();
		String sql = "select * from Foundation ORDER BY Foundation.id limit " + start + ", " + (end - start);
		Foundation = jdbcTemplate.query(sql, new MapperFoundation());
		return Foundation;
	}

	public List<Foundation> searchFoundation(int id, String name) {
		List<Foundation> searchFoundation = new ArrayList<Foundation>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from Foundation\r\n" + "where 1 = 1");

		if (id >= 0) {
			sqlBuilder.append(" and id = " + id);
		} else {
			sqlBuilder.append("");
		}

		if (name != null && !name.isEmpty()) {
			sqlBuilder.append(" and name like " + "'%" + name + "%'");
		}

		String sql = sqlBuilder.toString();
		searchFoundation = jdbcTemplate.query(sql, new MapperFoundation());
		return searchFoundation;
	}

	public List<Foundation> searchFoundationPagination(int id, String name, int start, int end) {
		List<Foundation> searchFoundation = new ArrayList<Foundation>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from Foundation\r\n" + "where 1 = 1");

		if (id >= 0) {
			sqlBuilder.append(" and id = " + id);
		} else {
			sqlBuilder.append("");
		}

		if (name != null && !name.isEmpty()) {
			sqlBuilder.append(" and name like " + "'%" + name + "%'");
		}

		sqlBuilder.append(" limit " + start + ", " + (end - start));
		String sql = sqlBuilder.toString();
		searchFoundation = jdbcTemplate.query(sql, new MapperFoundation());
		return searchFoundation;
	}

	public void deleteFoundation(int id) {
		String sql = "update foundation set status = 'Inactive' where id = " + id;
		jdbcTemplate.update(sql);
	}

	public void unDeleteFoundation(int id) {
		String sql = "update foundation set status = 'Active' where id = " + id;
		jdbcTemplate.update(sql);
	}

	@Transactional
	public void deleteMultipleFoundation(List<Integer> ids) {
		for (Integer id : ids) {
			try {
				jdbcTemplate.update("update foundation set status = 'Inactive' where id = ?", id);
			} catch (DataAccessException e) {
				throw new RuntimeException("Failed to delete element with id " + id, e);
			}
		}
	}

	public Foundation getDataFoundationById(int id) {
		String sql = "select * from foundation where id = " + id;
		Foundation foundation = jdbcTemplate.queryForObject(sql, new MapperFoundation());
		return foundation;
	}

	public int getFoundationIdByName(String name) {
		String sql = "select * from foundation where name = ?";
		Foundation foundation = jdbcTemplate.queryForObject(sql, new Object[] { name }, new MapperFoundation());
		return foundation.getId();
	}

	public void updateFoundation(Foundation foundation) {
		String sql = "update foundation\r\n"
				+ "set foundation.name = ?, foundation.description = ? ,foundation.email = ?, foundation.status = ?\r\n"
				+ "where foundation.id = ?;";
		jdbcTemplate.update(sql, foundation.getName(), foundation.getDescription(), foundation.getEmail(),
				foundation.getStatus(), foundation.getId());
	}

	public void insertFoundation(Foundation foundation) {
		String sql = "INSERT INTO `Foundation` (`id`, `name`,`description`, `email`, `status`) VALUES\r\n"
				+ "(null, ?, ?, ?, ?);";
		jdbcTemplate.update(sql, foundation.getName(), foundation.getDescription(), foundation.getEmail(),
				foundation.getStatus());
	}

	public boolean isUniqueFoundation(String name, int id) {
		String sql = "SELECT COUNT(*) FROM foundation WHERE name = ? AND id <> ?";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { name, id }, Integer.class);
		return count == 0;
	}
	
	public List<Foundation> getDataFoundationActive() {
		List<Foundation> Foundation = new ArrayList<Foundation>();
		String sql = "select * from Foundation where status = 'Active' order by id;";
		Foundation = jdbcTemplate.query(sql, new MapperFoundation());
		return Foundation;
	}

	public List<Foundation> getDataFoundationPaginationActive(int start, int end) {
		List<Foundation> Foundation = new ArrayList<Foundation>();
		String sql = "select * from Foundation where status = 'Active' ORDER BY Foundation.id limit " + start + ", " + (end - start);
		Foundation = jdbcTemplate.query(sql, new MapperFoundation());
		return Foundation;
	}
}

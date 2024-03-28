package com.website.eocs.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.website.eocs.entity.Categories;
import com.website.eocs.entity.MapperCategories;

@Repository
public class CategoriesDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Categories> getDataCategories() {
		List<Categories> Categories = new ArrayList<Categories>();
		String sql = "select * from Categories order by id;";
		Categories = jdbcTemplate.query(sql, new MapperCategories());
		return Categories;
	}
	
	public List<Categories> getDataCategoriesPagination(int start, int end) {
		List<Categories> Categories = new ArrayList<Categories>();
		String sql = "select * from Categories ORDER BY Categories.id limit " + start + ", " + (end - start);
		Categories = jdbcTemplate.query(sql, new MapperCategories());
		return Categories;
	}

	public List<Categories> searchCategories(int id, String name) {
		List<Categories> searchCategories = new ArrayList<Categories>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				"select * from Categories\r\n"
						+ "where 1 = 1");
		
		if (id >= 0) {
			sqlBuilder.append(" and id = " + id);
		} else {
			sqlBuilder.append("");
		}
		

		if (name != null && !name.isEmpty()) {
			sqlBuilder.append(" and category_name like " + "'%" + name + "%'");
		}

		
		String sql = sqlBuilder.toString();
		searchCategories = jdbcTemplate.query(sql, new MapperCategories());
		return searchCategories;
	}
	
	public List<Categories> searchCategoriesPagination(int id, String name, int start, int end) {
		List<Categories> searchCategories = new ArrayList<Categories>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				"select * from Categories\r\n" + "where 1 = 1");
		
		if (id >= 0) {
			sqlBuilder.append(" and id = " + id);
		} else {
			sqlBuilder.append("");
		}
		

		if (name != null && !name.isEmpty()) {
			sqlBuilder.append(" and category_name like " + "'%" + name + "%'");
		}

		sqlBuilder.append(" limit " + start + ", " + (end - start));
		String sql = sqlBuilder.toString();
		searchCategories = jdbcTemplate.query(sql, new MapperCategories());
		return searchCategories;
	}
	
	public void deleteCategories(int id) {
		String sql = "update categories set status = 'Inactive' where id = " + id;
		jdbcTemplate.update(sql);
	}
	
	public void unDeleteCategories(int id) {
		String sql = "update categories set status = 'Active' where id = " + id;
		jdbcTemplate.update(sql);
	}
	
	@Transactional
	public void deleteMultipleCategories(List<Integer> ids) {
		for (Integer id : ids) {
            try {
                jdbcTemplate.update("update categories set status = 'Inactive' where id = ?", id);
            } catch (DataAccessException e) {
                throw new RuntimeException("Failed to delete element with id " + id, e);
            }
        }
	}
	
	public Categories getDataCategoriesById(int id) {
		String sql = "select * from categories where id = " + id;
		Categories categories = jdbcTemplate.queryForObject(sql, new MapperCategories());
		return categories;
	}
	
	public int getCategoryIdByName(String name) {
		String sql = "select * from categories where category_name = ?" ;
		Categories categories =  jdbcTemplate.queryForObject(sql, new Object[]{name}, new MapperCategories());
		return categories.getId();
	}
	
	public void updateCategories(Categories categories) {
		String sql = "update categories\r\n"
				+ "set categories.category_name = ?, categories.description = ?, categories.status = ?\r\n"
				+ "where categories.id = ?;";
		jdbcTemplate.update(sql, categories.getName(), categories.getDescription(), categories.getStatus(), categories.getId());
	}

	public void insertCategories(Categories categories) {
		String sql = "INSERT INTO `Categories` (`id`, `category_name`,`description`, `status`) VALUES\r\n"
				+ "(null, ?, ?, ?);";
		jdbcTemplate.update(sql, categories.getName(), categories.getDescription(), categories.getStatus());
	}

	public boolean isUniqueCategories(String name, int id) {
		String sql = "SELECT COUNT(*) FROM categories WHERE category_name = ? AND id <> ?";
	    int count = jdbcTemplate.queryForObject(sql, new Object[] { name, id }, Integer.class);
	    return count == 0;
	}
	
	public List<Categories> getDataCategoriesActive() {
		List<Categories> Categories = new ArrayList<Categories>();
		String sql = "select * from Categories where status = 'Active' order by id;";
		Categories = jdbcTemplate.query(sql, new MapperCategories());
		return Categories;
	}
	
	public List<Categories> getDataCategoriesPaginationActive(int start, int end) {
		List<Categories> Categories = new ArrayList<Categories>();
		String sql = "select * from Categories where status = 'Active' ORDER BY Categories.id limit " + start + ", " + (end - start);
		Categories = jdbcTemplate.query(sql, new MapperCategories());
		return Categories;
	}

}

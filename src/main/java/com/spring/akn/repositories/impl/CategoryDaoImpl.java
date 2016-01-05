package com.spring.akn.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.repositories.CategoryDAO;

@Repository
public class CategoryDaoImpl implements CategoryDAO{
	
	private JdbcTemplate jdbcTemplate;
	
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * get all category record
	 */
	public List<CategoryDTO> listCategory() {
		String sql = "SELECT c_id, c_name, c_ismenu FROM tbcategory;";
		return getJdbcTemplate().query(sql, new CategoryRowMapper());
	}
	
	/**
	 * find unique category by id 
	 */
	public CategoryDTO findCategoryById(int id) {
		String sql = "SELECT c_id, c_name, c_ismenu FROM tbcategory WHERE c_id = ? ;";
		return getJdbcTemplate().query(sql , new Object[]{id}, new CategoryResultSetExtractor());
	}
	
	/**
	 * Delete category by id
	 */
	public boolean isDeleteCategoryById(int id) {		
		String sql = "DELETE FROM tbcategory WHERE c_id = ?;";
		int result = getJdbcTemplate().update(sql, new Object[]{id});
		if (result > 0)
			return true;
		return false;
	}
	
	/**
	 * Insert into category 
	 */
	public boolean isInsertCategory(CategoryDTO categoryDTO) {
		String sql = "INSERT INTO tbcategory(c_name) VALUES(?)";
		int result = getJdbcTemplate().update(sql, new Object[]{ categoryDTO.getName()} );
		if (result>0)
			return true;
		return false;
	}
	
	/**
	 * Update category data
	 */
	public boolean isUpdateCategory(CategoryDTO categoryDTO) {
		String sql = "UPDATE tbcategory SET c_name=? WHERE c_id=? ";
		int result = getJdbcTemplate().update(sql, new Object[]{ categoryDTO.getName(), categoryDTO.getId() } );
		if (result>0)
			return true;
		return false;
	}

	public boolean isMenuToggle(int id) {		
		String sql = "UPDATE tbcategory SET c_ismenu = CASE	WHEN c_ismenu=true THEN  FALSE ELSE TRUE END WHERE c_id = ?";		
		int result = getJdbcTemplate().update(sql, new Object[]{id});
		if ( result > 0)
			return true;
		return false;
	}
	
	/**
	 * Count Total record of category
	 */
	public int countCategoryRecord() {		
		String sql = "SELECT COUNT(*) FROM tbcategory";
		return getJdbcTemplate().queryForObject(sql , Integer.class);
	}
	
	/**
	 * RowMapper class for get all category list
	 */
	public static final class CategoryRowMapper implements RowMapper<CategoryDTO>{

		public CategoryDTO mapRow(ResultSet rs, int num) throws SQLException {
			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setId(rs.getInt("c_id"));
			categoryDTO.setName(rs.getString("c_name"));
			categoryDTO.setMenu(rs.getBoolean("c_ismenu"));
			return categoryDTO;
		}
		
	}
	
	/**
	 * Get one object of category by resultset Extractor
	 */
	public static final class CategoryResultSetExtractor implements ResultSetExtractor<CategoryDTO>{

		public CategoryDTO extractData(ResultSet rs) throws SQLException, DataAccessException {
			while(rs.next()){
				CategoryDTO categoryDTO = new CategoryDTO();
				categoryDTO.setId(rs.getInt("c_id"));
				categoryDTO.setName(rs.getString("c_name"));
				categoryDTO.setMenu(rs.getBoolean("c_ismenu"));
				return categoryDTO;
			}
			return null;
		}
		
	}

}

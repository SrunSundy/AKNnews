package com.spring.akn.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.spring.akn.entities.user.User;
import com.spring.akn.repositories.UserRespositories;

@Repository
public class UserRespositoriesImpl implements UserRespositories {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int userRegister(User user) {
		String sql="INSERT INTO tbuser(user_name, user_email, user_password, user_image) VALUES(?,?,?,?)";
		user.setImage("default.jpg");
		Object[] obj={user.getUsername(),user.getEmail(),user.getPassword(),user.getImage()};
		return jdbcTemplate.update(sql,obj);
	}

	public User userLogin(String email, String password) {
		String sql = "SELECT user_id, user_name, user_email, user_password, user_image, enabled FROM tbuser WHERE enabled = 't' AND ( UPPER(user_email) = UPPER(?) AND UPPER(user_password) = UPPER(?) ) ";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { email, password }, new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNumber) throws SQLException {
					User user = new User();
					user.setId(rs.getInt("user_id"));
					user.setUsername(rs.getString("user_name"));
					user.setEmail(rs.getString("user_email"));
					user.setImage(rs.getString("user_image"));
					user.setEnabled(rs.getBoolean("enabled"));
					return user;
				}
			});
		} catch (IncorrectResultSizeDataAccessException ex) {
			return null;
		}
	}


	public int enableUser(int id) {
		String sql="UPDATE tbuser SET enabled=(SELECT CASE WHEN enabled =false THEN true ELSE false END FROM tbuser WHERE user_id=?) WHERE user_id=?";
		return jdbcTemplate.update(sql,id,id);
	}

	
	public int updateUser(User user) {
		String sql="UPDATE tbuser SET user_name=? WHERE user_id=?";
		return jdbcTemplate.update(sql, new Object[] {user.getUsername(),user.getId()});
	}


	public User getUser(int id) {
		String sql = "SELECT user_id, user_name, user_email, user_image FROM tbuser WHERE user_id=? ";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNumber) throws SQLException {
					User user = new User();
					user.setId(rs.getInt("user_id"));
					user.setUsername(rs.getString("user_name"));
					user.setEmail(rs.getString("user_email"));
					user.setImage(rs.getString("user_image"));
					return user;
				}
			});
		} catch (IncorrectResultSizeDataAccessException ex) {
			return null;
		}
	}


	public int changePassword(String newpass, int id) {
		String sql="UPDATE tbuser SET user_password=? WHERE user_id=?";
		return jdbcTemplate.update(sql,newpass,id);
	}

	
	public User getCurrentPass(int id) {	
		try{
			return jdbcTemplate.queryForObject("SELECT user_password FROM tbuser WHERE user_id=?",new Object[]{id}, new RowMapper<User>(
					) {
						public User mapRow(ResultSet rs, int rowNumber) throws SQLException {		
							User user=new User();
							user.setPassword(rs.getString("user_password"));
							return user;
						}
			} );
		} catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
	}

	/*
	 * // User Mapper Class private static final class UserMapper implements
	 * RowMapper<User> { public User mapRow(ResultSet rs, int rowNumber) throws
	 * SQLException {
	 * 
	 * } }
	 */

}

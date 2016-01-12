package com.spring.akn.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.frmApiDoc.FrmUserAdd;
import com.spring.akn.entities.frmApiDoc.FrmUserChangePwd;
import com.spring.akn.entities.frmApiDoc.FrmUserLogin;
import com.spring.akn.entities.frmApiDoc.FrmUserUpdate;
import com.spring.akn.entities.user.User;
import com.spring.akn.repositories.UserRespositories;

@Repository
public class UserRespositoriesImpl implements UserRespositories {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int userRegister(FrmUserAdd user) {
		String sql="INSERT INTO tbuser(user_name, user_email, user_password, user_image) VALUES(?,?,md5(?),?)";
		Object[] obj={user.getUsername(),user.getEmail(),user.getPassword(),user.getImage()};
		return jdbcTemplate.update(sql,obj);
	}

	public User userLogin(FrmUserLogin user) {
		String sql = "SELECT user_id, user_name, user_email, user_password, user_image, enabled FROM tbuser WHERE enabled = 't' AND ( UPPER(user_email) = UPPER(?) AND user_password = md5(?) ) ";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { user.getEmail(), user.getPassword() }, new RowMapper<User>() {
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


/*	public int enableUser(int id) {
		String sql="UPDATE tbuser SET enabled=(SELECT CASE WHEN enabled =false THEN true ELSE false END FROM tbuser WHERE user_id=?) WHERE user_id=?";
		return jdbcTemplate.update(sql,id,id);
	}*/

	
	public int updateUser(FrmUserUpdate user) {
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


	public int changePassword(FrmUserChangePwd user) {
		String sql="UPDATE tbuser SET user_password=md5(?) WHERE user_id=? AND user_password=md5(?)";
		return jdbcTemplate.update(sql,new Object[] {user.getNewpass(),user.getId(),user.getOldpass()});
	}

	@Override
	public List<User> listUser(String key, int page) {
		int offset=(page*10)-10;
		if((page == 0 && key.equals("*")) || page == 0){
			if(page == 0 && key.equals("*")) key="%";
			return jdbcTemplate.query("SELECT  user_id, user_name, user_email, user_image, enabled FROM tbuser WHERE UPPER(user_name) LIKE UPPER(?) ORDER BY user_id DESC",new Object[]{"%"+key+"%"},new UserMapper());
		}
		else if((page != 0 && key.equals("*")))
			key = "%";		
		return jdbcTemplate.query("SELECT user_id, user_name, user_email, user_image, enabled FROM tbuser WHERE UPPER(user_name) LIKE UPPER(?) ORDER BY user_id DESC LIMIT 10 OFFSET ?", new Object[]{"%"+key+"%", offset}, new UserMapper());
	}
	
	//cLass user for wrapper user information
	private static final class UserMapper implements RowMapper<User>{		
		public User mapRow(ResultSet rs, int rowNumber) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("user_id"));
			user.setUsername(rs.getString("user_name"));
			user.setEmail(rs.getString("user_email"));
			user.setImage(rs.getString("user_image"));
			user.setEnabled(rs.getBoolean("enabled"));
			return user;
		}
	}

	@Override
	public String getCurrentImage(int id) {
		String sql = "SELECT user_image FROM tbuser WHERE user_id=? ";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { id },String.class );
		} catch (IncorrectResultSizeDataAccessException ex) {
			return null;

	     }
  }
}

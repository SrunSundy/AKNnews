package com.spring.akn.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.frmApiDoc.FrmUserAdd;
import com.spring.akn.entities.frmApiDoc.FrmUserChangePwd;
import com.spring.akn.entities.frmApiDoc.FrmUserLogin;
import com.spring.akn.entities.frmApiDoc.FrmUserUpdate;
import com.spring.akn.entities.user.Role;
import com.spring.akn.entities.user.User;
import com.spring.akn.repositories.UserRespositories;

@Repository
public class UserRespositoriesImpl implements UserRespositories {

	@Autowired
	DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int userRegister(FrmUserAdd user) {
		try {
			String sql = "INSERT INTO tbuser(user_name, user_email, user_password, user_image) VALUES(?,?,?,?)";
			user.setImage("user.jpg");
			Object[] obj = { user.getUsername(), user.getEmail(), user.getPassword(), user.getImage() };
			return jdbcTemplate.update(sql, obj);
		} catch (DuplicateKeyException ex) {
			return 0;
		}
	}

	public User userLogin(FrmUserLogin user) {
		String sql = "SELECT user_id, user_name, user_email, user_password, user_image, enabled FROM tbuser WHERE enabled = 't' AND ( UPPER(user_email) = UPPER(?) AND user_password = ?) ";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { user.getEmail(), user.getPassword() },
					new RowMapper<User>() {
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
		String sql = "UPDATE tbuser SET enabled=(SELECT CASE WHEN enabled =false THEN true ELSE false END FROM tbuser WHERE user_id=?) WHERE user_id=?";
		return jdbcTemplate.update(sql, id, id);
	}

	public int updateUser(FrmUserUpdate user) {
		String sql = "UPDATE tbuser SET user_name=? WHERE user_id=?";
		return jdbcTemplate.update(sql, new Object[] { user.getUsername(), user.getId() });
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
		String sql = "UPDATE tbuser SET user_password=? WHERE user_id=? AND user_password=?";
		return jdbcTemplate.update(sql, new Object[] { user.getNewpass(), user.getId(), user.getOldpass() });
	}

	@Override
	public List<User> listUser(String key, int page,int row) {
		if(row<=15){row=15;}
		int offset = (page * row) - row;
	    if ((page != 0 && key.equals("*"))){key = "%";}
		return jdbcTemplate.query(
				"SELECT user_id, user_name, user_email, user_image, enabled FROM tbuser WHERE UPPER(user_name) LIKE UPPER(?) ORDER BY user_id DESC LIMIT ? OFFSET ?",
				new Object[] { "%" + key + "%",row,offset }, new UserMapper());
	}

	// cLass user for wrapper user information
	private static final class UserMapper implements RowMapper<User> {
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
			return jdbcTemplate.queryForObject(sql, new Object[] { id }, String.class);
		} catch (IncorrectResultSizeDataAccessException ex) {
			return null;

		}
	}

	public int updateUserImage(String imagename, int id) {
		String sql = "UPDATE tbuser SET user_image=? WHERE user_id=?";
		return jdbcTemplate.update(sql, new Object[] { imagename, id });
	};

	@Override
	public User findUserByUserName(String username) {
		
		String sql = "SELECT user_id, user_name, user_email, user_password, enabled FROM tbuser WHERE user_email = ?";
		try (Connection cnn = dataSource.getConnection(); PreparedStatement ps = cnn.prepareStatement(sql);) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setUsername(rs.getString("user_name"));
				user.setPassword(rs.getString("user_password"));
				user.setEmail(rs.getString("user_email"));
				user.setEnabled(rs.getBoolean("enabled"));
				user.setRoles(this.findUserRoleByUserId(user.getId()));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<Role> findUserRoleByUserId(int id) {
		
		List<Role> roles = new ArrayList<Role>();
		String sql = "SELECT tbrole.role_id, tbrole.role_name FROM tbuser "
				+ "LEFT JOIN tbuser_role ON tbuser.user_id = tbuser_role.user_id "
				+ "LEFT JOIN tbrole ON tbrole.role_id= tbuser_role.role_id WHERE tbuser.user_id=? ";
		try (Connection cnn = dataSource.getConnection(); PreparedStatement ps = cnn.prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Role role = new Role();
				role.setId(rs.getInt("role_id"));
				role.setName("ROLE_" + rs.getString("role_name"));
				roles.add(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return roles;
	}

	@Override
	public int getUserTotalPage(String key, int row) {
		if(row <=0 ) row=10;
		if (key.equals("*")){key = "%";}
			String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
					+ "ELSE COUNT(*)/? END user_page FROM tbuser "
					+ "WHERE user_name LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,"%"+key+"%"} ,Integer.class);
		
	}

	@Override
	public int getUserTotalRecords(String key) {
		if (key.equals("*")){key = "%";}
		String sql="SELECT COUNT(*) FROM tbuser WHERE user_name LIKE ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{"%"+key+"%"} ,Integer.class);	
	}
}

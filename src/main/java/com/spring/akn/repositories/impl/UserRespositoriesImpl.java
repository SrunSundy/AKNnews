package com.spring.akn.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.spring.akn.encryption.Encryption;
import com.spring.akn.entities.frmApiDoc.FrmLogin;
import com.spring.akn.entities.frmApiDoc.FrmUser;
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
	
	HttpHeaders headers = new HttpHeaders();
	
    public UserRespositoriesImpl() {
    	
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("Authorization", "Basic S0FBUEkhQCMkOiFAIyRLQUFQSQ==");
	}
	
	public int userRegister(FrmUserAdd user) {
	       try {		
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
				restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		        
				HttpEntity<Object> request = new HttpEntity<Object>(user, headers);
				/*ResponseEntity<Map> response = restTemplate.exchange("http://192.168.178.6:8080/KAAPI/api/authentication/weblogin",
						HttpMethod.POST, request, Map.class);*/
				ResponseEntity<Map> response = restTemplate.exchange("http://api.khmeracademy.org/api/user/mobileuserregister",
				HttpMethod.POST, request, Map.class);
				
				Map<String, Object> map = (HashMap<String, Object>) response.getBody();
		        
				if((boolean)map.get("STATUS")==true)
					return 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
	}

	public FrmUser userLogin(FrmUserLogin user) {
		
		try {

			
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
	
			HttpEntity<Object> request = new HttpEntity<Object>(user, headers);
			/*ResponseEntity<Map> response = restTemplate.exchange("http://192.168.178.6:8080/KAAPI/api/authentication/weblogin",
					HttpMethod.POST, request, Map.class);*/
			ResponseEntity<Map> response = restTemplate.exchange("http://api.khmeracademy.org/api/authentication/mobilelogin",
			HttpMethod.POST, request, Map.class);
			
			Map<String, Object> map = (HashMap<String, Object>) response.getBody();
	        
			
			if (map.get("EMAIL") != null) {
				FrmUser u=new FrmUser();
				u.setId(new Encryption().decode((String)map.get("USERID")));
				u.setUsername((String)map.get("USERNAME"));
				u.setEmail((String)map.get("EMAIL"));
				u.setImage((String)map.get("PROFILE_IMG_URL"));
				u.setCover_image((String)map.get("COVER_IMG_URL"));
				u.setStatus((boolean)map.get("STATUS"));
				return u;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
		 try {		
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
				restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
				
		        //for decode user id 
				user.setUserId(new Encryption().encode(user.getUserId()));
		        
		        
				HttpEntity<Object> request = new HttpEntity<Object>(user, headers);
				/*ResponseEntity<Map> response = restTemplate.exchange("http://192.168.178.6:8080/KAAPI/api/authentication/weblogin",
						HttpMethod.POST, request, Map.class);*/
				ResponseEntity<Map> response = restTemplate.exchange("http://api.khmeracademy.org/api/user/changepassword",
				HttpMethod.POST, request, Map.class);
				
				Map<String, Object> map = (HashMap<String, Object>) response.getBody();
		        
				if((boolean)map.get("STATUS")==true)
					return 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
	}

	@Override
	public List<User> listUser(String key, int page,int row) {
		if(row<=15){row=15;}
		int offset = (page * row) - row;
	    if ((page != 0 && key.equals("*"))){key = "%";}
		return jdbcTemplate.query("SELECT tbuser.user_id, tbuser.user_name, tbuser.user_email, tbuser.user_image, tbuser.enabled,tbuser.register_date FROM tbuser"
				+ " INNER JOIN tbuser_role ON tbuser_role.user_id=tbuser.user_id WHERE tbuser_role.role_id <> 2 AND tbuser_role.role_id <> 1  AND UPPER(user_name) LIKE UPPER(?) ORDER BY user_id DESC LIMIT ? OFFSET ?;",
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
			user.setRegister_date(rs.getDate("register_date"));
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
		
		String sql = "SELECT user_id, user_name, user_email,user_image, user_password, enabled FROM news.tbuser WHERE user_email = ?";
		try (Connection cnn = dataSource.getConnection(); PreparedStatement ps = cnn.prepareStatement(sql);) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setUsername(rs.getString("user_name"));
				user.setPassword(rs.getString("user_password"));
				user.setImage(rs.getString("user_image"));
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
		String sql = "SELECT tbrole.role_id, tbrole.role_name FROM news.tbuser "
				+ "LEFT JOIN news.tbuser_role ON news.tbuser.user_id = news.tbuser_role.user_id "
				+ "LEFT JOIN news.tbrole ON news.tbrole.role_id= news.tbuser_role.role_id WHERE news.tbuser.user_id=? ";
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
					+ "INNER JOIN tbuser_role ON tbuser_role.user_id=tbuser.user_id WHERE tbuser_role.role_id <> 2 AND tbuser_role.role_id <> 1"
					+ "AND tbuser.user_name LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,"%"+key+"%"} ,Integer.class);
		
	}

	@Override
	public int getUserTotalRecords(String key) {
		if (key.equals("*")){key = "%";}
		String sql="SELECT COUNT(*) FROM tbuser "
				+ "INNER JOIN tbuser_role ON tbuser_role.user_id=tbuser.user_id WHERE tbuser_role.role_id <> 2 AND tbuser_role.role_id <> 1"
				+ " AND tbuser.user_name LIKE ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{"%"+key+"%"} ,Integer.class);	
	}

	@Override
	public List<User> listNewUser() {	
		return jdbcTemplate.query("SELECT tbuser.user_id, tbuser.user_name, tbuser.user_email, tbuser.user_image, tbuser.enabled, tbuser.register_date FROM tbuser INNER JOIN tbuser_role  ON tbuser_role.user_id = tbuser.user_id WHERE tbuser_role.role_id = 3 ORDER BY register_date DESC LIMIT 8", new UserMapper());
	}

	@Override
	public List<User> listNewAdmin() {
		return jdbcTemplate.query("SELECT tbuser.user_id, tbuser.user_name, tbuser.user_email, tbuser.user_image, tbuser.enabled, tbuser.register_date FROM tbuser INNER JOIN tbuser_role  ON tbuser_role.user_id = tbuser.user_id WHERE tbuser_role.role_id = 2 ORDER BY register_date DESC LIMIT 8", new UserMapper());
	}
}

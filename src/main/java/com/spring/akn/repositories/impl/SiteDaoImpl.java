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

import com.spring.akn.entities.SiteDTO;
import com.spring.akn.repositories.SiteDAO;

@Repository
public class SiteDaoImpl implements SiteDAO {
	
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<SiteDTO> listSite() {
		String sql = "SELECT s_id, s_name, s_url FROM tbsite;";
		return getJdbcTemplate().query(sql , new SiteRowMapper() );
	}

	@Override
	public SiteDTO findSiteById(int id) {
		String sql = "SELECT s_id, s_name, s_url FROM tbsite WHERE c_id = ?;";
		return getJdbcTemplate().query(sql , new Object[]{id} , new SiteResultSetExtractor());
	}

	@Override
	public boolean isDeleteSiteById(int id) {
		String sql = "DELETE FROM tbsite WHERE c_id = ? ;";
		int result = getJdbcTemplate().update(sql , new Object[]{id});
		if (result > 0)
			return true;
		return false;
	}

	@Override
	public boolean isInsertSite(SiteDTO siteDTO) {
		String sql = "INSERT INTO tbsite(c_name, c_url) VALUES(?,?)";
		int result = getJdbcTemplate().update(sql , new Object[]{siteDTO.getName(), siteDTO.getUrl()});
		if (result > 0)
			return true;
		return false;
	}

	@Override
	public boolean isUpdateSite(SiteDTO siteDTO) {
		String sql = "UPDATE tbsite SET c_name=?, c_url=? WHERE c_id = ? ;";
		int result = getJdbcTemplate().update(sql , new Object[]{siteDTO.getName(), siteDTO.getUrl(), siteDTO.getId()});
		if (result > 0)
			return true;
		return false;
	}

	@Override
	public int countSiteRecord() {		
		String sql = "SELECT COUNT(*) FROM tbsite;";
		return getJdbcTemplate().queryForObject(sql , Integer.class);
	}
	
	public static final class SiteRowMapper implements RowMapper<SiteDTO>{
		@Override
		public SiteDTO mapRow(ResultSet rs, int num) throws SQLException {
			SiteDTO dto= new SiteDTO();
			dto.setId(rs.getInt("s_id"));
			dto.setName(rs.getString("s_name"));
			dto.setUrl(rs.getString("s_url"));
			return dto;
		}		
	}
	
	public static final class SiteResultSetExtractor implements ResultSetExtractor<SiteDTO>{
		@Override
		public SiteDTO extractData(ResultSet rs) throws SQLException, DataAccessException {
			while(rs.next()){
				SiteDTO dto = new SiteDTO();
				dto.setId(rs.getInt("s_id"));
				dto.setName(rs.getString("s_name"));
				dto.setUrl(rs.getString("s_url"));
				return dto;
			}
			return null;
		}
		
	}

}

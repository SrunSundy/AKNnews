package com.spring.akn.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.entities.SiteDetailDTO;
import com.spring.akn.repositories.SiteDetailDAO;
@Repository
public class SiteDetailDAOImpl implements SiteDetailDAO{
	
	private JdbcTemplate jdbcTemplate;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean isInsertSiteDetail(SiteDetailDTO siteDetailDTO) {
		String sql = "INSERT INTO tbsite_detail(site_id, category_id, url) VALUES(?, ?, ?)";
		int result = getJdbcTemplate().update(sql , new Object[]{siteDetailDTO.getS_id(),siteDetailDTO.getC_id(),siteDetailDTO.getUrl()});
		if ( result > 0 )
			return true;
		return false;
	}

	@Override
	public boolean isUpdateSiteDetail(SiteDetailDTO siteDetailDTO) {
		String sql = "UPDATE tbsite_detail SET url=? WHERE (site_id = ? AND category_id = ?)";
		int result = getJdbcTemplate().update(sql , new Object[]{siteDetailDTO.getUrl(), siteDetailDTO.getS_id(),siteDetailDTO.getC_id()});
		if ( result > 0 )
			return true;
		return false;
	}
	@Override
	public boolean isDeleteSiteDetail(int s_id, int c_id) {
		String sql = "DELETE FROM tbsite_detail WHERE (site_id = ? AND category_id = ?) AND status = false;";
		int result = getJdbcTemplate().update(sql , new Object[]{s_id,c_id});
		if ( result > 0 )
			return true;
		return false;
	}
	@Override
	public boolean isToggleStatusSiteDetail(int s_id, int c_id) {
		String sql = "UPDATE tbsite_detail SET status = CASE WHEN status=true THEN  FALSE ELSE TRUE END WHERE (site_id = ? AND category_id = ?)";
		int result = getJdbcTemplate().update(sql , new Object[]{s_id, c_id});
		if ( result > 0 )
			return true;
		return false;
	}
	@Override
	public List<SiteDetailDTO> listSiteDetail() {
		String sql = "SELECT site_id, category_id, url, status FROM tbsite_detail ORDER BY site_id ASC";
		return getJdbcTemplate().query(sql , new SiteDetailRowMapper());
	}

	/**
	 * RowMapper class for get all site detail list
	 */
	public static final class SiteDetailRowMapper implements RowMapper<SiteDetailDTO>{

		public SiteDetailDTO mapRow(ResultSet rs, int num) throws SQLException {
			SiteDetailDTO siteDetailDTO = new SiteDetailDTO();
			siteDetailDTO.setC_id(rs.getInt("category_id"));
			siteDetailDTO.setS_id(rs.getInt("site_id"));
			siteDetailDTO.setUrl(rs.getString("url"));
			siteDetailDTO.setStatus(rs.getBoolean("status"));	
			
			return siteDetailDTO;
		}
		
	}
}

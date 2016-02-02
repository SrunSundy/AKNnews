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
	
	public static final class SiteDetailResultSetExstractor implements ResultSetExtractor<SiteDetailDTO>{

		@Override
		public SiteDetailDTO extractData(ResultSet rs) throws SQLException, DataAccessException {
			if ( rs.next() ){
				SiteDetailDTO siteDetailDTO = new SiteDetailDTO();
				siteDetailDTO.setC_id(rs.getInt("category_id"));
				siteDetailDTO.setS_id(rs.getInt("site_id"));
				siteDetailDTO.setUrl(rs.getString("url"));
				siteDetailDTO.setStatus(rs.getBoolean("status"));
				return siteDetailDTO;
			}
			return null;
		}
		
	}

	@Override
	public SiteDetailDTO findSiteAndCategoryById(int s_id, int c_id) {
		String sql="SELECT * FROM tbsite_detail WHERE site_id = ? AND category_id = ? ";
		return getJdbcTemplate().query(sql, new Object[]{s_id, c_id},new SiteDetailResultSetExstractor());
	}
	
	@Override
	public int countRecord() {
		String sql = "SELECT COUNT(*) FROM tbsite_detail;";
		return getJdbcTemplate().queryForObject(sql , Integer.class);
	} 
	
	@Override
	public List<SiteDetailDTO> listSiteDetailPage(int limit, int offet ,int s_id, int c_id) {
		
		int page = (limit * offet) - limit ;
		String sql = "";
		Object[] obj = null;
		if ( s_id == 0 && c_id == 0 ) {
			sql = getSiteDetailPage(limit, page);
			obj = new Object[]{limit ,page};
		}else if ( s_id == 0 && c_id != 0 ){
			sql = getSiteDetailPageCategory(limit, page, c_id);
			obj = new Object[]{c_id,limit ,page};
		}else if ( s_id != 0 && c_id == 0){
			sql = getSiteDetailPageSite(limit, page, s_id);
			obj = new Object[]{s_id, limit ,page};
		}else {
			sql = getSiteDetailPageSiteCategory(limit, page, s_id, c_id);
			obj = new Object[]{s_id, c_id, limit ,page};
		}
		return getJdbcTemplate().query(sql , obj , new SiteDetailRowMapper());
		
	}

	public String getSiteDetailPage(int limit, int page) {
		return "SELECT site_id, category_id, url, status FROM tbsite_detail ORDER BY site_id ASC LIMIT ? OFFSET ?";
	}
	
	public String getSiteDetailPageSite(int limit, int page ,int s_id){
		return "SELECT site_id, category_id, url, status FROM tbsite_detail WHERE site_id = ? ORDER BY site_id ASC LIMIT ? OFFSET ?";
	}
	
	public String getSiteDetailPageCategory(int limit, int page ,int c_id){
		return "SELECT site_id, category_id, url, status FROM tbsite_detail WHERE category_id = ? ORDER BY site_id ASC LIMIT ? OFFSET ?";
	}
	
	public String getSiteDetailPageSiteCategory(int limit, int page ,int s_id, int c_id ){
		return "SELECT site_id, category_id, url, status FROM tbsite_detail WHERE site_id = ? AND category_id = ?   ORDER BY site_id ASC LIMIT ? OFFSET ?";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

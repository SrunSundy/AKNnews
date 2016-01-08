package com.spring.akn.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
	public boolean isInsertSiteDetail(int s_id, int c_id, String url) {
		String sql = "INSERT INTO tbsite_detail(site_id, category_id, url) VALUES(?, ?, ?)";
		int result = getJdbcTemplate().update(sql , new Object[]{s_id,c_id,url});
		if ( result > 0 )
			return true;
		return false;
	}

	@Override
	public boolean isUpdateSiteDetail(int s_id, int c_id, String url) {
		String sql = "UPDATE tbsite_detail SET url=? WHERE (site_id = ? AND category_id = ?)";
		int result = getJdbcTemplate().update(sql , new Object[]{url,s_id,c_id});
		if ( result > 0 )
			return true;
		return false;
	}
	@Override
	public boolean isDeleteSiteDetail(int s_id, int c_id) {
		String sql = "DELETE FROM tbsite_detail WHERE (site_id = ? and category_id = ?) ";
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

}

package com.spring.akn.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SiteDTO;
import com.spring.akn.repositories.NewsRepositories;

@Repository
public class NewsRepositriesImpl implements NewsRepositories {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<NewsDTO> listNewsDatas(int page, int categoryid, int siteid, int userid) {
		String sql="";
		int offset = ( page * 10 ) - 10;
		
		if(userid!=0){
			if(categoryid!=0){
				sql="SELECT n.news_id,n.news_title,n.news_description"
						+ ",n.news_img,n.news_date,n.news_url,n.news_hit,n.news_content,n.source_id,"
						+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave,c.c_name"
						+ " FROM tbnews n INNER JOIN tbcategory c ON c.c_id=n.category_id "
						+ "WHERE n.news_status=true AND n.category_id=? LIMIT 10 OFFSET ? ";
				return jdbcTemplate.query(sql,new Object[]{ userid,categoryid ,offset }, new RowMapper<NewsDTO>(){
					@Override
					public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
						NewsDTO news = new NewsDTO();
						
						news.setId(rs.getInt("news_id"));
						news.setTitle(rs.getString("news_title"));
						news.setDescription(rs.getString("news_description"));
						news.setImage(rs.getString("news_img"));
						news.setDate(rs.getDate("news_date"));
						news.setUrl(rs.getString("news_url"));
						news.setHit(rs.getInt("news_hit"));
						news.setSaved(rs.getBoolean("news_issave"));
						news.setContent(rs.getString("news_content"));
						
						SiteDTO site=new SiteDTO();
						site.setId(rs.getInt("source_id"));
						
						CategoryDTO category=new CategoryDTO();
						category.setName(rs.getString("c_name"));
						
						news.setSite(site);
						news.setCategory(category);
						
						return news;
					}
					
				});
			}
			if(siteid!=0){
				sql="SELECT n.news_id,n.news_title,n.news_description"
						+ ",n.news_img,n.news_date,n.news_url,n.news_hit,n.news_content,"
						+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave ,s.s_id,s.s_name,s.s_url "
						+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
						+ "WHERE n.news_status=true AND n.source_id=? LIMIT 10 OFFSET ? ";
				return jdbcTemplate.query(sql,new Object[]{ userid,siteid ,offset } ,new RowMapper<NewsDTO>(){
					@Override
					public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
						NewsDTO news = new NewsDTO();
						
						news.setId(rs.getInt("news_id"));
						news.setTitle(rs.getString("news_title"));
						news.setDescription(rs.getString("news_description"));
						news.setImage(rs.getString("news_img"));
						news.setDate(rs.getDate("news_date"));
						news.setUrl(rs.getString("news_url"));
						news.setHit(rs.getInt("news_hit"));
						news.setSaved(rs.getBoolean("news_issave"));
						news.setContent(rs.getString("news_content"));
						
						SiteDTO site=new SiteDTO();
						site.setId(rs.getInt("s_id"));
						site.setName(rs.getString("s_name"));
						site.setUrl(rs.getString("s_url"));
						
						news.setSite(site);
						return news;
					}
					
				});

			}
			sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,n.news_content,n.source_id, "
					+ " (CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave"
					+ " FROM tbnews n "
					+ "WHERE n.news_status=true LIMIT 10 OFFSET ? ";
			return jdbcTemplate.query(sql,new Object[]{ userid,offset } , new RowMapper<NewsDTO>(){
				@Override
				public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
					NewsDTO news = new NewsDTO();
					
					news.setId(rs.getInt("news_id"));
					news.setTitle(rs.getString("news_title"));
					news.setDescription(rs.getString("news_description"));
					news.setImage(rs.getString("news_img"));
					news.setDate(rs.getDate("news_date"));
					news.setUrl(rs.getString("news_url"));
					news.setHit(rs.getInt("news_hit"));
					news.setSaved(rs.getBoolean("news_issave"));
					news.setContent(rs.getString("news_content"));
					
					SiteDTO site=new SiteDTO();
					site.setId(rs.getInt("source_id"));
					
					news.setSite(site);
					return news;
				}
				
			});
			
		}
		if(categoryid!=0){
			sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,n.news_content,n.source_id "
					+ " FROM tbnews n INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.category_id=? LIMIT 10 OFFSET ? ";
			return jdbcTemplate.query(sql,new Object[]{categoryid ,offset }, new RowMapper<NewsDTO>(){
				@Override
				public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
					NewsDTO news = new NewsDTO();
					
					news.setId(rs.getInt("news_id"));
					news.setTitle(rs.getString("news_title"));
					news.setDescription(rs.getString("news_description"));
					news.setImage(rs.getString("news_img"));
					news.setDate(rs.getDate("news_date"));
					news.setUrl(rs.getString("news_url"));
					news.setHit(rs.getInt("news_hit"));
					news.setContent(rs.getString("news_content"));
					
					SiteDTO site=new SiteDTO();
					site.setId(rs.getInt("source_id"));
					
					CategoryDTO category=new CategoryDTO();
					category.setName(rs.getString("c_name"));
					
					news.setSite(site);
					news.setCategory(category);
					
					return news;
				}
				
			});
		}
		if(siteid!=0){
			sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,n.news_content,s.s_id,s.s_name,s.s_url "
					+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
					+ "WHERE n.news_status=true AND n.source_id=? LIMIT 10 OFFSET ? ";
			return jdbcTemplate.query(sql,new Object[]{ siteid ,offset } ,new RowMapper<NewsDTO>(){
				@Override
				public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
					NewsDTO news = new NewsDTO();
					
					news.setId(rs.getInt("news_id"));
					news.setTitle(rs.getString("news_title"));
					news.setDescription(rs.getString("news_description"));
					news.setImage(rs.getString("news_img"));
					news.setDate(rs.getDate("news_date"));
					news.setUrl(rs.getString("news_url"));
					news.setHit(rs.getInt("news_hit"));
					news.setContent(rs.getString("news_content"));
					
					SiteDTO site=new SiteDTO();
					site.setId(rs.getInt("s_id"));
					site.setName(rs.getString("s_name"));
					site.setUrl(rs.getString("s_url"));
					
					news.setSite(site);
					return news;
				}
				
			});

		}
		sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,n.news_content,n.source_id "
				+ " FROM tbnews n "
				+ "WHERE n.news_status=true LIMIT 10 OFFSET ? ";
		return jdbcTemplate.query(sql,new Object[]{ offset } , new RowMapper<NewsDTO>(){
			@Override
			public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
				NewsDTO news = new NewsDTO();
				
				news.setId(rs.getInt("news_id"));
				news.setTitle(rs.getString("news_title"));
				news.setDescription(rs.getString("news_description"));
				news.setImage(rs.getString("news_img"));
				news.setDate(rs.getDate("news_date"));
				news.setUrl(rs.getString("news_url"));
				news.setHit(rs.getInt("news_hit"));
				news.setContent(rs.getString("news_content"));
				
				SiteDTO site=new SiteDTO();
				site.setId(rs.getInt("source_id"));
				
				news.setSite(site);
				return news;
			}
			
		});
	}

	@Override
	public NewsDTO listNewsData(int newsid, int userid) {
		String sql="";
		if(userid != 0){
			sql="SELECT ";
		}
		sql="";
		return null;
	}

	@Override
	public int insertNews(NewsDTO news) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateNews(NewsDTO news) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteNews(int news) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<NewsDTO> searchNews(String key, int page, int categoryid, int siteid, int userid) {
		// TODO Auto-generated method stub
		return null;
	}
	


}

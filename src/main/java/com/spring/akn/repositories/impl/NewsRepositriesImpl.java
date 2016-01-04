package com.spring.akn.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


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
		if(categoryid!=0){
			
		}
		if(siteid!=0){
			
		}
		if(userid!=0){
			sql="";
		}
		return jdbcTemplate.query(sql, new NewsMapper());
		
	}

	@Override
	public NewsDTO listNewsData(int newsid, int userid) {
		// TODO Auto-generated method stub
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
	
	private static final class NewsMapper implements RowMapper<NewsDTO>{		
		public NewsDTO mapRow(ResultSet rs, int rowNumber) throws SQLException {
			NewsDTO news = new NewsDTO();
			
			news.setId(rs.getInt("news_id"));
			news.setTitle(rs.getString("news_title"));
			news.setDescription(rs.getString("news_description"));
			news.setImage(rs.getString("news_img"));
			news.setDate(rs.getDate("news_date"));
			news.setUrl(rs.getString("news_url"));
			news.setHit(rs.getInt("news_hit"));
			news.setStatus(rs.getBoolean("news_status"));
			news.setSaved(rs.getBoolean("news_issave"));
			news.setContent(rs.getString("news_content"));
			
			SiteDTO site=new SiteDTO();
			site.setName(rs.getString(""));
			site.setUrl(rs.getString(""));
		
			
			return news;
		}
	}

}

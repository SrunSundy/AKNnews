package com.spring.akn.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
	
		int offset = ( page * 10 ) - 10;
		if(categoryid!=0) return listNewsByCategory(userid, categoryid, offset);
		if(siteid!=0) return listNewsBySite(userid, siteid, offset);
		return listAllNews(userid, offset);
		
	}

	@Override
	public NewsDTO listNewsData(int newsid, int userid) {
		
		if(userid != 0) return listDetailWithUserId(userid, newsid);
		return listDetailWithNoUserId(newsid);
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
		int offset=(page*10)-10;

		if(categoryid != 0 ) return searchByCategory(key , categoryid, userid, offset);
		if(siteid != 0) return searchBySite(key ,siteid, userid,offset);
		return searchAll(key, userid, offset);

	}
	
	@Override
	public int saveNews(int newsid, int userid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteSavedNews(int newsid, int userid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<NewsDTO> listSavedNews() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * ADDITIONAL FUNCTION
	 * 
	 * @param key
	 * @param categoryid
	 * @param userid
	 * @param offset
	 * @return
	 */
	
	//SEARCHING FUNCTION 
	public List<NewsDTO> searchByCategory(String key,int categoryid, int userid,int offset){
		if(userid != 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_url,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN tbcategory c "
					+ "ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.category_id=?  AND n.news_title LIKE ? "
					+ "LIMIT 10 OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,categoryid,"%"+key+"%",offset},new GetNewsWithUserIDMapper());
		}
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_url "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "INNER JOIN tbcategory c "
				+ "ON c.c_id=n.category_id "
				+ "WHERE n.news_status=true AND n.category_id=? AND n.news_title LIKE ? "
				+ "LIMIT 10 OFFSET ? ";
		return jdbcTemplate.query(sql, new Object[]{categoryid,"%"+key+"%",offset},new GetNewsWithNoUserIDMapper());
		
	}
	
	public List<NewsDTO> searchBySite(String key,int siteid,int userid,int offset){
		
		if(userid != 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_url,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true AND s.s_id=? AND n.news_title LIKE ? "
					+ "LIMIT 10 OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,siteid,"%"+key+"%",offset},new GetNewsWithUserIDMapper());
		}
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_url "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ " ON s.s_id=n.source_id "
				+ "WHERE n.news_status=true AND s.s_id=? AND n.news_title LIKE ? "
				+ "LIMIT 10 OFFSET ?";
		return jdbcTemplate.query(sql, new Object[]{siteid,"%"+key+"%",offset},new GetNewsWithNoUserIDMapper());
	}
	
	public List<NewsDTO> searchAll(String key, int userid,int offset){
		
		if(userid!=0){
		
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_url,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true  AND n.news_title LIKE ? LIMIT 10 OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,"%"+key+"%",offset},new GetNewsWithUserIDMapper());

		}
	
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_url  "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "WHERE n.news_status=true  AND n.news_title LIKE ?  LIMIT 10 OFFSET ?";
		return jdbcTemplate.query(sql, new Object[]{"%"+key+"%",offset},new GetNewsWithNoUserIDMapper());

	}
	
	

	//END SERACHING FUNCTION
	

	//LIST NEWS FUNCTION 
		
	public List<NewsDTO> listNewsByCategory(int userid,int categoryid,int offset){
		if(userid!=0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_url,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "  ON s.s_id=n.source_id "
					+ " INNER JOIN tbcategory c "
					+ "ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.category_id=?  "
					+ "LIMIT 10 OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{ userid,categoryid ,offset }, new GetNewsWithUserIDMapper());
		}

			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_url "
					+ "FROM tbnews n INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "INNER JOIN tbsite s ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true AND n.category_id=? LIMIT 10 OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{categoryid ,offset }, new GetNewsWithNoUserIDMapper());
		
	}
	
	public List<NewsDTO> listNewsBySite(int userid,int siteid,int offset){
		if(userid!=0){
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave ,s.s_id,s.s_url "
					+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
					+ "WHERE n.news_status=true AND n.source_id=? LIMIT 10 OFFSET ? ";
			return jdbcTemplate.query(sql,new Object[]{ userid,siteid ,offset } ,new GetNewsWithUserIDMapper());
		}
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_url "
				+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
				+ "WHERE n.news_status=true AND n.source_id=? LIMIT 10 OFFSET ? ";
		return jdbcTemplate.query(sql,new Object[]{ siteid ,offset } ,new GetNewsWithNoUserIDMapper());
	}
	public List<NewsDTO> listAllNews(int userid,int offset){
		if(userid!=0){

			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave,s.s_id,s.s_url "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true LIMIT 10 OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{ userid,offset } , new GetNewsWithUserIDMapper());
		}
	
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_url "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "WHERE n.news_status=true LIMIT 10 OFFSET ?";
		return jdbcTemplate.query(sql,new Object[]{ offset } , new GetNewsWithNoUserIDMapper());
	}
	//END LIST NEWS FUNCTION 
	
	
	
	
	//LIST NEWS DETAIL
	
	public NewsDTO listDetailWithUserId(int userid,int newsid){
		try{
			String sql="SELECT n.news_id,n.news_title,n.news_date,"
					+ " (CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave"
					+ " FROM tbnews n "
					+ "WHERE n.news_status=true AND n.news_id=? ";
			return jdbcTemplate.queryForObject(sql,new Object[]{ userid,newsid } , new RowMapper<NewsDTO>(){
				@Override
				public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
					NewsDTO news = new NewsDTO();
					
					news.setId(rs.getInt("news_id"));
					news.setTitle(rs.getString("news_title"));
					news.setDate(rs.getDate("news_date"));
					news.setSaved(rs.getBoolean("news_issave"));
					return news;
				}
				
			});
		}catch(IncorrectResultSizeDataAccessException ex){
			return null;
		}
		
	}
	public NewsDTO listDetailWithNoUserId(int newsid){
		try{
			String sql="SELECT n.news_id,n.news_title,n.news_date"
					+ " FROM tbnews n "
					+ "WHERE n.news_status=true AND n.news_id=? ";
			return jdbcTemplate.queryForObject(sql,new Object[]{newsid } , new RowMapper<NewsDTO>(){
				@Override
				public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
					NewsDTO news = new NewsDTO();
					
					news.setId(rs.getInt("news_id"));
					news.setTitle(rs.getString("news_title"));
					news.setDate(rs.getDate("news_date"));
					return news;
				}
				
			});
		}catch(IncorrectResultSizeDataAccessException ex){
			return null;
		}
		
	}
	//END LIST NEWS DETAIL
	
	private static final class GetNewsWithUserIDMapper implements RowMapper<NewsDTO>{		
		public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
			NewsDTO news = new NewsDTO();
			
			news.setId(rs.getInt("news_id"));
			news.setTitle(rs.getString("news_title"));
			news.setDescription(rs.getString("news_description"));
			news.setImage(rs.getString("news_img"));
			news.setDate(rs.getDate("news_date"));
			news.setHit(rs.getInt("news_hit"));
			news.setUrl(rs.getString("news_url"));
			news.setSaved(rs.getBoolean("news_issave"));
		
			
			SiteDTO site=new SiteDTO();
			site.setId(rs.getInt("s_id"));
			site.setUrl(rs.getString("s_url"));
			
			news.setSite(site);
			return news;
		}
	}
	private static final class GetNewsWithNoUserIDMapper implements RowMapper<NewsDTO>{		
		public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
			NewsDTO news = new NewsDTO();
			
			news.setId(rs.getInt("news_id"));
			news.setTitle(rs.getString("news_title"));
			news.setDescription(rs.getString("news_description"));
			news.setImage(rs.getString("news_img"));
			news.setDate(rs.getDate("news_date"));
			news.setHit(rs.getInt("news_hit"));
			news.setUrl(rs.getString("news_url"));
		
			
			SiteDTO site=new SiteDTO();
			site.setId(rs.getInt("s_id"));
			site.setUrl(rs.getString("s_url"));
			
			news.setSite(site);
			return news;
		}
	}

}

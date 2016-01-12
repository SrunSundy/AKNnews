package com.spring.akn.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SaveListDTO;
import com.spring.akn.entities.SearchNewsDTO;
import com.spring.akn.entities.SiteDTO;
import com.spring.akn.repositories.NewsRepositories;

@Repository
public class NewsRepositriesImpl implements NewsRepositories {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<NewsDTO> listNewsDatas(int page,int row, int categoryid, int siteid, int userid) {
		if(page <= 0) return new ArrayList<NewsDTO>();
		if(row <=0 ) row=10;
		
		int offset = ( page * row ) - row;
		if(categoryid!=0) return listNewsByCategory(userid, categoryid, row ,offset);
		if(siteid!=0) return listNewsBySite(userid, siteid, row,offset);
		return listAllNews(userid, row,offset);
		
	}
	
	@Override
	public int insertNews(NewsDTO news) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteNews(int newsid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateNews(NewsDTO news) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int toggleNews(int newsid) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*@Override
	public NewsDTO listNewsData(int newsid, int userid) {
		
		if(userid != 0) return listDetailWithUserId(userid, newsid);
		return listDetailWithNoUserId(newsid);
	}*/
	@Override
	public List<NewsDTO> searchNews(SearchNewsDTO search) {
		int row=search.getRow();
		int page =search.getPage();
		
		if(page <=0 )  return new ArrayList<NewsDTO>();
		if(row <=0 ) row=10;
		
		int offset=(page* row)-row;

		if(search.getCid() != 0 ) 
			return searchByCategory(search.getKey() , search.getCid(), search.getSid(),row, offset);
		if(search.getSid() != 0) 
			return searchBySite(search.getKey() ,search.getSid(), search.getUid(),row,offset);
		return searchAll(search.getKey(), search.getUid(),row, offset);

	}
	
	@Override
	public int getNewsTotalPage(String key,int row,int categoryid,int siteid) {
		if(row <=0 ) row=10;
		if(categoryid != 0){
			String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
					+ "ELSE COUNT(*)/? END news_page FROM tbnews "
					+ "WHERE news_status=true AND category_id=? "
					+ "AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,categoryid,"%"+key+"%"} ,Integer.class);
		}
		if(siteid != 0){
			String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
					+ "ELSE COUNT(*)/? END news_page FROM tbnews "
					+ "WHERE news_status=true AND source_id=? "
					+ "AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,siteid,"%"+key+"%"} ,Integer.class);
		}
		String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
				+ "ELSE COUNT(*)/? END news_page FROM tbnews "
				+ "WHERE news_status=true "
				+ "AND news_title LIKE ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,"%"+key+"%"} ,Integer.class);	
	}
	
	@Override
	public int getNewsTotalRecords(String key,int categoryid,int siteid){
		if(categoryid !=0){
			String sql="SELECT COUNT(*) FROM tbnews WHERE news_status=true "
					+ "AND category_id=? AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{categoryid,"%"+key+"%"} ,Integer.class);	
		}
		if(siteid !=0){
			String sql="SELECT COUNT(*) FROM tbnews WHERE news_status=true "
					+ "AND source_id=? AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{siteid,"%"+key+"%"} ,Integer.class);	
		}
		String sql="SELECT COUNT(*) FROM tbnews WHERE news_status=true "
				+ "AND news_title LIKE ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{"%"+key+"%"} ,Integer.class);	
	}
	
	@Override
	public List<NewsDTO> getPopularNews(int userid) {
		String sql="";
		if(userid!=0){
			sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,"
					+ "n.news_date,n.news_hit,n.news_url,s.s_id,s.s_logo,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true AND n.news_date  >= CURRENT_DATE -INTERVAL '1 day' ORDER BY news_hit DESC";
			return jdbcTemplate.query(sql, new Object[]{userid},new GetNewsWithUserIDMapper());
		}
		sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,"
				+ "n.news_date,n.news_hit,n.news_url,s.s_id,s.s_logo "
				+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id=n.source_id "
				+ "WHERE n.news_status=true AND n.news_date  >= CURRENT_DATE -INTERVAL '1 day' "
				+ "ORDER BY n.news_hit DESC";
		return jdbcTemplate.query(sql, new GetNewsWithNoUserIDMapper());
	}
	
	@Override
	public int saveNews(SaveListDTO savenews) {
		String sql="INSERT INTO tbsavelist(news_id,user_id) VALUES(?,?)";
		return jdbcTemplate.update(sql,savenews.getNewsid(),savenews.getUserid());
	}

	@Override
	public int deleteSavedNews(int newsid,int userid) {
		String sql="DELETE FROM tbsavelist WHERE news_id=? AND user_id=?";
		return jdbcTemplate.update(sql,newsid,userid);
	}

	@Override
	public List<NewsDTO> listSavedNews(int userid) {
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_logo "
				+ "FROM tbsavelist sl "
				+ "INNER JOIN tbnews n ON sl.news_id=n.news_id "
				+ "INNER JOIN tbuser u ON u.user_id=sl.user_id "
				+ "INNER JOIN tbsite s ON s.s_id =n.source_id "
				+ "WHERE n.news_status=true AND u.user_id=?";
		return jdbcTemplate.query(sql, new Object[]{userid},new GetNewsWithNoUserIDMapper());
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
	public List<NewsDTO> searchByCategory(String key,int categoryid, int userid,int row,int offset){
		if(userid != 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_logo,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN tbcategory c "
					+ "ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.category_id=?  AND n.news_title LIKE ? ORDER BY n.news_date "
					+ "LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,categoryid,"%"+key+"%",row,offset},new GetNewsWithUserIDMapper());
		}
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_logo "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "INNER JOIN tbcategory c "
				+ "ON c.c_id=n.category_id "
				+ "WHERE n.news_status=true AND n.category_id=? AND n.news_title LIKE ? ORDER BY n.news_date "
				+ "LIMIT ? OFFSET ? ";
		return jdbcTemplate.query(sql, new Object[]{categoryid,"%"+key+"%",row,offset},new GetNewsWithNoUserIDMapper());
		
	}
	
	public List<NewsDTO> searchBySite(String key,int siteid,int userid,int row,int offset){
		
		if(userid != 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_logo,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true AND s.s_id=? AND n.news_title LIKE ? ORDER BY n.news_date "
					+ "LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,siteid,"%"+key+"%",row,offset},new GetNewsWithUserIDMapper());
		}
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_logo "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ " ON s.s_id=n.source_id "
				+ "WHERE n.news_status=true AND s.s_id=? AND n.news_title LIKE ? ORDER BY n.news_date "
				+ "LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql, new Object[]{siteid,"%"+key+"%",row,offset},new GetNewsWithNoUserIDMapper());
	}
	
	public List<NewsDTO> searchAll(String key, int userid,int row,int offset){
		
		if(userid!=0){
		
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_logo,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true  AND n.news_title LIKE ? ORDER BY n.news_date LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,"%"+key+"%",row,offset},new GetNewsWithUserIDMapper());

		}
	
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_logo  "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "WHERE n.news_status=true  AND n.news_title LIKE ? ORDER BY n.news_date LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql, new Object[]{"%"+key+"%",row,offset},new GetNewsWithNoUserIDMapper());

	}
	
	

	//END SERACHING FUNCTION
	

	//LIST NEWS FUNCTION 
		
	public List<NewsDTO> listNewsByCategory(int userid,int categoryid,int row,int offset){
		if(userid!=0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_logo,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "  ON s.s_id=n.source_id "
					+ " INNER JOIN tbcategory c "
					+ "ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.category_id=? ORDER BY n.news_date "
					+ "LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{ userid,categoryid,row ,offset }, new GetNewsWithUserIDMapper());
		}

			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_logo "
					+ "FROM tbnews n INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "INNER JOIN tbsite s ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true AND n.category_id=? ORDER BY n.news_date LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{categoryid ,row,offset }, new GetNewsWithNoUserIDMapper());
		
	}
	
	public List<NewsDTO> listNewsBySite(int userid,int siteid,int row,int offset){
		if(userid!=0){
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave ,s.s_id,s.s_logo "
					+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
					+ "WHERE n.news_status=true AND n.source_id=? ORDER BY n.news_date LIMIT ? OFFSET ? ";
			return jdbcTemplate.query(sql,new Object[]{ userid,siteid,row ,offset } ,new GetNewsWithUserIDMapper());
		}
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_logo "
				+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
				+ "WHERE n.news_status=true AND n.source_id=? ORDER BY n.news_date LIMIT ? OFFSET ? ";
		return jdbcTemplate.query(sql,new Object[]{ siteid ,row,offset } ,new GetNewsWithNoUserIDMapper());
	}
	public List<NewsDTO> listAllNews(int userid,int row,int offset){
		if(userid!=0){

			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave,s.s_id,s.s_logo "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true ORDER BY n.news_date LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{ userid,row,offset } , new GetNewsWithUserIDMapper());
		}
	
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_logo "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "WHERE n.news_status=true ORDER BY n.news_date LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql,new Object[]{row, offset } , new GetNewsWithNoUserIDMapper());
	}
	//END LIST NEWS FUNCTION 
	
	
	
	
	//LIST NEWS DETAIL
/*	
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
		
	}*/
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
			site.setLogo(rs.getString("s_logo"));
			
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
			site.setLogo(rs.getString("s_logo"));
			
			news.setSite(site);
			return news;
		}
	}





}

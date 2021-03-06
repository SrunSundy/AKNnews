package com.spring.akn.repositories.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SavedNewsDTO;
import com.spring.akn.entities.SearchNewsDTO;
import com.spring.akn.entities.SiteDTO;
import com.spring.akn.entities.frmApiDoc.FrmSaveListAdd;
import com.spring.akn.repositories.NewsRepositories;


/**
 * @author Dy
 */
@Repository
public class NewsRepositriesImpl implements NewsRepositories {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * is used for listing all news from the DB.
	 * we can filter through 
	 * News's category which is used categoryid 
	 * News's site which is used siteid.
	 * row and page are used for pagination.
	 * userid is used to verify the news is saved by 
	 * the specific user.
	 * 
	 * NOTE:
	 * if row is submitted with 0 value 
	 * it will automatically change the row value from 0 to 10
	 * 
	 * if page is submitted with 0 value
	 * it will return null
	 * 	
	 */
	@Override
	public List<NewsDTO> listNewsDatas(int page,int row, int categoryid, int siteid, int userid) {
		if(page <= 0) return new ArrayList<NewsDTO>();
		if(row <=0 ) row=10;
		int offset = ( page * row ) - row;
		if(categoryid !=0 && siteid!=0) 
			return listNewsBySiteAndCategory(userid, siteid, categoryid, row, offset);
		if(categoryid!=0) 
			return listNewsByCategory(userid, categoryid, row ,offset);
		if(siteid!=0) 
			return listNewsBySite(userid, siteid, row,offset);
		
		return listAllNews(userid, row,offset);
		
	}
	
	/**
	 * is used for inserting AKN news
	 */
	@Override
	public int insertNews(NewsDTO news) {
		
		int newsid=getCurSequence()+1;
		System.err.println(newsid);
		String url="detail/"+newsid;
		
		String sql="INSERT INTO news.tbnews(news_title,news_description,news_img,news_url,category_id,source_id,news_content) "
				+ "VALUES(?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql,news.getTitle(),news.getDescription(),news.getImage(),url,
				news.getCategory().getId(),6,news.getContent());
	}

	@Override
	public int deleteNews(int newsid) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * is used for changing news status to false or true
	 * which newsid is needed to change the status.
	 */
	@Override
	public int toggleNews(int newsid) {
		// TODO Auto-generated method stub
		String sql="UPDATE news.tbnews set news_status=(select CASE WHEN news_status = true THEN false ELSE true END from news.tbnews"
				+ " WHERE news_id=?) WHERE news_id=?";
		return jdbcTemplate.update(sql,newsid,newsid);
	}
	
	/**
	 * is used for counting view which
	 * newsid is needed to update view counting
	 */
	@Override
	public int updateView(int newsid) {
		// TODO Auto-generated method stub
		String sql="UPDATE news.tbnews SET news_hit=news_hit+1 WHERE news_id=?";
		return jdbcTemplate.update(sql,newsid);
	}


	/**
	 * is used to search all news from DB.
	 * we use SearchNewsDTO class to wrap the searching fields like
	 * row and page are used for pagination.
	 * categoryid and siteid are used for filtering News's list
	 * userid is used to verify the news is saved by 
	 * the specific user
	 * 
	 * 
	 *NOTE:
	 * if row is submitted with 0 value 
	 * it will automatically change the row value from 0 to 10
	 * 
	 * if page is submitted with 0 value
	 * it will return null
	 */
	@Override
	public List<NewsDTO> searchNews(SearchNewsDTO search) {
		int row=search.getRow();
		int page =search.getPage();
		
		if(page <=0 )  return new ArrayList<NewsDTO>();
		if(row <=0 ) row=10;
		
		int offset=(page* row)-row;

		if(search.getCid() !=0 && search.getSid() !=0)
			return searchBySiteAndCategory(search.getKey(), search.getSid(),  search.getCid(),search.getUid(), row, offset);
			
		if(search.getCid() != 0 ) 
			return searchByCategory(search.getKey() , search.getCid(), search.getUid(),row, offset);
		if(search.getSid() != 0) 
			return searchBySite(search.getKey() ,search.getSid(), search.getUid(),row,offset);
	
		return searchAll(search.getKey(), search.getUid(),row, offset);

	}
	
	
	/**
	 * is used for counting News's page which is
	 * depended on row
	 * key,categoryid and siteid are used for filtering News's page
	 *  
	 * userid is used for verifying the method is used by admin or user
	 * if it is used by admin News will be listed even its status false 
	 */
	@Override
	public int getNewsTotalPage(String key,int row,int categoryid,int siteid,int userid) {
		
		boolean status1=true;
		boolean status2=true;
		if(row <=0 ) row=10;
		
		if(userid<-1){status1=false;status2=false;}
		if(userid<0) status2=false;
		
		if(categoryid != 0 && siteid != 0){
			String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
					+ "ELSE COUNT(*)/? END news_page FROM news.tbnews "
					+ "WHERE (news_status=? OR news_status=?) AND category_id=? AND source_id=? "
					+ "AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,status1,status2,categoryid,siteid,"%"+key+"%"} ,Integer.class);
		}
		if(categoryid != 0){
			String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
					+ "ELSE COUNT(*)/? END news_page FROM news.tbnews "
					+ "WHERE (news_status=? OR news_status=?) AND category_id=? "
					+ "AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,status1,status2,categoryid,"%"+key+"%"} ,Integer.class);
		}
		if(siteid != 0){
			String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
					+ "ELSE COUNT(*)/? END news_page FROM news.tbnews "
					+ "WHERE (news_status=? OR news_status=?) AND source_id=? "
					+ "AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,status1,status2,siteid,"%"+key+"%"} ,Integer.class);
		}
		String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
				+ "ELSE COUNT(*)/? END news_page FROM news.tbnews "
				+ "WHERE (news_status=? OR news_status=?) "
				+ "AND news_title LIKE ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,status1,status2,"%"+key+"%"} ,Integer.class);	
	}
	
	/**
	 * is used for counting records of News
	 * userid is used for verifying the method is used by admin or user
	 * if it is used by admin News will be listed even its status false 
	 */
	@Override
	public int getNewsTotalRecords(String key,int categoryid,int siteid,int userid){
		
		boolean status1=true;
		boolean status2=true;
		if(userid<-1){status1=false;status2=false;}
		if(userid<0) status2=false;
		if(categoryid !=0 && siteid !=0){
			String sql="SELECT COUNT(*) FROM news.tbnews WHERE (news_status=? OR news_status=?) "
					+ "AND category_id=? AND source_id=? AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{status1,status2,categoryid,siteid,"%"+key+"%"} ,Integer.class);	
		}
		if(categoryid !=0){
			String sql="SELECT COUNT(*) FROM news.tbnews WHERE (news_status=? OR news_status=?) "
					+ "AND category_id=? AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{status1,status2,categoryid,"%"+key+"%"} ,Integer.class);	
		}
		if(siteid !=0){
			String sql="SELECT COUNT(*) FROM news.tbnews WHERE (news_status=? OR news_status=?) "
					+ "AND source_id=? AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{status1,status2,siteid,"%"+key+"%"} ,Integer.class);	
		}
		String sql="SELECT COUNT(*) FROM news.tbnews WHERE (news_status=? OR news_status=?) "
				+ "AND news_title LIKE ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{status1,status2,"%"+key+"%"} ,Integer.class);	
	}
	
	/**
	 * is used for listing popular news which
	 * userid is used for verifying the news is saved by
	 * the specific user.
	 */
	@Override
	public List<NewsDTO> getPopularNews(int userid,int day,int row) {
		String sql="";
		if(day<=0)day=1;
		if(userid!=0){
			sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,"
					+ "n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM news.tbnews n INNER JOIN news.tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.news_date  >= CURRENT_DATE - ( ? || ' days')::interval ORDER BY news_hit DESC LIMIT ?";
			return jdbcTemplate.query(sql, new Object[]{userid,day,row},new GetNewsWithUserIDMapper());
		}
		sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,"
				+ "n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM news.tbnews n INNER JOIN news.tbsite s ON s.s_id=n.source_id "
				+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
				+ "WHERE n.news_status=true AND n.news_date  >= CURRENT_DATE - ( ? || ' days')::interval "
				+ "ORDER BY n.news_hit DESC LIMIT ?";
		return jdbcTemplate.query(sql,new Object[]{day,row}, new GetNewsWithNoUserIDMapper());
	}
	
	
	/**
	 * listing the News's statistic from all the Website
	 */
	@Override
	public List<NewsDTO> listNewsStatistic(int categoryid, int siteid, int day, int row) {
		// TODO Auto-generated method stub
		if(row<=0) row=10;
		if(day<=0) day=1;
		if(categoryid !=0 && siteid !=0){
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM news.tbnews n INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "INNER JOIN news.tbsite s ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true AND n.category_id=? AND n.source_id=? "
					+ "AND n.news_date  >= CURRENT_DATE - ( ? || ' days')::interval "
					+ "ORDER BY n.news_hit DESC LIMIT ?";
			return jdbcTemplate.query(sql, new Object[]{categoryid,siteid,day,row}, new GetNewsWithNoUserIDMapper());
		}
		if(categoryid != 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM news.tbnews n INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "INNER JOIN news.tbsite s ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true AND n.category_id=? "
					+ "AND n.news_date  >= CURRENT_DATE - ( ? || ' days')::interval "
					+ "ORDER BY n.news_hit DESC LIMIT ?";
			return jdbcTemplate.query(sql, new Object[]{categoryid,day,row}, new GetNewsWithNoUserIDMapper());
		}
		if(siteid != 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM news.tbnews n INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "INNER JOIN news.tbsite s ON s.s_id=n.source_id "
					+ "WHERE n.news_status=true AND n.source_id=? "
					+ "AND n.news_date  >= CURRENT_DATE - ( ? || ' days')::interval "
					+ "ORDER BY n.news_hit DESC LIMIT ?";
			return jdbcTemplate.query(sql, new Object[]{siteid,day,row}, new GetNewsWithNoUserIDMapper());
		}
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM news.tbnews n INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
				+ "INNER JOIN news.tbsite s ON s.s_id=n.source_id "
				+ "WHERE n.news_status=true "
				+ "AND n.news_date  >= CURRENT_DATE - ( ? || ' days')::interval "
				+ "ORDER BY n.news_hit DESC LIMIT ?";
		return jdbcTemplate.query(sql, new Object[]{day,row}, new GetNewsWithNoUserIDMapper());
	}
	
	/**
	 * is used for saving news by specific user
	 */
	@Override
	public int saveNews(FrmSaveListAdd savenews) {
		String sql="INSERT INTO news.tbsavelist(news_id,user_id) VALUES(?,?)";
 		return jdbcTemplate.update(sql,savenews.getNewsid(),savenews.getUserid());
	}

	/**
	 * is used for deleting news by specific user
	 */
	@Override
	public int deleteSavedNews(int newsid,int userid) {
		String sql="DELETE FROM news.tbsavelist WHERE news_id=? AND user_id=?";
		return jdbcTemplate.update(sql,newsid,userid);
	}

	
	/**
	 * is used for listing all saved news by specific user
	 */
	@Override
	public List<NewsDTO> listSavedNews(int userid,int row,int page,int day){
		if(page <=0 ) return new ArrayList<NewsDTO>();
		if(row <= 0) row=10;
		int offset = (page * row) - row;
		if(day == 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_url,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name,sl.save_date "
					+ "FROM news.tbsavelist sl "
					+ "INNER JOIN news.tbnews n ON sl.news_id=n.news_id "
					+ "INNER JOIN news.tbsite s ON s.s_id =n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE  n.news_status=true  AND sl.user_id=?  ORDER BY sl.id DESC LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,row,offset},new GetSavedNewsWithNoUserIDMapper());	
		}
		if(day == 1)//list saved news for today
		{
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name,sl.save_date "
					+ "FROM news.tbsavelist sl "
					+ "INNER JOIN news.tbnews n ON sl.news_id=n.news_id "
					+ "INNER JOIN news.tbsite s ON s.s_id =n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE  n.news_status=true  AND sl.user_id=? AND sl.save_date::timestamp::date = (CURRENT_DATE - ( 0 || ' days')::interval)::timestamp::date  ORDER BY sl.id DESC LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,row,offset},new GetSavedNewsWithNoUserIDMapper());
		}else if(day > 30){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_url,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name,sl.save_date "
					+ "FROM news.tbsavelist sl "
					+ "INNER JOIN news.tbnews n ON sl.news_id=n.news_id "
					+ "INNER JOIN news.tbsite s ON s.s_id =n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE  n.news_status=true  AND sl.user_id=? AND sl.save_date::timestamp::date < (CURRENT_DATE - ( 30 || ' days')::interval)::timestamp::date  ORDER BY sl.id DESC LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,row,offset},new GetSavedNewsWithNoUserIDMapper());
		}
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name,sl.save_date "
					+ "FROM news.tbsavelist sl "
					+ "INNER JOIN news.tbnews n ON sl.news_id=n.news_id "
					+ "INNER JOIN news.tbsite s ON s.s_id =n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE  n.news_status=true  AND sl.user_id=? AND sl.save_date  >= CURRENT_DATE - ( ? || ' days')::interval ORDER BY sl.id DESC LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql, new Object[]{userid,day,row,offset},new GetSavedNewsWithNoUserIDMapper());	
	
	}
	
	
	/**
	 * separated  function which is used in searchNews()
	 * @param key
	 * @param categoryid
	 * @param userid
	 * @param row
	 * @param offset
	 * @return
	 */
	//SEARCHING FUNCTION 
	public List<NewsDTO> searchByCategory(String key,int categoryid, int userid,int row,int offset){
		boolean status1=true;
		boolean status2=true;
		if(userid > 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_url,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM news.tbnews n INNER JOIN news.tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN news.tbcategory c "
					+ "ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.category_id=?  AND n.news_title LIKE ? ORDER BY n.news_id DESC "
					+ "LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,categoryid,"%"+key+"%",row,offset},new GetNewsWithUserIDMapper());
		}
		if(userid<-1){status1=false;status2=false;}
		if(userid<0)status2=false;
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM news.tbnews n INNER JOIN news.tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "INNER JOIN news.tbcategory c "
				+ "ON c.c_id=n.category_id "
				+ "WHERE (news_status=? OR news_status=?) AND n.category_id=? AND n.news_title LIKE ? ORDER BY n.news_id DESC "
				+ "LIMIT ? OFFSET ? ";
		return jdbcTemplate.query(sql, new Object[]{status1,status2,categoryid,"%"+key+"%",row,offset},new GetNewsWithNoUserIDMapper());
		
	}
	
	/**
	 * separated  function which is used in searchNews()
	 * @param key
	 * @param siteid
	 * @param userid
	 * @param row
	 * @param offset
	 * @return
	 */
	
	public List<NewsDTO> searchBySite(String key,int siteid,int userid,int row,int offset){
		
		boolean status1=true;
		boolean status2=true;
		if(userid > 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_url,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM news.tbnews n INNER JOIN news.tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND s.s_id=? AND n.news_title LIKE ? ORDER BY n.news_id DESC "
					+ "LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,siteid,"%"+key+"%",row,offset},new GetNewsWithUserIDMapper());
		}
		if(userid<-1){status1=false;status2=false;}
		if(userid<0) status2=false;
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_url,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM news.tbnews n INNER JOIN news.tbsite s "
				+ " ON s.s_id=n.source_id "
				+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
				+ "WHERE (news_status=? OR news_status=?) AND s.s_id=? AND n.news_title LIKE ? ORDER BY n.news_id DESC "
				+ "LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql, new Object[]{status1,status2,siteid,"%"+key+"%",row,offset},new GetNewsWithNoUserIDMapper());
	}
	
	/**
	 * separated  function which is used in searchNews()
	 * @param key
	 * @param siteid
	 * @param categoryid
	 * @param userid
	 * @param row
	 * @param offset
	 * @return
	 */
	
	public List<NewsDTO> searchBySiteAndCategory(String key,int siteid,int categoryid,int userid,int row,int offset){
		
		boolean status1=true;
		boolean status2=true;
		if(userid > 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,"
					+ "n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave ,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM news.tbnews n INNER JOIN news.tbsite s ON s.s_id = n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.news_title LIKE ? AND n.source_id=? AND c.c_id=? ORDER BY n.news_id DESC LIMIT ? OFFSET ? ";
			return jdbcTemplate.query(sql, new Object[]{userid,"%"+key+"%",siteid,categoryid,row,offset},new GetNewsWithUserIDMapper());
		}
		if(userid<-1){status1=false;status2=false;}
		if(userid<0) status2=false;
		String sql="SELECT n.news_id,n.news_title,n.news_description,"
				+ "n.news_img,n.news_date,n.news_url,n.news_hit,"
				+ "s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM news.tbnews n INNER JOIN news.tbsite s ON s.s_id = n.source_id "
				+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
				+ "WHERE (news_status=? OR news_status=?) AND n.news_title LIKE ? AND n.source_id=? AND c.c_id=? ORDER BY n.news_id DESC LIMIT ? OFFSET ? ";
		return jdbcTemplate.query(sql, new Object[]{status1,status2,"%"+key+"%",siteid,categoryid,row,offset},new GetNewsWithNoUserIDMapper());
	}
	
	/**
	 * separated  function which is used in searchNews()
	 * @param key
	 * @param userid
	 * @param row
	 * @param offset
	 * @return
	 */
	public List<NewsDTO> searchAll(String key, int userid,int row,int offset){
		
		
		boolean status1=true;
		boolean status2=true;
		if(userid>0){
		
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM news.tbnews n INNER JOIN news.tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true  AND n.news_title LIKE ? ORDER BY n.news_id DESC LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,"%"+key+"%",row,offset},new GetNewsWithUserIDMapper());

		}
		if(userid<-1){status1=false;status2=false;}
		if(userid<0) status2=false;
	
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name  "
				+ "FROM news.tbnews n INNER JOIN news.tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
				+ "WHERE (news_status=? OR news_status=?)  AND n.news_title LIKE ? ORDER BY n.news_id DESC LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql, new Object[]{status1,status2,"%"+key+"%",row,offset},new GetNewsWithNoUserIDMapper());

	}
	
	

	//END SERACHING FUNCTION
	

	//LIST NEWS FUNCTION 
	/**
	 * separated  function which is used in listNewsDatas()
	 * @param userid
	 * @param categoryid
	 * @param row
	 * @param offset
	 * @return
	 */
	public List<NewsDTO> listNewsByCategory(int userid,int categoryid,int row,int offset){
		
		
		boolean status1=true;
		boolean status2=true;
		if(userid>0){
			System.err.println("LIST NEWS BY CATE");
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM news.tbnews n INNER JOIN news.tbsite s "
					+ "  ON s.s_id=n.source_id "
					+ " INNER JOIN news.tbcategory c "
					+ "ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.category_id=? ORDER BY n.news_id DESC "
					+ "LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{ userid,categoryid,row ,offset }, new GetNewsWithUserIDMapper());
		}
		if(userid<-1){status1=false;status2=false;}
		if(userid<0)status2=false;
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM news.tbnews n INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "INNER JOIN news.tbsite s ON s.s_id=n.source_id "
					+ "WHERE (n.news_status=? OR n.news_status=?) AND n.category_id=? ORDER BY n.news_id DESC LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{status1,status2,categoryid ,row,offset }, new GetNewsWithNoUserIDMapper());
		
	}
	
	/**
	 * separated  function which is used in listNewsDatas()
	 * @param userid
	 * @param siteid
	 * @param row
	 * @param offset
	 * @return
	 */
	public List<NewsDTO> listNewsBySite(int userid,int siteid,int row,int offset){
		
		boolean status1=true;
		boolean status2=true;
		if(userid>0){
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave ,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM news.tbnews n INNER JOIN news.tbsite s ON s.s_id = n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.source_id=? ORDER BY n.news_id DESC LIMIT ? OFFSET ? ";
			return jdbcTemplate.query(sql,new Object[]{ userid,siteid,row ,offset } ,new GetNewsWithUserIDMapper());
		}
		if(userid<-1){status1=false;status2=false;}
		if(userid<0){status2=false;}
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM news.tbnews n INNER JOIN news.tbsite s ON s.s_id = n.source_id "
				+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
				+ "WHERE (n.news_status=? OR n.news_status=?) AND n.source_id=? ORDER BY n.news_id DESC LIMIT ? OFFSET ? ";
		return jdbcTemplate.query(sql,new Object[]{ status1,status2,siteid ,row,offset } ,new GetNewsWithNoUserIDMapper());
	}
	
	/**
	 * separated  function which is used in listNewsDatas()
	 * @param userid
	 * @param siteid
	 * @param categoryid
	 * @param row
	 * @param offset
	 * @return
	 */
	public List<NewsDTO> listNewsBySiteAndCategory(int userid,int siteid,int categoryid,int row,int offset){
		
		boolean status1=true;
		boolean status2=true;
		if(userid > 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave ,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM news.tbnews n INNER JOIN news.tbsite s ON s.s_id = n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.source_id=? AND c.c_id=? ORDER BY n.news_id DESC  LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{ userid,siteid,categoryid,row ,offset } ,new GetNewsWithUserIDMapper());
		}
		if(userid<-1){status1=false;status2=false;}
		if(userid<0){status2=false;}
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
				+ "s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM news.tbnews n INNER JOIN news.tbsite s ON s.s_id = n.source_id "
				+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
				+ "WHERE (n.news_status=? OR n.news_status=?) AND n.source_id=? AND c.c_id=? ORDER BY n.news_id DESC  LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql,new Object[]{ status1,status2,siteid,categoryid,row ,offset } ,new GetNewsWithNoUserIDMapper());
		
	}
	
	/**
	 * separated  function which is used in listNewsDatas()
	 * @param userid
	 * @param row
	 * @param offset
	 * @return
	 */
	public List<NewsDTO> listAllNews(int userid,int row,int offset){
		
		boolean status1=true;
		boolean status2=true;
		if(userid>0){

			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM news.tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM news.tbnews n INNER JOIN news.tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true ORDER BY n.news_id DESC LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{ userid,row,offset } , new GetNewsWithUserIDMapper());
		}
		if(userid<-1){status1=false;status2=false;}
		if(userid<0){status2=false;}
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_url,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM news.tbnews n INNER JOIN news.tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "INNER JOIN news.tbcategory c ON c.c_id=n.category_id "
				+ "WHERE n.news_status=? OR n.news_status=? ORDER BY n.news_id DESC LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql,new Object[]{status1,status2,row, offset } , new GetNewsWithNoUserIDMapper());
	}
	//END LIST NEWS FUNCTION 


	
	/**
	 * is used for get last news id
	 * @return
	 */
	public int getCurSequence(){
		String sql="SELECT last_value FROM news.tbnews_news_id_seq;";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	
	private static final class GetNewsWithUserIDMapper implements RowMapper<NewsDTO>{		
		public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
			NewsDTO news = new NewsDTO();
			
			news.setId(rs.getInt("news_id"));
			news.setTitle(rs.getString("news_title"));
			news.setDescription(rs.getString("news_description"));
			news.setImage(rs.getString("news_img"));
			news.setDate(rs.getTimestamp("news_date"));
			news.setHit(rs.getInt("news_hit"));
			news.setUrl(rs.getString("news_url"));
			news.setStatus(rs.getBoolean("news_status"));
			news.setSaved(rs.getBoolean("news_issave"));
		
			
			SiteDTO site=new SiteDTO();
			site.setId(rs.getInt("s_id"));
			site.setName(rs.getString("s_name"));
			site.setUrl(rs.getString("s_url"));
			site.setLogo(rs.getString("s_logo"));
			
			CategoryDTO category=new CategoryDTO();
			category.setId(rs.getInt("c_id"));
			category.setName(rs.getString("c_name"));
			
			news.setSite(site);
			news.setCategory(category);
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
			news.setDate(rs.getTimestamp("news_date"));
			news.setHit(rs.getInt("news_hit"));
			news.setStatus(rs.getBoolean("news_status"));
			news.setUrl(rs.getString("news_url"));
		
			
			SiteDTO site=new SiteDTO();
			site.setId(rs.getInt("s_id"));
			site.setName(rs.getString("s_name"));
			site.setUrl(rs.getString("s_url"));
			site.setLogo(rs.getString("s_logo"));
			
			CategoryDTO category=new CategoryDTO();
			category.setId(rs.getInt("c_id"));
			category.setName(rs.getString("c_name"));
			
			news.setSite(site);
			news.setCategory(category);
			return news;
		}
	}
	
	private static final class GetSavedNewsWithNoUserIDMapper implements RowMapper<NewsDTO>{		
		public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
			NewsDTO news = new NewsDTO();
			
			news.setId(rs.getInt("news_id"));
			news.setTitle(rs.getString("news_title"));
			news.setDescription(rs.getString("news_description"));
			news.setImage(rs.getString("news_img"));
			news.setDate(rs.getTimestamp("news_date"));
			news.setHit(rs.getInt("news_hit"));
			news.setStatus(rs.getBoolean("news_status"));
			news.setUrl(rs.getString("news_url"));
		
			
			SiteDTO site=new SiteDTO();
			site.setId(rs.getInt("s_id"));
			site.setName(rs.getString("s_name"));
			site.setUrl(rs.getString("s_url"));
			site.setLogo(rs.getString("s_logo"));
			
			CategoryDTO category=new CategoryDTO();
			category.setId(rs.getInt("c_id"));
			category.setName(rs.getString("c_name"));
			
			SavedNewsDTO savenews=new SavedNewsDTO();
			savenews.setSaveddate(rs.getTimestamp("save_date"));
			
			news.setSite(site);
			news.setCategory(category);
			news.setSavenews(savenews);
			return news;
		}
	}

	//get news info
	/**
	 * get news's title to show
	 */
	@Override
	public String getNewsTitle(int newsid) {
		// TODO Auto-generated method stub
		String sql="SELECT news_title FROM news.tbnews WHERE news_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[]{newsid},String.class);
	}

	/**
	 * get news's description to show
	 */
	@Override
	public String getNewsDescription(int newsid) {
		// TODO Auto-generated method stub
		String sql="SELECT news_description FROM news.tbnews WHERE news_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[]{newsid},String.class);
	}
	/**
	 * get thumbnail to show
	 */
	@Override
	public String getNewsThumbnail(int newsid) {
		// TODO Auto-generated method stub
		String sql="SELECT news_img FROM news.tbnews WHERE news_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[]{newsid},String.class);
	}
	/**
	 * get content to show
	 */
	@Override
	public String getNewsContent(int newsid) {
		// TODO Auto-generated method stub
		String sql="SELECT news_content FROM news.tbnews WHERE news_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[]{newsid},String.class);
	}

	
	//update news info
	/*s
	 * (non-Javadoc)
	 * @see com.spring.akn.repositories.NewsRepositories#updateNewsTitle(int, java.lang.String)
	 */
	/**
	 * for updating news's title
	 */
	@Override
	public int updateNewsTitle(NewsDTO news) {
		// TODO Auto-generated method stub
		String sql="UPDATE news.tbnews SET  news_title= ? WHERE news_id=?";
		return jdbcTemplate.update(sql,news.getTitle(),news.getId());
	}
	/**
	 * for updating news's category
	 */
	@Override
	public int updateNewsCategory(NewsDTO news) {
		// TODO Auto-generated method stub
		String sql="UPDATE news.tbnews SET category_id =?  WHERE news_id=?";
		return jdbcTemplate.update(sql,news.getCategory().getId(),news.getId());
	}

	/**
	 * for updating news's description
	 */
	@Override
	public int updateNewsDescription(NewsDTO news) {
		// TODO Auto-generated method stub
		String sql="UPDATE news.tbnews SET  news_description= ? WHERE news_id=?";
		return jdbcTemplate.update(sql,news.getDescription(),news.getId());
		
	}


	/**
	 * for updating news's content
	 */
	@Override
	public int updateNewsContent(NewsDTO news) {
		// TODO Auto-generated method stub
		String sql="UPDATE news.tbnews SET news_content=? WHERE news_id=?";
		return jdbcTemplate.update(sql,news.getContent(),news.getId());
	}

	@Override
	public int getNewsRecordByMonth(int month) {
		// TODO Auto-generated method stub
		String sql="SELECT COUNT(*) FROM news.tbnews WHERE EXTRACT(MONTH FROM news_date) = ? AND EXTRACT(YEAR FROM news_date) = EXTRACT(YEAR FROM NOW())";
		return jdbcTemplate.queryForObject(sql,new Object[]{month},Integer.class);
	}



	


}

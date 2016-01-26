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
import com.spring.akn.entities.SearchNewsDTO;
import com.spring.akn.entities.SiteDTO;
import com.spring.akn.entities.frmApiDoc.FrmSaveListAdd;
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
		
		if(categoryid !=0 && siteid!=0) 
			return listNewsBySiteAndCategory(userid, siteid, categoryid, row, offset);
		if(categoryid!=0) 
			return listNewsByCategory(userid, categoryid, row ,offset);
		if(siteid!=0) 
			return listNewsBySite(userid, siteid, row,offset);
		
		return listAllNews(userid, row,offset);
		
	}
	
	@Override
	public int insertNews(NewsDTO news) {
		
		int newsid=getCurSequence()+1;
		System.err.println(newsid);
		String url="detail/"+newsid;
		
		String sql="INSERT INTO tbnews(news_title,news_description,news_img,news_url,category_id,source_id,news_content) "
				+ "VALUES(?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql,news.getTitle(),news.getDescription(),news.getImage(),url,
				news.getCategory().getId(),6,news.getContent());
	}

	@Override
	public int deleteNews(int newsid) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*@Override
	public int updateNews(NewsDTO news) {
		// TODO Auto-generated method stub
		String sql="UPDATE tbnews SET  news_title= ?,news_description= ?,news_url =? "
				+ ",category_id =? ,source_id =?,news_content=? WHERE news_id=?";
		return jdbcTemplate.update(sql,news.getTitle(),news.getDescription(),news.getUrl(),
				news.getCategory().getId(),news.getSite().getId(),news.getContent(),news.getId());
	}*/

	@Override
	public int toggleNews(int newsid) {
		// TODO Auto-generated method stub
		String sql="UPDATE tbnews set news_status=(select CASE WHEN news_status = true THEN false ELSE true END from tbnews"
				+ " WHERE news_id=?) WHERE news_id=?";
		return jdbcTemplate.update(sql,newsid);
	}
	
	@Override
	public int updateView(int newsid) {
		// TODO Auto-generated method stub
		String sql="UPDATE tbnews SET news_hit=news_hit+1 WHERE news_id=?";
		return jdbcTemplate.update(sql,newsid);
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

		if(search.getCid() !=0 && search.getSid() !=0)
			return searchBySiteAndCategory(search.getKey(), search.getSid(),  search.getCid(),search.getUid(), row, offset);
			
		if(search.getCid() != 0 ) 
			return searchByCategory(search.getKey() , search.getCid(), search.getUid(),row, offset);
		if(search.getSid() != 0) 
			return searchBySite(search.getKey() ,search.getSid(), search.getUid(),row,offset);
	
		return searchAll(search.getKey(), search.getUid(),row, offset);

	}
	
	@Override
	public int getNewsTotalPage(String key,int row,int categoryid,int siteid,int userid) {
		
		boolean status1=true;
		boolean status2=true;
		if(row <=0 ) row=10;
		
		if(userid<0) status2=false;
		
		if(categoryid != 0 && siteid != 0){
			String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
					+ "ELSE COUNT(*)/? END news_page FROM tbnews "
					+ "WHERE (news_status=? OR news_status=?) AND category_id=? AND source_id=? "
					+ "AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,status1,status2,categoryid,siteid,"%"+key+"%"} ,Integer.class);
		}
		if(categoryid != 0){
			String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
					+ "ELSE COUNT(*)/? END news_page FROM tbnews "
					+ "WHERE (news_status=? OR news_status=?) AND category_id=? "
					+ "AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,status1,status2,categoryid,"%"+key+"%"} ,Integer.class);
		}
		if(siteid != 0){
			String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
					+ "ELSE COUNT(*)/? END news_page FROM tbnews "
					+ "WHERE (news_status=? OR news_status=?) AND source_id=? "
					+ "AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,status1,status2,siteid,"%"+key+"%"} ,Integer.class);
		}
		String sql="SELECT CASE WHEN COUNT(*)% ? !=0 THEN COUNT(*)/ ? +1 "
				+ "ELSE COUNT(*)/? END news_page FROM tbnews "
				+ "WHERE (news_status=? OR news_status=?) "
				+ "AND news_title LIKE ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{row,row,row,status1,status2,"%"+key+"%"} ,Integer.class);	
	}
	
	@Override
	public int getNewsTotalRecords(String key,int categoryid,int siteid,int userid){
		
		boolean status1=true;
		boolean status2=true;
		
		if(userid<0) status2=false;
		if(categoryid !=0 && siteid !=0){
			String sql="SELECT COUNT(*) FROM tbnews WHERE (news_status=? OR news_status=?) "
					+ "AND category_id=? AND source_id=? AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{status1,status2,categoryid,siteid,"%"+key+"%"} ,Integer.class);	
		}
		if(categoryid !=0){
			String sql="SELECT COUNT(*) FROM tbnews WHERE (news_status=? OR news_status=?) "
					+ "AND category_id=? AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{status1,status2,categoryid,"%"+key+"%"} ,Integer.class);	
		}
		if(siteid !=0){
			String sql="SELECT COUNT(*) FROM tbnews WHERE (news_status=? OR news_status=?) "
					+ "AND source_id=? AND news_title LIKE ?";
			return jdbcTemplate.queryForObject(sql, new Object[]{status1,status2,siteid,"%"+key+"%"} ,Integer.class);	
		}
		String sql="SELECT COUNT(*) FROM tbnews WHERE (news_status=? OR news_status=?) "
				+ "AND news_title LIKE ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{status1,status2,"%"+key+"%"} ,Integer.class);	
	}
	
	@Override
	public List<NewsDTO> getPopularNews(int userid) {
		String sql="";
		if(userid!=0){
			sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,"
					+ "n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.news_date  >= CURRENT_DATE -INTERVAL '1 day' ORDER BY news_hit DESC LIMIT 10";
			return jdbcTemplate.query(sql, new Object[]{userid},new GetNewsWithUserIDMapper());
		}
		sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,"
				+ "n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id=n.source_id "
				+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
				+ "WHERE n.news_status=true AND n.news_date  >= CURRENT_DATE -INTERVAL '1 day' "
				+ "ORDER BY n.news_hit DESC LIMIT 10";
		return jdbcTemplate.query(sql, new GetNewsWithNoUserIDMapper());
	}
	
	@Override
	public int saveNews(FrmSaveListAdd savenews) {
		String sql="INSERT INTO tbsavelist(news_id,user_id) VALUES(?,?)";
		return jdbcTemplate.update(sql,savenews.getNewsid(),savenews.getUserid());
	}

	@Override
	public int deleteSavedNews(int newsid,int userid) {
		String sql="DELETE FROM tbsavelist WHERE news_id=? AND user_id=?";
		return jdbcTemplate.update(sql,newsid,userid);
	}

	

	@Override
	public List<NewsDTO> listSavedNews(int userid,int row, int page) {
		if(page <= 0) return new ArrayList<NewsDTO>();
		if(row <=0 ) row=10;
		int offset = ( page * row ) - row;
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM tbsavelist sl "
				+ "INNER JOIN tbnews n ON sl.news_id=n.news_id "
				+ "INNER JOIN tbuser u ON u.user_id=sl.user_id "
				+ "INNER JOIN tbsite s ON s.s_id =n.source_id "
				+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
				+ "WHERE n.news_status=true AND u.user_id=? LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql, new Object[]{userid,row,offset},new GetNewsWithNoUserIDMapper());
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
		boolean status1=true;
		boolean status2=true;
		if(userid > 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN tbcategory c "
					+ "ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.category_id=?  AND n.news_title LIKE ? ORDER BY n.news_id DESC "
					+ "LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,categoryid,"%"+key+"%",row,offset},new GetNewsWithUserIDMapper());
		}
		if(userid<0)status2=false;
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "INNER JOIN tbcategory c "
				+ "ON c.c_id=n.category_id "
				+ "WHERE (news_status=? OR news_status=?) AND n.category_id=? AND n.news_title LIKE ? ORDER BY n.news_id DESC "
				+ "LIMIT ? OFFSET ? ";
		return jdbcTemplate.query(sql, new Object[]{status1,status2,categoryid,"%"+key+"%",row,offset},new GetNewsWithNoUserIDMapper());
		
	}
	
	public List<NewsDTO> searchBySite(String key,int siteid,int userid,int row,int offset){
		
		boolean status1=true;
		boolean status2=true;
		if(userid > 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND s.s_id=? AND n.news_title LIKE ? ORDER BY n.news_id DESC "
					+ "LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,siteid,"%"+key+"%",row,offset},new GetNewsWithUserIDMapper());
		}
		if(userid<0) status2=false;
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ " ON s.s_id=n.source_id "
				+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
				+ "WHERE (news_status=? OR news_status=?) AND s.s_id=? AND n.news_title LIKE ? ORDER BY n.news_id DESC "
				+ "LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql, new Object[]{status1,status2,siteid,"%"+key+"%",row,offset},new GetNewsWithNoUserIDMapper());
	}
	
	public List<NewsDTO> searchBySiteAndCategory(String key,int siteid,int categoryid,int userid,int row,int offset){
		
		boolean status1=true;
		boolean status2=true;
		if(userid > 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description,"
					+ "n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave ,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
					+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.news_title LIKE ? AND n.source_id=? AND c.c_id=? ORDER BY n.news_id DESC LIMIT ? OFFSET ? ";
			return jdbcTemplate.query(sql, new Object[]{userid,"%"+key+"%",siteid,categoryid,row,offset},new GetNewsWithUserIDMapper());
		}
		if(userid<0) status2=false;
		String sql="SELECT n.news_id,n.news_title,n.news_description,"
				+ "n.news_img,n.news_date,n.news_url,n.news_hit,"
				+ "s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
				+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
				+ "WHERE (news_status=? OR news_status=?) AND n.news_title LIKE ? AND n.source_id=? AND c.c_id=? ORDER BY n.news_id DESC LIMIT ? OFFSET ? ";
		return jdbcTemplate.query(sql, new Object[]{status1,status2,"%"+key+"%",siteid,categoryid,row,offset},new GetNewsWithNoUserIDMapper());
	}
	
	public List<NewsDTO> searchAll(String key, int userid,int row,int offset){
		
		
		boolean status1=true;
		boolean status2=true;
		if(userid>0){
		
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true  AND n.news_title LIKE ? ORDER BY n.news_id DESC LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql, new Object[]{userid,"%"+key+"%",row,offset},new GetNewsWithUserIDMapper());

		}
		if(userid<0) status2=false;
	
		String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name  "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
				+ "WHERE (news_status=? OR news_status=?)  AND n.news_title LIKE ? ORDER BY n.news_id DESC LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql, new Object[]{status1,status2,"%"+key+"%",row,offset},new GetNewsWithNoUserIDMapper());

	}
	
	

	//END SERACHING FUNCTION
	

	//LIST NEWS FUNCTION 
		
	public List<NewsDTO> listNewsByCategory(int userid,int categoryid,int row,int offset){
		
		
		boolean status1=true;
		boolean status2=true;
		if(userid>0){
			System.err.println("LIST NEWS BY CATE");
			String sql="SELECT n.news_id,n.news_title,n.news_description,n.news_img,n.news_date,n.news_hit,n.news_url,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "  ON s.s_id=n.source_id "
					+ " INNER JOIN tbcategory c "
					+ "ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.category_id=? ORDER BY n.news_id DESC "
					+ "LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{ userid,categoryid,row ,offset }, new GetNewsWithUserIDMapper());
		}

		if(userid<0){status2=false;System.err.println("yes");}
		System.err.println(userid);
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM tbnews n INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "INNER JOIN tbsite s ON s.s_id=n.source_id "
					+ "WHERE (n.news_status=? OR n.news_status=?) AND n.category_id=? ORDER BY n.news_id DESC LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{status1,status2,categoryid ,row,offset }, new GetNewsWithNoUserIDMapper());
		
	}
	
	public List<NewsDTO> listNewsBySite(int userid,int siteid,int row,int offset){
		
		boolean status1=true;
		boolean status2=true;
		if(userid>0){
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave ,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
					+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.source_id=? ORDER BY n.news_id DESC LIMIT ? OFFSET ? ";
			return jdbcTemplate.query(sql,new Object[]{ userid,siteid,row ,offset } ,new GetNewsWithUserIDMapper());
		}
		if(userid<0){status2=false;}
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
				+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
				+ "WHERE (n.news_status=? OR n.news_status=?) AND n.source_id=? ORDER BY n.news_id DESC LIMIT ? OFFSET ? ";
		return jdbcTemplate.query(sql,new Object[]{ status1,status2,siteid ,row,offset } ,new GetNewsWithNoUserIDMapper());
	}
	
	public List<NewsDTO> listNewsBySiteAndCategory(int userid,int siteid,int categoryid,int row,int offset){
		
		boolean status1=true;
		boolean status2=true;
		if(userid > 0){
			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave ,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
					+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true AND n.source_id=? AND c.c_id=? ORDER BY n.news_id DESC  LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{ userid,siteid,categoryid,row ,offset } ,new GetNewsWithUserIDMapper());
		}
		if(userid<0){status2=false;}
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
				+ "s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM tbnews n INNER JOIN tbsite s ON s.s_id = n.source_id "
				+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
				+ "WHERE (n.news_status=? OR n.news_status=?) AND n.source_id=? AND c.c_id=? ORDER BY n.news_id DESC  LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql,new Object[]{ status1,status2,siteid,categoryid,row ,offset } ,new GetNewsWithNoUserIDMapper());
		
	}
	public List<NewsDTO> listAllNews(int userid,int row,int offset){
		
		boolean status1=true;
		boolean status2=true;
		if(userid>0){

			String sql="SELECT n.news_id,n.news_title,n.news_description"
					+ ",n.news_img,n.news_date,n.news_url,n.news_hit,"
					+ "(CASE WHEN n.news_id IN (SELECT news_id FROM tbsavelist WHERE user_id=? ) THEN TRUE ELSE FALSE END) AS news_issave,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
					+ "FROM tbnews n INNER JOIN tbsite s "
					+ "ON s.s_id=n.source_id "
					+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
					+ "WHERE n.news_status=true ORDER BY n.news_id DESC LIMIT ? OFFSET ?";
			return jdbcTemplate.query(sql,new Object[]{ userid,row,offset } , new GetNewsWithUserIDMapper());
		}
		if(userid<0){status2=false;}
		String sql="SELECT n.news_id,n.news_title,n.news_description"
				+ ",n.news_img,n.news_date,n.news_url,n.news_hit,s.s_id,s.s_name,s.s_logo,n.news_status,c.c_id,c.c_name "
				+ "FROM tbnews n INNER JOIN tbsite s "
				+ "ON s.s_id=n.source_id "
				+ "INNER JOIN tbcategory c ON c.c_id=n.category_id "
				+ "WHERE n.news_status=? OR n.news_status=? ORDER BY n.news_id DESC LIMIT ? OFFSET ?";
		return jdbcTemplate.query(sql,new Object[]{status1,status2,row, offset } , new GetNewsWithNoUserIDMapper());
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
	
	
	public int getCurSequence(){
		String sql="SELECT last_value FROM tbnews_news_id_seq;";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	/*@Override
	public NewsDTO listAData(int newsid) {
		// TODO Auto-generated method stub
		String sql="SELECT news_title,news_description,news_img,news_url,category_id,news_content,news_status FROM tbnews WHERE news_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[]{newsid},new GetAnNewsMapper());
	}
	*/
	
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
			site.setLogo(rs.getString("s_logo"));
			
			CategoryDTO category=new CategoryDTO();
			category.setId(rs.getInt("c_id"));
			category.setName(rs.getString("c_name"));
			
			news.setSite(site);
			news.setCategory(category);
			return news;
		}
	}

/*	private static final class GetAnNewsMapper implements RowMapper<NewsDTO>{		
		public NewsDTO mapRow(ResultSet rs, int row) throws SQLException {
			NewsDTO news = new NewsDTO();
			
			news.setTitle(rs.getString("news_title"));
			news.setDescription(rs.getString("news_description"));
			news.setImage(rs.getString("news_img"));
			news.setStatus(rs.getBoolean("news_status"));
			news.setContent(rs.getString("news_content"));
			news.setUrl(rs.getString("news_url"));
			
			CategoryDTO category=new CategoryDTO();
			category.setId(rs.getInt("category_id"));
		
			news.setCategory(category);
			return news;
		}
	}*/
	//get news info
	@Override
	public String getNewsTitle(int newsid) {
		// TODO Auto-generated method stub
		String sql="SELECT news_title FROM tbnews WHERE news_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[]{newsid},String.class);
	}

	@Override
	public String getNewsDescription(int newsid) {
		// TODO Auto-generated method stub
		String sql="SELECT news_description FROM tbnews WHERE news_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[]{newsid},String.class);
	}

	@Override
	public String getNewsThumbnail(int newsid) {
		// TODO Auto-generated method stub
		String sql="SELECT news_img FROM tbnews WHERE news_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[]{newsid},String.class);
	}

	@Override
	public String getNewsContent(int newsid) {
		// TODO Auto-generated method stub
		String sql="SELECT news_content FROM tbnews WHERE news_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[]{newsid},String.class);
	}

	
	//update news info
	/*s
	 * (non-Javadoc)
	 * @see com.spring.akn.repositories.NewsRepositories#updateNewsTitle(int, java.lang.String)
	 */
	
	@Override
	public int updateNewsTitle(NewsDTO news) {
		// TODO Auto-generated method stub
		String sql="UPDATE tbnews SET  news_title= ? WHERE news_id=?";
		return jdbcTemplate.update(sql,news.getTitle(),news.getId());
	}

	@Override
	public int updateNewsCategory(NewsDTO news) {
		// TODO Auto-generated method stub
		String sql="UPDATE tbnews SET category_id =?  WHERE news_id=?";
		return jdbcTemplate.update(sql,news.getCategory().getId(),news.getId());
	}

	@Override
	public int updateNewsDescription(NewsDTO news) {
		// TODO Auto-generated method stub
		String sql="UPDATE tbnews SET  news_description= ? WHERE news_id=?";
		return jdbcTemplate.update(sql,news.getDescription(),news.getId());
		
	}

	/*@Override
	public int updateNewsThumbnail(NewsDTO news) {
		// TODO Auto-generated method stub
		String sql="UPDATE tbnews SET  news_img=? WHERE news_id=?";
		return jdbcTemplate.update(sql,news.getImage(),news.getId());
	}*/

	@Override
	public int updateNewsContent(NewsDTO news) {
		// TODO Auto-generated method stub
		String sql="UPDATE tbnews SET news_content=? WHERE news_id=?";
		return jdbcTemplate.update(sql,news.getContent(),news.getId());
	}

	


}

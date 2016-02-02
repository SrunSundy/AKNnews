package com.spring.akn.repositories;

import java.util.List;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SearchNewsDTO;
import com.spring.akn.entities.frmApiDoc.FrmSaveListAdd;

public interface NewsRepositories {

	public List<NewsDTO> listNewsDatas(int page,int row,int categoryid,int siteid,int userid);
	
	//public NewsDTO listNewsData(int newsid,int userid);
	
	public int insertNews(NewsDTO news);
	
	public int deleteNews(int newsid);
	
	//public int updateNews(NewsDTO news);
	
	public int updateView(int newsid);
	
	public int toggleNews(int newsid);
	
	public List<NewsDTO> searchNews(SearchNewsDTO search);
	
	public List<NewsDTO> getPopularNews(int userid,int day,int row);
	
	/*public NewsDTO listAData(int newsid);*/
	
	public String getNewsTitle(int newsid);
	
	public String getNewsDescription(int newsid);
	
	public String getNewsThumbnail(int newsid);
	
	public String getNewsContent(int newsid);
	
	public int updateNewsTitle(NewsDTO news);
	
	public int updateNewsCategory(NewsDTO news);
	
	public int updateNewsDescription(NewsDTO news);
	
/*	public int updateNewsThumbnail(NewsDTO news);
*/	
	public int updateNewsContent(NewsDTO news);
	
	public int getNewsTotalPage(String key,int row,int categoryid,int siteid,int userid); 
	
	public int getNewsTotalRecords(String key,int categoryid,int siteid,int userid);
	
	public int saveNews(FrmSaveListAdd savenews);
	
	public int deleteSavedNews(int newsid,int userid);
	
	public List<NewsDTO> listSavedNews(int userid,int row,int page,int day);
	
	public List<NewsDTO> listNewsStatistic(int categoryid,int siteid,int day,int row);
}

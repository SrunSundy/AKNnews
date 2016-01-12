package com.spring.akn.repositories;

import java.util.List;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SaveListDTO;
import com.spring.akn.entities.SearchNewsDTO;

public interface NewsRepositories {

	public List<NewsDTO> listNewsDatas(int page,int row,int categoryid,int siteid,int userid);
	
	//public NewsDTO listNewsData(int newsid,int userid);
	
	public int insertNews(NewsDTO news);
	
	public int deleteNews(int newsid);
	
	public int updateNews(NewsDTO news);
	
	public int toggleNews(int newsid);
	
	public List<NewsDTO> searchNews(SearchNewsDTO search);
	
	public List<NewsDTO> getPopularNews(int userid);
	
	public int getNewsTotalPage(String key,int row,int categoryid,int siteid); 
	
	public int getNewsTotalRecords(String key,int categoryid,int siteid);
	
	public int saveNews(SaveListDTO savenews);
	
	public int deleteSavedNews(int newsid,int userid);
	
	public List<NewsDTO> listSavedNews(int userid);
}

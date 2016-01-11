package com.spring.akn.services;

import java.util.List;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SaveListDTO;
import com.spring.akn.entities.SearchNewsDTO;

public interface NewsService {

	public List<NewsDTO> listNewsDatas(int page,int categoryid,int siteid,int userid);
	
	//public NewsDTO listNewsData(int newsid,int userid);
	
	
	public List<NewsDTO> searchNews(SearchNewsDTO search);
	
	public int getNewsTotalPage(String key,int categoryid,int siteid);
	
	public int getNewsTotalRecords(String key,int categoryid,int siteid);
	
	public List<NewsDTO> getPopularNews(int userid);
	
	public int saveNews(SaveListDTO savenews);
	
	public int deleteSavedNews(int newsid,int userid);
	
	public List<NewsDTO> listSavedNews(int userid);
	
}

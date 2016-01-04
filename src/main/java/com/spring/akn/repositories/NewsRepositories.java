package com.spring.akn.repositories;

import java.util.List;

import com.spring.akn.entities.NewsDTO;

public interface NewsRepositories {

	public List<NewsDTO> listNewsDatas(int page,int categoryid,int siteid,int userid);
	
	public NewsDTO listNewsData(int newsid,int userid);
	
	public int insertNews(NewsDTO news);
	
	public int updateNews(NewsDTO news);
	
	public int deleteNews(int news);
	
	public List<NewsDTO> searchNews(String key,int page,int categoryid,int siteid,int userid);
	
}

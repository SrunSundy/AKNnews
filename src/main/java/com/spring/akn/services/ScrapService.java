package com.spring.akn.services;

import com.spring.akn.entities.NewsDTO;

public interface ScrapService {
	/***
	 * return row affected after insert into database
	 */
	public int scrapAllSites();
	
	public NewsDTO scrapNews(String url, int user_id);
}

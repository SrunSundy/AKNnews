package com.spring.akn.services;

import com.spring.akn.entities.NewsDTO;

public interface ScrapService {
	/***
	 * scrap by all sites
	 * return row affected after insert into database
	 */
	public int scrapAllSites();
	
	public NewsDTO scrapNews(String url, int user_id);
	
	/***
	 * scrap by one site
	 * return row affected after insert into database
	 */
	public int scrapSite(int site_id);
	
}

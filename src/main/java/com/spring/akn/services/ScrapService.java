package com.spring.akn.services;

import com.spring.akn.entities.NewsDTO;

public interface ScrapService {
	/***
	 * scrap by all sites<br>
	 * return row affected after insert into database
	 */
	public int scrapAllSites();
	
	/***
	 * detail news for reading
	 * */
	public NewsDTO scrapNews(String url, int user_id);
	
	/***
	 * scrap by one site<br>
	 * return row affected after insert into database
	 */
	public int scrapSite(int site_id);
	
}

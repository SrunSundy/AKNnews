package com.spring.akn.repositories;

import com.spring.akn.entities.NewsDTO;

public interface ScrapDAO {
	
		/***
		 * return number of news being insert
		 */
		public int[] scrapAllSites();
		
		public NewsDTO scrapNews(String url, int user_id);
		
		/***
		 * return number of news being insert
		 */
		public int[] scrapSite(int site_id);
}
	
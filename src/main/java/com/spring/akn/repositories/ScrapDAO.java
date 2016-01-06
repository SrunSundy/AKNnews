package com.spring.akn.repositories;

import com.spring.akn.entities.NewsDTO;

public interface ScrapDAO {
	
		/***
		 * return affected row
		 */
		public int[] scrapAllSites();
		
		public NewsDTO scrapNews(String url);
		
}
	
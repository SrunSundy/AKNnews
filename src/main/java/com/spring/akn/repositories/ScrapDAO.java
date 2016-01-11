package com.spring.akn.repositories;

import com.spring.akn.entities.NewsDTO;

public interface ScrapDAO {
	
		/***
		 * return number of news being insert
		 */
		public int[] scrapAllSites();
		
		
		/***
		 * detail news for reading
		 * */
		public NewsDTO scrapNews(int id, int user_id);
		
		/***
		 * return number of news being insert
		 */
		public int[] scrapSite(int site_id);
}
	
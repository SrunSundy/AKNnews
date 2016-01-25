package com.spring.akn.repositories;

import java.util.ArrayList;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.scrap.StructureDTO;
import com.spring.akn.entities.scrap.TestScrapDTO;

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
		
		/***
		 * use for test scraping if right or wrong
		 * return news object
		 */
		public ArrayList<TestScrapDTO> testScrap(StructureDTO selector);
		
		/***
		 * use for scraping news content
		 * return string
		 */
		public String testScrapContent(StructureDTO selector);
}
	
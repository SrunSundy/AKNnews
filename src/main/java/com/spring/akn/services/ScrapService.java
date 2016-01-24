package com.spring.akn.services;

import java.util.ArrayList;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.scrap.StructureDTO;

public interface ScrapService {
	/***
	 * scrap by all sites<br>
	 * return row affected after insert into database
	 */
	public int scrapAllSites();
	
	/***
	 * detail news for reading
	 * */
	public NewsDTO scrapNews(int id, int user_id);
	
	/***
	 * scrap by one site<br>
	 * return row affected after insert into database
	 */
	public int scrapSite(int site_id);
	
	
	/***
	 * use for test scraping if right or wrong
	 * return news object
	 */
	public ArrayList<NewsDTO> testScrap(StructureDTO selector);
	
	/***
	 * use for scraping content of news
	 * return string
	 */
	public String testScrapContent(StructureDTO selector);
	
	
}

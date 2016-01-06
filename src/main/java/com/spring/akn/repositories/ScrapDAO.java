package com.spring.akn.repositories;

import java.util.ArrayList;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.scrap.ScrapNewsDTO;

public interface ScrapDAO {
	
		public ArrayList<NewsDTO> scrapAllSites();
			
}
	
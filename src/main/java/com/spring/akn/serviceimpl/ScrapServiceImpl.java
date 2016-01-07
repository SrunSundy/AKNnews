package com.spring.akn.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.repositories.ScrapDAO;
import com.spring.akn.services.ScrapService;

@Service
public class ScrapServiceImpl implements ScrapService{

	@Autowired ScrapDAO scrapDAO;
	
	@Override
	public int scrapAllSites() {
		
		int[] size = scrapDAO.scrapAllSites();
		
		int affected = 0;
		
		for(int i=0; i<size.length; i++)
			if(size[i]==1)
				affected++;
		
		return affected;
	}

	@Override
	public NewsDTO scrapNews(String url, int user_id) {
		
		return scrapDAO.scrapNews(url, user_id);
		
	}
	
	
}
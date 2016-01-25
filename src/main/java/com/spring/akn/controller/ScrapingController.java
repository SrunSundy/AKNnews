package com.spring.akn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.spring.akn.services.ScrapService;

@Configuration
@EnableScheduling
public class ScrapingController {

	@Autowired
	ScrapService scrapService;
	
	private static int i = 0;	
	
	@Scheduled(initialDelay=1000000, fixedDelay=2000000)
	public void autoScrap(){
		
		/* this method execute twice because of spring security, so i decided to do like that !!!*/
		if(i%2!=0){
			
			System.out.println("AUTO SCRAPING EXECUTING...!");
			
			int affected = scrapService.scrapAllSites();
			
			System.out.println("ROW_AFFECTED : " + affected);
		}
		
		i++;	
	}
}

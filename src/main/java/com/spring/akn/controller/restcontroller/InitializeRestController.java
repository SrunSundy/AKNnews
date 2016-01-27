package com.spring.akn.controller.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SiteDTO;
import com.spring.akn.services.CategoryServices;
import com.spring.akn.services.NewsService;
import com.spring.akn.services.ScrapService;
import com.spring.akn.services.SiteServices;

@RestController
@RequestMapping("api/initialize")
public class InitializeRestController {
	
	@Autowired
	NewsService newsService;
	
	@Autowired
	SiteServices siteService;
	
	@Autowired
	CategoryServices categoryService;
	
	@Autowired
	ScrapService scrapService;
	
	@RequestMapping(value="/{row}/{cid}/{uid}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> initialize(@PathVariable("row") int row, @PathVariable int cid, @PathVariable("uid") int uid){
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		List<NewsDTO> news = newsService.listNewsDatas(1,row, cid, 0, uid);
		List<CategoryDTO> categories = categoryService.listCategoryHaveNews();
		List<SiteDTO> sites = siteService.listSite();
		List<NewsDTO> populars = newsService.getPopularNews(uid,1,10);
		
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "INITIALIZE SUCCESSFULLY.");
		map.put("NEWS", news);
		map.put("CATEGORY", categories);
		map.put("SITE", sites);
		map.put("POPULAR", populars);
		
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);	
	}
	
	@RequestMapping(value="/detail/{nid}/{uid}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> initDetail(@PathVariable("nid") int nid, @PathVariable("uid") int uid){
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		List<CategoryDTO> categories = categoryService.listCategoryHaveNews();
		List<SiteDTO> sites = siteService.listSite();
		List<NewsDTO> populars = newsService.getPopularNews(0,7,7);
		
		NewsDTO news= scrapService.scrapNews(nid, uid);
		
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "INITIALIZE SUCCESSFULLY.");
		map.put("CATEGORY", categories);
		map.put("SITE", sites);
		map.put("POPULAR", populars);
		map.put("NEWS", news);
		
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);	
	}
	
}

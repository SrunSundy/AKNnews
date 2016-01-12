package com.spring.akn.controller.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SaveListDTO;
import com.spring.akn.entities.SearchNewsDTO;
import com.spring.akn.services.NewsService;
import com.spring.akn.services.ScrapService;

@RestController
@RequestMapping("api/article")
public class NewsRestController {

	@Autowired
	NewsService newsservice;
	
	@Autowired
	ScrapService scrapservice;
	
	@RequestMapping(value="/{page}/{row}/{cid}/{sid}/{uid}/", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listNews(@PathVariable("page") int page,@PathVariable("row") int row,@PathVariable("cid") int cid
			,@PathVariable("sid") int sid,@PathVariable("uid") int uid){
		List<NewsDTO> news = newsservice.listNewsDatas(page,row, cid, sid, uid);
		System.err.println(news);
		Map<String, Object> map = new HashMap<String,Object>();
	
		if(news == null || news.isEmpty()){
			
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS NOT FOUND...");
			map.put("TOTAL_PAGES", newsservice.getNewsTotalPage("",row,cid,sid));
			map.put("TOTAL_RECORDS", newsservice.getNewsTotalRecords("",cid ,sid));
			map.put("RESPONSE_DATA",news);
			
			return new ResponseEntity<Map<String,Object>>
							(map,HttpStatus.OK);

		}	
	
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("TOTAL_PAGES", newsservice.getNewsTotalPage("",row,cid,sid));
		map.put("TOTAL_RECORDS", newsservice.getNewsTotalRecords("",cid ,sid));
		map.put("RESPONSE_DATA",news);
		
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	@RequestMapping(value="/{id}/{uid}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listNewData(@PathVariable("id") int id,@PathVariable("uid") int uid ){
		
		NewsDTO news= scrapservice.scrapNews(id, uid);
		System.err.println(news);
		Map<String, Object> map = new HashMap<String,Object>();
		if(news == null){
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "NEWS NOT FOUND...");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("RESPONSE_DATA", news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> searchNews(@RequestBody SearchNewsDTO search){
		List<NewsDTO> news = newsservice.searchNews(search);
		
		Map<String, Object> map = new HashMap<String,Object>();
		if(news == null || news.isEmpty()){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS NOT FOUND...");
			map.put("TOTAL_PAGES", newsservice.getNewsTotalPage(search.getKey(),search.getRow(),search.getCid(),search.getSid()));
			map.put("TOTAL_RECORDS", newsservice.getNewsTotalRecords(search.getKey(),search.getCid() ,search.getSid()));
			map.put("RESPONSE_DATA",news);
			return new ResponseEntity<Map<String,Object>>
							(map,HttpStatus.OK);

		}	
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("TOTAL_PAGES", newsservice.getNewsTotalPage(search.getKey(),search.getRow(),search.getCid(),search.getSid()));
		map.put("TOTAL_RECORDS", newsservice.getNewsTotalRecords(search.getKey(),search.getCid() ,search.getSid()));
		map.put("RESPONSE_DATA",news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	
	@RequestMapping(value="/popular/{uid}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getPopNews(@PathVariable("uid") int uid){
		List<NewsDTO> news = newsservice.getPopularNews(uid);
		
		Map<String, Object> map = new HashMap<String,Object>();
		if(news.isEmpty()){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS NOT FOUND...");
			map.put("RESPONSE_DATA",news);
			return new ResponseEntity<Map<String,Object>>
							(map,HttpStatus.OK);

		}	
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("RESPONSE_DATA",news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	@RequestMapping(value="/savelist", method= RequestMethod.POST )
	public ResponseEntity<Map<String,Object>> saveNews(@RequestBody SaveListDTO savenews){
		
		System.err.println("Save news");
		Map<String, Object> map  = new HashMap<String, Object>();
	
		if(newsservice.saveNews(savenews)>0){
			
			map.put("MESSAGE","NEWS HAS BEEN SAVED.");
			map.put("STATUS", HttpStatus.OK.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}else{
			map.put("MESSAGE","SAVE FAILS.");
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}
		
	}
	
	
	@RequestMapping(value="/savelist/{uid}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listSavedNews(@PathVariable("uid") int userid){
		List<NewsDTO> news = newsservice.listSavedNews(userid);
		Map<String, Object> map = new HashMap<String,Object>();
		if(news.isEmpty()){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NO SAVED NEWS...");
			map.put("RESPONSE_DATA",news);
			return new ResponseEntity<Map<String,Object>>
							(map,HttpStatus.OK);

		}	
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("RESPONSE_DATA",news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	@RequestMapping(value="/savelist/{id}/{uid}", method=RequestMethod.DELETE)
	public ResponseEntity<Map<String,Object>> deleteSavedNews(@PathVariable("id") int id,@PathVariable("uid") int uid ){
		System.err.println("delete Saved news");
		Map<String, Object> map  = new HashMap<String, Object>();
	
		if(newsservice.deleteSavedNews(id,uid)>0){
			
			map.put("MESSAGE","NEWS HAS BEEN DELETED.");
			map.put("STATUS", HttpStatus.OK.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}else{
			map.put("MESSAGE","DELETED FAILS.");
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}
	}
	
	
}

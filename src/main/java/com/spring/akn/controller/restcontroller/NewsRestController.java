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

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SearchNewsDTO;
import com.spring.akn.entities.frmApiDoc.FrmSaveListAdd;
import com.spring.akn.services.NewsService;
import com.spring.akn.services.ScrapService;

@RestController
@RequestMapping("api/article")
public class NewsRestController {

	@Autowired
	NewsService newsservice;
	
	@Autowired
	ScrapService scrapservice;
	
	@RequestMapping(value="/{page}/{row}/{categoryid}/{sourceid}/{userid}/", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listNews(@PathVariable("page") int page,@PathVariable("row") int row,@PathVariable("categoryid") int cid
			,@PathVariable("sourceid") int sid,@PathVariable("userid") int uid){
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
	
	
	
	@RequestMapping(value="/{newsid}/{userid}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listNewData(@PathVariable("newsid") int id,@PathVariable("userid") int uid ){
		
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
	
	@ApiIgnore
	@RequestMapping(value="/scrap",method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> scrapNews(){
		
		Map<String, Object> map  = new HashMap<String, Object>();
		int i=scrapservice.scrapAllSites();
		if(i>0){
			
			map.put("MESSAGE","NEWS HAS BEEN SCRAPING");
			map.put("NUMBER OF NEWS", i);
			map.put("STATUS", HttpStatus.OK.value());
			
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}else{
			map.put("MESSAGE","NO NEWS TO SCRAP");
			map.put("NUMBER OF NEWS", i);
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}
		
		
	
	}
	
	@ApiIgnore
	@RequestMapping(value="/",method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> insertNews(@RequestBody NewsDTO news){
		
		Map<String, Object> map  = new HashMap<String, Object>();
		
		if(newsservice.insertNews(news)>0){
			
			map.put("MESSAGE","NEWS HAS BEEN INSERTED.");
			map.put("STATUS", HttpStatus.OK.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}else{
			map.put("MESSAGE","INSERT FAILS.");
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}
		
	}
	
	
	@ApiIgnore
	@RequestMapping(value="/",method=RequestMethod.PUT)
	public ResponseEntity<Map<String,Object>> updateNews(@RequestBody NewsDTO news){
		
		Map<String, Object> map  = new HashMap<String, Object>();
		
		if(newsservice.updateNews(news)>0){
			
			map.put("MESSAGE","NEWS HAS BEEN UPDATED.");
			map.put("STATUS", HttpStatus.OK.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}else{
			map.put("MESSAGE","UPDATE FAILS.");
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}
		
	}
	
	@ApiIgnore
	@RequestMapping(value="/toggle/{newsid}",method=RequestMethod.PATCH)
	public ResponseEntity<Map<String,Object>> updateNewsStatus(@PathVariable("newsid") int newsid){
		
		Map<String, Object> map  = new HashMap<String, Object>();
		
		if(newsservice.toggleNews(newsid)>0){
			
			map.put("MESSAGE","NEWS STATUS HAS BEEN UPDATED.");
			map.put("STATUS", HttpStatus.OK.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}else{
			map.put("MESSAGE","UPDATE FAILS.");
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}
		
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
	
	
	@RequestMapping(value="/popular/{userid}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getPopNews(@PathVariable("userid") int uid){
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
	public ResponseEntity<Map<String,Object>> saveNews(@RequestBody FrmSaveListAdd savenews){
		
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
	
	@RequestMapping(value="/viewcount/{newsid}", method= RequestMethod.PATCH )
	public ResponseEntity<Map<String,Object>> updateView(@PathVariable("newsid") int newsid){
		
		Map<String, Object> map  = new HashMap<String, Object>();
	
		if(newsservice.updateView(newsid)>0){
			
			map.put("MESSAGE","Count success");
			map.put("STATUS", HttpStatus.OK.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}else{
			map.put("MESSAGE","Count fails");
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<Map<String,Object>>
								(map, HttpStatus.OK);
		}
		
	}
	
	
	@RequestMapping(value="/savelist/{userid}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listSavedNews(@PathVariable("userid") int userid){
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
	
	@RequestMapping(value="/savelist/{newsid}/{userid}", method=RequestMethod.DELETE)
	public ResponseEntity<Map<String,Object>> deleteSavedNews(@PathVariable("newsid") int id,@PathVariable("userid") int uid ){
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
	
	@RequestMapping(value="/scrap/", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> scrapAllSite(){
		
		System.err.println("scraping controller...");
		
		Map<String, Object> map  = new HashMap<String, Object>();
		try{
			int row_affected = scrapservice.scrapAllSites();
			System.out.println(row_affected);
			
			map.put("ROW_AFFECTED", row_affected);
			map.put("MESSAGE","NEWS HAS BEEN INSERTED.");
			map.put("STATUS", HttpStatus.OK.value());
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
			
		}catch(Exception ex){
			map.put("MESSAGE","SCRAP FAILED.");
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		}
	}
	
	
	
}

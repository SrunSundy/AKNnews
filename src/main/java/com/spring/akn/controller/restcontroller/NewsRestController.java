package com.spring.akn.controller.restcontroller;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.spring.akn.entities.NewsDTO;
import com.spring.akn.entities.SearchNewsDTO;
import com.spring.akn.entities.frmApiDoc.FrmSaveListAdd;
import com.spring.akn.services.CategoryServices;
import com.spring.akn.services.NewsService;
import com.spring.akn.services.ScrapService;
import com.spring.akn.services.SiteServices;
import com.spring.akn.services.UserServices;

@RestController
@RequestMapping("api/article")
public class NewsRestController {

	@Autowired
	NewsService newsservice;
	
	@Autowired
	ScrapService scrapservice;
	
	@Autowired
	private SiteServices siteServices;
	
	@Autowired
	UserServices userServices;
	
	@Autowired
	CategoryServices cateservice;
	
	@RequestMapping(value="/{page}/{row}/{categoryid}/{sourceid}/{userid}/", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listNews(@PathVariable("page") int page,@PathVariable("row") int row,@PathVariable("categoryid") int cid
			,@PathVariable("sourceid") int sid,@PathVariable("userid") int uid){
		List<NewsDTO> news = newsservice.listNewsDatas(page,row, cid, sid, uid);
		System.err.println(news);
		Map<String, Object> map = new HashMap<String,Object>();
	
		if(news == null || news.isEmpty()){
			
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS NOT FOUND...");
			map.put("TOTAL_PAGES", newsservice.getNewsTotalPage("",row,cid,sid,uid));
			map.put("TOTAL_RECORDS", newsservice.getNewsTotalRecords("",cid ,sid,uid));
			map.put("RESPONSE_DATA",news);
			
			return new ResponseEntity<Map<String,Object>>
							(map,HttpStatus.OK);

		}	
	
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("TOTAL_PAGES", newsservice.getNewsTotalPage("",row,cid,sid,uid));
		map.put("TOTAL_RECORDS", newsservice.getNewsTotalRecords("",cid ,sid,uid));
		map.put("RESPONSE_DATA",news);
		
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	@RequestMapping(value="/record/{categoryid}/{sourceid}/{userid}/", method=RequestMethod.GET)
	public  ResponseEntity<Map<String,Object>>  getNewsTotalRecord(@PathVariable("categoryid") int cid
			,@PathVariable("sourceid") int sid,@PathVariable("userid") int uid){
		Map<String, Object> map = new HashMap<String,Object>();
		
		int total = newsservice.getNewsTotalRecords("",cid ,sid,uid);
		if(total <=0){
			
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS NOT FOUND...");
			map.put("TOTAL_RECORDS", total);
			return new ResponseEntity<Map<String,Object>>
							(map,HttpStatus.OK);
		}	
	
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("TOTAL_RECORDS", total);
	
		
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
		updateView(id);
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("RESPONSE_DATA", news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	@RequestMapping(value="/statistic/{categoryid}/{siteid}/{day}/{row}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listNewsStatistic(@PathVariable("categoryid") int cid,@PathVariable("siteid") int sid,
			@PathVariable("day") int day, @PathVariable("row") int row){
			Map<String, Object> map = new HashMap<String,Object>();
			
			List<NewsDTO> newsstatistic = newsservice.listNewsStatistic(cid, sid, day, row);
			System.err.println("NEWS STATISTIC --> "+newsstatistic);
			if(newsstatistic.isEmpty()){
				map.put("STATUS", HttpStatus.OK.value());
				map.put("MESSAGE", "NEWS NOT FOUND...");
				map.put("RESPONSE_DATA",newsstatistic);
				
				return new ResponseEntity<Map<String,Object>>
								(map,HttpStatus.OK);
			}
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS HAS BEEN FOUND");
			map.put("RESPONSE_DATA",newsstatistic);
			
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);	
		
	}
	@ApiIgnore
	@RequestMapping(value="/dashboard", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAdminDashboard(){
		Map<String, Object> map = new HashMap<String,Object>();
		
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("NUM_SITE", siteServices.countSiteRecord());
		map.put("POP_NEWS", newsservice.getPopularNews(0, 30, 6));
		map.put("AKNLIST_NEWS",newsservice.listNewsDatas(1, 6, 0, 6, -1));
		map.put("TOTAL_NEWS",newsservice.getNewsTotalRecords("",0 ,0,-1));
		map.put("AKN_NEWS",newsservice.getNewsTotalRecords("",0 ,6,-1));
		map.put("SABAY_NEWS",newsservice.getNewsTotalRecords("",0 ,1,-1));
		map.put("BNEWS_NEWS",newsservice.getNewsTotalRecords("",0 ,5,-1));
		map.put("MUL_NEWS",newsservice.getNewsTotalRecords("",0 ,12,-1));
		
		map.put("NUM_CATE", cateservice.countCategoryRecord());
		
		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE", "USER FOUND");
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	@RequestMapping(value="/staticrecord/", method=RequestMethod.GET)
	public  ResponseEntity<Map<String,Object>>  getAdminDashboardStatistic(){
		Map<String, Object> map = new HashMap<String,Object>();
	
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("TOTAL_RECORDS_JAN", newsservice.getNewsRecordByMonth(1));
		map.put("TOTAL_RECORDS_FEB", newsservice.getNewsRecordByMonth(2));
		map.put("TOTAL_RECORDS_MAR", newsservice.getNewsRecordByMonth(3));
		map.put("TOTAL_RECORDS_APR", newsservice.getNewsRecordByMonth(4));
		map.put("TOTAL_RECORDS_MAY", newsservice.getNewsRecordByMonth(5));
		map.put("TOTAL_RECORDS_JUN", newsservice.getNewsRecordByMonth(6));
		map.put("TOTAL_RECORDS_JUL", newsservice.getNewsRecordByMonth(7));
		map.put("TOTAL_RECORDS_AUG", newsservice.getNewsRecordByMonth(8));
		map.put("TOTAL_RECORDS_SEP", newsservice.getNewsRecordByMonth(9));
		map.put("TOTAL_RECORDS_OCT", newsservice.getNewsRecordByMonth(10));
		map.put("TOTAL_RECORDS_NOV", newsservice.getNewsRecordByMonth(11));
		map.put("TOTAL_RECORDS_DEC", newsservice.getNewsRecordByMonth(12));

		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	@ApiIgnore
	@RequestMapping(value="/", method= RequestMethod.POST )
	public ResponseEntity<Map<String,Object>> insertNews(@RequestParam("json") String data, @RequestParam("file") MultipartFile file, HttpServletRequest request){
		Map<String, Object> map  = new HashMap<String, Object>();
		Gson g = new Gson();
		NewsDTO news = g.fromJson(data, NewsDTO.class);

		if(!file.isEmpty()){
			try{
				
	            String originalFilename = file.getOriginalFilename(); 
	            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
	            
	            String filename =UUID.randomUUID()+"."+extension;
	            System.out.println("Filename : " + filename);
				
	            byte[] bytes = file.getBytes();

				// creating the directory to store file
				String savePath = request.getSession().getServletContext().getRealPath("/resources/images/news/");
				System.out.println(savePath);
				File path = new File(savePath);
				if(!path.exists()){
					path.mkdir();
				}
				
				// creating the file on server
				File serverFile = new File(savePath + File.separator + filename );
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				
				System.out.println(serverFile.getAbsolutePath());
				System.out.println("You are successfully uploaded file " + filename);
				
				System.err.println("save news");
				news.setImage(filename);
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
			}catch(Exception e){
				System.out.println("You are failed to upload  => " + e.getMessage());
			}
		}else{
			System.err.println("File not found");
		}
		return null;
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
	

	@ApiIgnore
	@RequestMapping(value="/title/{newsid}",method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getNewsTitle(@PathVariable("newsid") int newsid){
		
		
		String news= newsservice.getNewsTitle(newsid);
		System.err.println(news);
		Map<String, Object> map = new HashMap<String,Object>();
		if(news == null){
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "NEWS TITLE NOT FOUND...");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS TITLE HAS BEEN FOUND");
		map.put("RESPONSE_DATA", news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	@ApiIgnore
	@RequestMapping(value="/title",method=RequestMethod.PUT)
	public ResponseEntity<Map<String,Object>> updateNewsTitle(@RequestBody NewsDTO news){
		
		Map<String, Object> map = new HashMap<String,Object>();
		if(newsservice.updateNewsTitle(news) > 0){
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "NEWS TITLE HAS BENN UPDATED...");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);
		}else{
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS TITLE UPDATE FAIL");
			map.put("RESPONSE_DATA", "SUCCESS");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);	
		}
		
	}
	
	@ApiIgnore
	@RequestMapping(value="/description/{newsid}",method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getNewsDescription(@PathVariable("newsid") int newsid){
		
		
		String news= newsservice.getNewsDescription(newsid);
		System.err.println(news);
		Map<String, Object> map = new HashMap<String,Object>();
		if(news == null){
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "NEWS DESCRIPTION NOT FOUND...");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS DESCRIPTION HAS BEEN FOUND");
		map.put("RESPONSE_DATA", news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	@ApiIgnore
	@RequestMapping(value="/description",method=RequestMethod.PUT)
	public ResponseEntity<Map<String,Object>> updateNewsDescription(@RequestBody NewsDTO news){
		
		Map<String, Object> map = new HashMap<String,Object>();
		if(newsservice.updateNewsDescription(news) > 0){
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "NEWS DESCRIPTION HAS BENN UPDATED...");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);
		}else{
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS DESCRIPTION UPDATE FAIL");
			map.put("RESPONSE_DATA", "SUCCESS");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);	
		}
		
	}
	
	@ApiIgnore
	@RequestMapping(value="/image/{newsid}",method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getNewsThumbnail(@PathVariable("newsid") int newsid){
		
		
		String news= newsservice.getNewsThumbnail(newsid);
		System.err.println(news);
		Map<String, Object> map = new HashMap<String,Object>();
		if(news == null){
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "NEWS THUMBNAIL NOT FOUND...");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS THUMBNAIL HAS BEEN FOUND");
		map.put("RESPONSE_DATA", news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	@ApiIgnore
	@RequestMapping(value="/image",method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> updateNews(@RequestParam("json") String data, @RequestParam("file") MultipartFile file, HttpServletRequest request){
		Gson g = new Gson();
		NewsDTO news = g.fromJson(data, NewsDTO.class);
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(!file.isEmpty()){
			try{
			
				String filename=news.getImage();
	            System.out.println("Filename : " + filename);
				
	            byte[] bytes = file.getBytes();

				// creating the directory to store file
				String savePath = request.getSession().getServletContext().getRealPath("/resources/images/news/");
				System.out.println(savePath);
				File path = new File(savePath);
				if(!path.exists()){
					path.mkdir();
				}
				
				// creating the file on server
				File serverFile = new File(savePath + File.separator + filename );
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				
				System.out.println(serverFile.getAbsolutePath());
				System.out.println("You are successfully uploaded file " + filename);
				map.put("MESSAGE", "UPLOAD IMAGE SUCCESS");
				map.put("STATUS", HttpStatus.OK.value());
				map.put("IMAGE", request.getContextPath() + "/images/" + filename);
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
				
			}catch(Exception e){
				System.out.println("You are failed to upload  => " + e.getMessage());
			}
		}else{
			System.err.println("File not found");
		}
		return null;
		
	}
	
	@ApiIgnore
	@RequestMapping(value="/content/{newsid}",method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getNewsContent(@PathVariable("newsid") int newsid){
		
		
		String news= newsservice.getNewsContent(newsid);
		System.err.println(news);
		Map<String, Object> map = new HashMap<String,Object>();
		if(news == null){
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "NEWS CONTENT NOT FOUND...");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS CONTENT HAS BEEN FOUND");
		map.put("RESPONSE_DATA", news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	@ApiIgnore
	@RequestMapping(value="/content",method=RequestMethod.PUT)
	public ResponseEntity<Map<String,Object>> updateNewsContent(@RequestBody NewsDTO news){
		
		Map<String, Object> map = new HashMap<String,Object>();
		if(newsservice.updateNewsContent(news) > 0){
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "NEWS CONTENT HAS BENN UPDATED...");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);
		}else{
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS CONTENT UPDATE FAIL");
			map.put("RESPONSE_DATA", "SUCCESS");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);	
		}
		
	}
	
	@ApiIgnore
	@RequestMapping(value="/newscategory",method=RequestMethod.PUT)
	public ResponseEntity<Map<String,Object>> updateNewsCategory(@RequestBody NewsDTO news){
		
		Map<String, Object> map = new HashMap<String,Object>();
		if(newsservice.updateNewsCategory(news) > 0){
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "NEWS CATEGORY HAS BENN UPDATED...");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);
		}else{
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS CATEGORY UPDATE FAIL");
			map.put("RESPONSE_DATA", "SUCCESS");
			return new ResponseEntity<Map<String,Object>>
										(map,HttpStatus.OK);	
		}
		
	}
	
	
	
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> searchNews(@RequestBody SearchNewsDTO search){
		System.err.println("SEARCHING CID: "+search.getCid()+"KEY: "+search.getKey() +"SITEID"+ search.getSid());
		List<NewsDTO> news = newsservice.searchNews(search);
		
		Map<String, Object> map = new HashMap<String,Object>();
		if(news == null || news.isEmpty()){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "NEWS NOT FOUND...");
			map.put("TOTAL_PAGES", newsservice.getNewsTotalPage(search.getKey(),search.getRow(),search.getCid(),search.getSid(),search.getUid()));
			map.put("TOTAL_RECORDS", newsservice.getNewsTotalRecords(search.getKey(),search.getCid() ,search.getSid(),search.getUid()));
			map.put("RESPONSE_DATA",news);
			return new ResponseEntity<Map<String,Object>>
							(map,HttpStatus.OK);

		}	
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("TOTAL_PAGES", newsservice.getNewsTotalPage(search.getKey(),search.getRow(),search.getCid(),search.getSid(),search.getUid()));
		map.put("TOTAL_RECORDS", newsservice.getNewsTotalRecords(search.getKey(),search.getCid() ,search.getSid(),search.getUid()));
		map.put("RESPONSE_DATA",news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
	
	
	@RequestMapping(value="/popular/{userid}/{day}/{row}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getPopNews(@PathVariable("userid") int uid,
			@PathVariable("day") int day,@PathVariable("row") int row){
		List<NewsDTO> news = newsservice.getPopularNews(uid,day,row);
		System.err.println("POP NEWS--> "+news);
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
	
	
	@RequestMapping(value="/savelist/{userid}/{row}/{page}/{day}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listSavedNews(@PathVariable("userid") int userid,
			@PathVariable("row") int row,@PathVariable("page") int page,@PathVariable("day") int day){
		List<NewsDTO> news = newsservice.listSavedNews(userid,row,page,day);
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
	
	/*@RequestMapping(value="/savelist/{userid}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listSavedNews(@PathVariable("userid") int userid){
		List<NewsDTO> news = newsservice.listSavedNews(userid, 10, 1);
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
	*/
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
	
	@RequestMapping(value="/scrap/{id}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> scrapSite(@PathVariable("id") int site_id){
		
		System.err.println("scraping controller...");
		
		Map<String, Object> map  = new HashMap<String, Object>();
		try{
			int row_affected = scrapservice.scrapSite(site_id);
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

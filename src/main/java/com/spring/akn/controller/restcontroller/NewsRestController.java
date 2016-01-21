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
	
	
	@ApiIgnore
	@RequestMapping(value="/",method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> insertNews(@RequestBody NewsDTO news){
	
		Map<String, Object> map  = new HashMap<String, Object>();
		System.err.println("save news");
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
	@RequestMapping(value="/insertupload", method= RequestMethod.POST )
	public ResponseEntity<Map<String,Object>> uploadImage( @RequestParam("file") MultipartFile file, HttpServletRequest request){
		Map<String, Object> map  = new HashMap<String, Object>();
		System.err.println("HELLO");
		if(!file.isEmpty()){
			try{
				
	            String originalFilename = file.getOriginalFilename(); 
	            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
	            
	            String filename =UUID.randomUUID()+"."+extension;
	            System.out.println("Filename : " + filename);
				
	            byte[] bytes = file.getBytes();

				// creating the directory to store file
				String savePath = request.getSession().getServletContext().getRealPath("/resources/images/");
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
				map.put("MESSAGE","UPLOAD IMAGE SUCCESS");
				map.put("STATUS", HttpStatus.OK.value());
				map.put("IMAGE", request.getContextPath() + "/images/" + filename);
				map.put("IMAGENAME", filename);
				
				return new ResponseEntity<Map<String,Object>>
									(map, HttpStatus.OK);
			}catch(Exception e){
				System.out.println("You are failed to upload  => " + e.getMessage());
			}
		}else{
			System.err.println("File not found");
		}
		return null;
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
	
	@ApiIgnore
	@RequestMapping(value="/detail/{newsid}",method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getANews(@PathVariable("newsid") int newsid){
		
		
		NewsDTO news= newsservice.listAData(newsid);
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

package com.spring.akn.controller.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.akn.entities.scrap.StructureDTO;
import com.spring.akn.entities.scrap.TestScrapDTO;
import com.spring.akn.services.ScrapService;

@RestController
@RequestMapping("api/scrap")
public class ScrapRestController {
	
	@Autowired
	ScrapService scrapService;
	
	@RequestMapping(value="/test", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> testScrap(@RequestBody StructureDTO selector){
		System.out.println(selector.getRowsSelector());
		ArrayList<TestScrapDTO> news = scrapService.testScrap(selector);
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("RESPONSE_DATA", news);

		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);	
	}
		
	@RequestMapping(value="/content", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> scrapContent(@RequestBody StructureDTO selector){
		System.out.println(selector.getContent());
		String content = scrapService.testScrapContent(selector);
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "NEWS HAS BEEN FOUND");
		map.put("CONTENT", content);

		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);	
	}//scrapSite
	
	@RequestMapping(value="/site/{id}", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> scrapSource(@PathVariable("id")int id){
		
		int effect = scrapService.scrapSite(id);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "SOURCE HAS BEEN EFFECT");
		map.put("CONTENT", effect);

		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);	
	}
	
	
	
}

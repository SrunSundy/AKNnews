package com.spring.akn.controller.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.spring.akn.entities.NewsDTO;
import com.spring.akn.services.NewsService;

@RestController
@RequestMapping("api/article")
public class NewsRestController {

	@Autowired
	NewsService newsservice;
	
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> listNews(){
		List<NewsDTO> news = newsservice.listNewsDatas(1, 0, 0, 0);
		Map<String, Object> map = new HashMap<String,Object>();
		if(news.isEmpty()){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "ARTICLE NOT FOUND...");
			map.put("RESPONSE_DATA",news);
			return new ResponseEntity<Map<String,Object>>
							(map,HttpStatus.OK);

		}	
		map.put("STATUS", HttpStatus.OK.value());
		map.put("MESSAGE", "ARITCLE HAS BEEN FOUND");
	//	map.put("ROW_COUNT", articleservice.getAllRow());
		map.put("RESPONSE_DATA",news);
		return new ResponseEntity<Map<String,Object>>
									(map,HttpStatus.OK);	
	}
}

package com.spring.akn.controller.restcontroller;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.internal.compiler.v2_0.functions.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gs.collections.impl.list.mutable.ArrayListAdapter;
import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.services.SiteDetailServices;

@RestController
@RequestMapping(value="/api/article/sitedetail")
public class SiteDetailRestController {
	@Autowired
	private SiteDetailServices siteDetailServices;
	
	/**
	 * Insert into sitedetail
	 * @param s_id
	 * @param c_id
	 * @param url
	 * @return
	 */
	@RequestMapping(value="/{s_id}/{c_id}/{url}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> insertSiteDetail(@PathVariable("s_id")int s_id,@PathVariable("c_id")int c_id,@PathVariable("url")String url){
		Map<String, Object> map = new Hashtable<String,Object>();
		if ( siteDetailServices.isInsertSiteDetail(s_id, c_id, url)){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE DETAIL INSERT SUCCESS!");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITEDETAIL INSERT FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Update sitedetail data
	 * @param s_id
	 * @param c_id
	 * @param url
	 * @return
	 */
	@RequestMapping(value="/{s_id}/{c_id}/{url}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateSiteDetail(@PathVariable("s_id")int s_id,@PathVariable("c_id")int c_id,@PathVariable("url")String url){
		Map<String, Object> map = new Hashtable<String,Object>();
		if ( siteDetailServices.isUpdateSiteDetail(s_id, c_id, url)){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE DETAIL UPDATE SUCCESS!");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITEDETAIL UPDATE FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Delete from sitedetail
	 * @param s_id
	 * @param c_id
	 * @return
	 */
	@RequestMapping(value="/{s_id}/{c_id}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deleteSiteDetail(@PathVariable("s_id")int s_id, @PathVariable("c_id")int c_id){
		Map<String, Object> map = new Hashtable<String,Object>();
		if ( siteDetailServices.isDeleteSiteDetail(s_id, c_id)){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE DETAIL DELETE SUCCESS!");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITEDETAIL DELETE FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}

}

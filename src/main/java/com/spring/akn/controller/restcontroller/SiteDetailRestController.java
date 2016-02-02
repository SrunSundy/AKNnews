package com.spring.akn.controller.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static java.lang.Math.toIntExact;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.spring.akn.entities.SiteDetailDTO;
import com.spring.akn.services.SiteDetailServices;
  
@RestController
@RequestMapping(value = "/api/article/scrapurl")//sitedetail
@ApiIgnore
public class SiteDetailRestController {
	@Autowired
	private SiteDetailServices siteDetailServices;

	/**
	 * Insert into sitedetail
	 * @return
	 * @throws ParseException
	 */

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> insertSiteDetail(@RequestBody SiteDetailDTO siteDetail){
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (siteDetailServices.isInsertSiteDetail( siteDetail )) {
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE DETAIL INSERT SUCCESS!");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		//}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE DETAIL INSERT FAIL!");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	/**
	 * Get all site detail
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listSiteDetail() throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();

		if (!siteDetailServices.listSiteDetail().isEmpty()) {
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE DETAIL LIST DATA SUCCESS!");
			map.put("DATA", siteDetailServices.listSiteDetail() );
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}

		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITED ETAIL LIST DATA  FAIL!");
		map.put("DATA", siteDetailServices.listSiteDetail() );
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	/**
	 * List crap url with page
	 * @param limit
	 * @param page
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/page/{limit}/{page}/{site_id}/{category_id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listSiteDetailPage(@PathVariable("limit")int limit ,@PathVariable("page") int page,
			@PathVariable("site_id")int s_id, @PathVariable("category_id")int c_id ) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SiteDetailDTO> lst = siteDetailServices.listSiteDetailPage(limit, page ,s_id, c_id);
		if (!lst.isEmpty()) {
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE DETAIL LIST DATA SUCCESS!");
			map.put("DATA", lst );
			map.put("TOTALRECORD", siteDetailServices.countRecord());
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}

		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITED ETAIL LIST DATA  FAIL!");
		map.put("DATA", lst );
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}


	/**
	 * Update sitedetail data
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateSiteDetail(@RequestBody SiteDetailDTO siteDetail){
		Map<String, Object> map = new HashMap<String, Object>();

		if (siteDetailServices.isUpdateSiteDetail( siteDetail )) {
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE DETAIL UPDATE SUCCESS!");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITEDETAIL UPDATE FAIL!");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	/**
	 * Delete from sitedetail
	 * @param s_id
	 * @param c_id
	 * @return
	 */
	@RequestMapping(value = "/{s_id}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deleteSiteDetail(@PathVariable("s_id") int cid) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (siteDetailServices.isDeleteSiteDetail( cid )) {
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE DETAIL DELETE SUCCESS!");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITEDETAIL DELETE FAIL! BECAUSE STATUS TRUE");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	/**
	 * find object site detail
	 * @param s_id
	 * @param c_id
	 * @return
	 */
	@RequestMapping(value = "/{s_id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> findSiteAndCategoryById(@PathVariable("s_id") int s_id ) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (siteDetailServices.findSiteAndCategoryById ( s_id ) != null) {
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE DETAIL GET SUCCESS!");
			map.put("DATA", siteDetailServices.findSiteAndCategoryById( s_id ));
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE DETAIL GET FAIL!");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	/**
	 * Toggle status change
	 * @param s_id
	 * @param c_id
	 * @return
	 */
	@RequestMapping(value="/toggle/{s_id}", method = RequestMethod.PATCH)
	public ResponseEntity<Map<String, Object>> toggleStatusSiteDetail(@PathVariable("s_id")int cid ){
		Map<String, Object> map = new HashMap<String, Object>();
		if (siteDetailServices.isToggleStatusSiteDetail( cid ) ) {
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE DETAIL CHANGE STATUS SUCCESS!");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE DETAIL CHANGE STATUS FAIL!");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);		
	}
	


}

package com.spring.akn.controller.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.analysis.miscellaneous.StemmerOverrideFilter;
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
import com.spring.akn.entities.SiteDTO;
import com.spring.akn.services.SiteServices;

@RestController
@RequestMapping(value="/api/article/site")
public class SiteRestController {
	@Autowired
	private SiteServices siteServices;
	
	/**
	 * List all tbsite
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listSite(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<SiteDTO> lst = siteServices.listSite();
		if ( !lst.isEmpty() ) {
			map.put("STATUS", HttpStatus.FOUND.value());
			map.put("MESSAGE", "SITE LIST FOUND!" );
			map.put("DATA", lst);
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE LIST NOT FOUND!" );
		map.put("DATA", lst);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	/**
	 * Find tbsite unique with id
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> findSiteById(@PathVariable("id") int id){
		SiteDTO siteDTO = siteServices.findSiteById(id);
		Map<String, Object> map = new HashMap<String,Object>();
		if ( siteDTO != null) {
			map.put("STATUS", HttpStatus.FOUND.value());
			map.put("MESSAGE", "SITE ID "+id+" FOOUND!");
			map.put("DATA", siteDTO);
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE ID "+id+" NOT FOOUND!");
		map.put("DATA", "");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Delete tbsite by id
	 * @param id
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deleteSiteById(@PathVariable("id") int id){
		Map<String, Object> map = new HashMap<String,Object>();
		
		if ( siteServices.checkExistSite(id) == null ){
			if ( siteServices.isDeleteSiteById(id) ){
				map.put("STATUS", HttpStatus.OK.value());
				map.put("MESSAGE", "SITE ID "+id+" DELETE SUCCESS!");
				return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
			}
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE ID "+id+" DELETE FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Insert data into tbsite by object
	 * @param siteDTO
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value="/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> insertSite(@RequestBody SiteDTO siteDTO){
		Map<String, Object> map = new HashMap<String,Object>();
		if ( siteServices.isInsertSite(siteDTO) ){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE INSERT SUCCESS!");
		//	map.put("SITE_ID", siteServices.getSiteSequence());
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE INSERT FAIL!");
		//map.put("SITE_ID", siteServices.getSiteSequence());
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Update tbsite data by using object
	 * @param siteDTO
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value="/", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateSite(@RequestBody SiteDTO siteDTO){
		Map<String, Object> map = new HashMap<String,Object>();
		if ( siteServices.isUpdateSite(siteDTO) ){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE UPDATE SUCCESS!");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE UPDATE FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}/{basepath}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateBasePath(@PathVariable("id")int id ,@PathVariable("basepath")String basepath){
		Map<String, Object> map = new HashMap<String,Object>();
		if ( siteServices.updateSiteBasePath(id, basepath) ){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE BASEPATH UPDATE SUCCESS!");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE BASEPATH UPDATE FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Count total record in tbsite
	 * @return
	 */
	@RequestMapping(value="/record", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> countSiteRecord(){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE", "SITE COUNT SUCCESS!");
		map.put("DATA", siteServices.countSiteRecord());
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	

	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> uploadSiteLogo( @RequestParam("file") MultipartFile file, HttpServletRequest request,
			@RequestParam("id")int s_id ){
		Map<String, Object> map = new HashMap<String,Object>();

		if ( siteServices.isUploadLogo(file, request, s_id) ){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE UPDATE SUCCESS!");
			map.put("IMAGE", siteServices.getLogoPath(request, s_id));
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE UPDATE FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	@RequestMapping(value="/ex", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> ext(){
		Map<String, Object> map = new HashMap<String, Object>();
		if (siteServices.checkExistSite(100) != null) {
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "SITE not null SUCCESS!");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "SITE null FAIL!");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);		
	}
	
	

	
}

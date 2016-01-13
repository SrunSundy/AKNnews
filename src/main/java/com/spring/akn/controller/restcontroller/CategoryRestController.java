package com.spring.akn.controller.restcontroller;


import java.util.HashMap;
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
import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.services.CategoryServices;

@RestController
@RequestMapping(value="/api/article/category")
public class CategoryRestController {
	
	@Autowired
	private CategoryServices categoryServices;
	
	/**
	 * Get all category data
	 * @return
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listCategory(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<CategoryDTO> lst = categoryServices.listCategory();
		if ( !lst.isEmpty() ) {
			map.put("STATUS", HttpStatus.FOUND.value());
			map.put("MESSAGE", "CATEGORY LIST FOUND!" );
			map.put("DATA", lst);
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "CATEGORY LIST NOT FOUND!" );
		map.put("DATA", lst);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	/**
	 * Get all category data have news data
	 * @return
	 */
	@RequestMapping(value="/1", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listCategoryHaveNews(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<CategoryDTO> lst = categoryServices.listCategoryHaveNews();
		if ( !lst.isEmpty() ) {
			map.put("STATUS", HttpStatus.FOUND.value());
			map.put("MESSAGE", "CATEGORY LIST FOUND!" );
			map.put("DATA", lst);
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "CATEGORY LIST NOT FOUND!" );
		map.put("DATA", lst);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	/**
	 * Find category object by id
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> findCategoryById(@PathVariable("id") int id){
		CategoryDTO categoryDTO = categoryServices.findCategoryById(id);
		Map<String, Object> map = new HashMap<String,Object>();
		if ( categoryDTO != null) {
			map.put("STATUS", HttpStatus.FOUND.value());
			map.put("MESSAGE", "CATEGORY ID "+id+" FOOUND!");
			map.put("DATA", categoryDTO);
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "CATEGORY ID "+id+" NOT FOOUND!");
		map.put("DATA", "");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Delete category by id
	 * @param id
	 * @return
	 */
	
	@ApiIgnore
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deleteCategoryById(@PathVariable("id") int id){
		Map<String, Object> map = new HashMap<String,Object>();
		if ( categoryServices.isDeleteCategoryById(id) ){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "CATEGORY ID "+id+" DELETE SUCCESS!");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "CATEGORY ID "+id+" DELETE FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Insert Category data
	 * @param categoryDTO
	 * @return
	 */
	
	@ApiIgnore
	@RequestMapping(value="/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> insertCategory(@RequestBody CategoryDTO categoryDTO){
		Map<String, Object> map = new HashMap<String,Object>();
		if ( categoryServices.isInsertCategory(categoryDTO) ){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "CATEGORY INSERT SUCCESS!");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "CATEGORY INSERT FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Update category data
	 * @param categoryDTO
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value="/", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateCategory(@RequestBody CategoryDTO categoryDTO){
		Map<String, Object> map = new HashMap<String,Object>();
		if ( categoryServices.isUpdateCategory(categoryDTO) ){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "CATEGORY UPDATE SUCCESS!");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "CATEGORY UPDATE FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Count category record
	 * @return
	 */
	@RequestMapping(value="/record", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> countCategoryRecord(){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE", "CATEGORY COUNT SUCCESS!");
		map.put("DATA", categoryServices.countCategoryRecord());
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	/**
	 * Change isMenu by toogle (if true to false if false to true)
	 * @param id
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value="/toggle/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Map<String, Object>> toggleCategory(@PathVariable("id") int id){
		Map<String, Object> map = new HashMap<String,Object>();
		if ( categoryServices.isMenuToggle(id) ){
			map.put("STATUS", HttpStatus.OK.value());
			map.put("MESSAGE", "CATEGORY CHANGE SET MENU SUCCESS!");
			return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "CATEGORY CHANGE SET MENU FAIL!");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	
		
}

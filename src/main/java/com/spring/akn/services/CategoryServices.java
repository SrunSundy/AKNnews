package com.spring.akn.services;

import java.util.List;

import com.spring.akn.entities.CategoryDTO;

public interface CategoryServices {
	// list all category
	public List<CategoryDTO> listCategory();
	// find one category by id
	public CategoryDTO findCategoryById(int id);
	// delete category record
	public boolean isDeleteCategoryById(int id);
	// insert object category
	public boolean isInsertCategory(CategoryDTO categoryDTO);
	// update object category condition with id
	public boolean isUpdateCategory(CategoryDTO categoryDTO);
	// change status category for top menu true or false 
	public boolean isMenuToggle(int id);
	// count all record of category 
	public int countCategoryRecord();
	// list all category that have news
	public List<CategoryDTO> listCategoryHaveNews();
	//check category exist in news or not
	public CategoryDTO checkExistCategory(int id);
}

package com.spring.akn.repositories;

import java.util.List;

import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.entities.SiteDTO;

public interface CategoryDAO {
	
	public List<CategoryDTO> listCategory();
	public CategoryDTO findCategoryById(int id);
	public boolean isDeleteCategoryById(int id);
	public boolean isInsertCategory(CategoryDTO categoryDTO);
	public boolean isUpdateCategory(CategoryDTO categoryDTO);
	public boolean isMenuToggle(int id);
	public int countCategoryRecord();
	public List<CategoryDTO> listCategoryHaveNews();
	public CategoryDTO checkExistCategory(int id);
}

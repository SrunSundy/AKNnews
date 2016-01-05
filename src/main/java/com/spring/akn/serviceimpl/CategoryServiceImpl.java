package com.spring.akn.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.akn.entities.CategoryDTO;
import com.spring.akn.repositories.CategoryDAO;
import com.spring.akn.services.CategoryServices;

@Service
public class CategoryServiceImpl implements CategoryServices{
	
	@Autowired 
	private CategoryDAO categoryDAO;
	public List<CategoryDTO> listCategory() {
		return categoryDAO.listCategory();
	}

	public CategoryDTO findCategoryById(int id) {
		return categoryDAO.findCategoryById(id);
	}

	public boolean isDeleteCategoryById(int id) {
		return categoryDAO.isDeleteCategoryById(id);
	}

	public boolean isInsertCategory(CategoryDTO categoryDTO) {
		return categoryDAO.isInsertCategory(categoryDTO);
	}

	public boolean isUpdateCategory(CategoryDTO categoryDTO) {
		return categoryDAO.isUpdateCategory(categoryDTO);
	}

	public boolean isMenuToggle(int id) {
		return categoryDAO.isMenuToggle(id);
	}

	public int countCategoryRecord() {
		return categoryDAO.countCategoryRecord();
	}
	
}

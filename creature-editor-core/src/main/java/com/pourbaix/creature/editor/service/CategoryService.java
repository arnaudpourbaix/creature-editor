package com.pourbaix.creature.editor.service;

import java.util.List;

import com.pourbaix.creature.editor.domain.Category;

public interface CategoryService {

	List<Category> getList();
	
    Category findById(int id);
    
    void saveCategory(Category category);
    
    void deleteCategory(int id);
    
    List<Category> findCategories(String name);
}

package com.pourbaix.creature.editor.dao;

import java.util.List;

import com.pourbaix.creature.editor.domain.Category;

public interface CategoryDao extends AbstractDao<Category, Integer> {
	
	List<Category> getList();
	
    List<Category> findCategories(String name);
    
}

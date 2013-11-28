package com.pourbaix.creature.editor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pourbaix.creature.editor.dao.CategoryDao;
import com.pourbaix.creature.editor.domain.Category;
import com.pourbaix.creature.editor.service.CategoryService;

@Service("categoryService")
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public List<Category> getList() {
        return categoryDao.getList();
    }
    
    public Category findById(int id) {
        return categoryDao.findById(id);
    }

    @Transactional(readOnly = false)
    public void saveCategory(Category category) {
    	categoryDao.saveOrUpdate(category);
    }

    @Transactional(readOnly = false)
    public void deleteCategory(int id) {
    	Category category = categoryDao.findById(id);
        if (category != null) {
        	categoryDao.delete(category);
        }
    }

    public List<Category> findCategories(String name) {
        return categoryDao.findCategories(name);
    }
}

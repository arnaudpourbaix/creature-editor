package com.pourbaix.creature.editor.category;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.pourbaix.creature.editor.domain.Category;
import com.pourbaix.creature.editor.service.CategoryService;

@Controller
public class CategoryController {

	static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    private static final ConcurrentSkipListMap<Long, Category> categoryRepository = new ConcurrentSkipListMap<Long, Category>();
    
    @Autowired
    private CategoryService categoryService;
    

//    @RequestMapping(value = "/category", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody List<Category> list() {
//    	List<Category> categories = categoryService.getList();
//    	return categories;
//    }

    @Transactional
    @RequestMapping(value = "/category", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<CategoryDTO> list() {
    	List<Category> categories = categoryService.getList();
    	List<CategoryDTO> result = Lists.transform(categories, new Function<Category, CategoryDTO>() {
			@Override
			public CategoryDTO apply(Category input) {
				return new CategoryDTO(input);
			}
    	});
    	return result;
    }
    
    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Category getById(@PathVariable long id) {
        return categoryRepository.get(id);
    }

    @RequestMapping(value = "/category", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestBody Category category) {
//        long id = categoryRepository.incrementAndGet();
//        category.setId(id);
        categoryRepository.put(1L, category);
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
    	categoryRepository.remove(id);
    }

}

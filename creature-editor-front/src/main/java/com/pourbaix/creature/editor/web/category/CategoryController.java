package com.pourbaix.creature.editor.web.category;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pourbaix.creature.editor.domain.Category;
import com.pourbaix.creature.editor.repository.CategoryRepository;

@RestController
public class CategoryController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryRepository categoryRepository;

	@RequestMapping(value = "/category", method = RequestMethod.GET, produces = "application/json")
	public List<Category> list() {
		List<Category> categories = categoryRepository.findAllFetchParent();
		return categories;
	}

	@RequestMapping(value = "/category/{id}", method = RequestMethod.GET, produces = "application/json")
	public Category getById(@PathVariable Integer id) {
		return categoryRepository.findOne(id);
	}

	@RequestMapping(value = "/category/name/{name}", method = RequestMethod.GET, produces = "application/json")
	public Category getByName(@PathVariable String name) {
		Category category = categoryRepository.findByName(name);
		return category;
	}

	@RequestMapping(value = "/category", method = RequestMethod.PUT, produces = "application/json")
	public Category save(@RequestBody Category category) {
		categoryRepository.save(category);
		return category;
	}

	@RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		categoryRepository.delete(id);
	}

}

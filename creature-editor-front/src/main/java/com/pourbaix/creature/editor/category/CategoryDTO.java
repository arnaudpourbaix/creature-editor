package com.pourbaix.creature.editor.category;

import com.pourbaix.creature.editor.domain.Category;

public class CategoryDTO {

	private Integer id;
	private String name;
	private Integer parentId;
	
	public CategoryDTO() {
		// default constructor
	}
	
	public CategoryDTO(Category category) {
		this.id = category.getId();
		this.name = category.getName();
		if (category.getParent() != null) {
			this.parentId = category.getParent().getId();
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
}

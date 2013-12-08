package com.pourbaix.creature.editor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pourbaix.creature.editor.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	@Query("select c from Category c left join fetch c.parent order by name asc")
	public List<Category> findAllFetchParent();

}

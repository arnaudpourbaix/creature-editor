package com.pourbaix.creature.editor.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.pourbaix.creature.editor.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select c from Category c left join fetch c.parent order by name asc")
	public List<Category> findAllFetchParent();

	@Query("select c from Category c where upper(c.name) = upper(:name)")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public Category findByName(@Param("name") String name);

}

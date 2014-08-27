package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Integer> {

	@EntityGraph(value = "Keyword.lists", type = EntityGraphType.LOAD)
	Keyword findByName(String name);

}

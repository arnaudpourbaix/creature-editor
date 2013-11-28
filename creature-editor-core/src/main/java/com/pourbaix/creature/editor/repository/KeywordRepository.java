package com.pourbaix.creature.editor.repository;

import org.springframework.data.repository.CrudRepository;

import com.pourbaix.creature.editor.domain.Keyword;

public interface KeywordRepository extends CrudRepository<Keyword, Integer> {

	Keyword findByName(String name);

}

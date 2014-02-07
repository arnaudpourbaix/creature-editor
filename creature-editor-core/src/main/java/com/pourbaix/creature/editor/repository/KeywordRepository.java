package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Integer> {

	Keyword findByName(String name);

}

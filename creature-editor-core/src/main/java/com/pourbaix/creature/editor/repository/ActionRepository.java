package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.Action;

public interface ActionRepository extends JpaRepository<Action, String> {

	Action findByName(String name);

}

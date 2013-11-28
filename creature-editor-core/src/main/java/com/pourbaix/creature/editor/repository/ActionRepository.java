package com.pourbaix.creature.editor.repository;

import org.springframework.data.repository.CrudRepository;

import com.pourbaix.creature.editor.domain.Action;

public interface ActionRepository extends CrudRepository<Action, String> {

	Action findByName(String name);

}

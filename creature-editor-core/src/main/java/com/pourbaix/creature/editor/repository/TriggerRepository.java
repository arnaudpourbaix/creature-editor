package com.pourbaix.creature.editor.repository;

import org.springframework.data.repository.CrudRepository;

import com.pourbaix.creature.editor.domain.Trigger;

public interface TriggerRepository extends CrudRepository<Trigger, String> {

	Trigger findByName(String name);

}

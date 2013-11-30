package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.Trigger;

public interface TriggerRepository extends JpaRepository<Trigger, String> {

	Trigger findByName(String name);

}

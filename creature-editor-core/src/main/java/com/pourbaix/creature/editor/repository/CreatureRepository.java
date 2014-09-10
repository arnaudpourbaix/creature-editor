package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.Creature;

public interface CreatureRepository extends JpaRepository<Creature, Integer> {

}

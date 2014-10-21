package com.pourbaix.creature.editor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.Attribute;
import com.pourbaix.creature.editor.domain.AttributeValue;
import com.pourbaix.creature.editor.domain.Creature;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Integer> {
	
	public List<AttributeValue> findByAttributeAndCreature(Attribute attribute, Creature creature);

}

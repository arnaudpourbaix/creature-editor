package com.pourbaix.creature.editor.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import com.pourbaix.creature.editor.domain.Attribute;
import com.pourbaix.creature.editor.domain.AttributeValue;
import com.pourbaix.creature.editor.domain.Creature;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Integer> {
	
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public List<AttributeValue> findByAttributeAndCreature(Attribute attribute, Creature creature);

}

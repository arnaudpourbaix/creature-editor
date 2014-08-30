package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.Attribute;
import com.pourbaix.creature.editor.domain.AttributeEnum;

public interface AttributeRepository extends JpaRepository<Attribute, AttributeEnum> {
	

}

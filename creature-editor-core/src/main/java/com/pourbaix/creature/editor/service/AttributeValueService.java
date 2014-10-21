package com.pourbaix.creature.editor.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.pourbaix.creature.editor.domain.Attribute;
import com.pourbaix.creature.editor.domain.AttributeEnum;
import com.pourbaix.creature.editor.domain.AttributeValue;
import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.domain.StateEnum;
import com.pourbaix.creature.editor.repository.AttributeRepository;
import com.pourbaix.creature.editor.repository.AttributeValueRepository;

@Service
public class AttributeValueService {

	@Resource
	private AttributeRepository attributeRepository;
	
	@Resource
	private AttributeValueRepository attributeValueRepository;
	

	public AttributeValue getCurrentAttributeValue(Creature creature, AttributeEnum name) {
		Attribute attribute = attributeRepository.findOne(name);
		List<AttributeValue> values = attributeValueRepository.findByAttributeAndCreature(attribute, creature);
		if (CollectionUtils.isEmpty(values)) {
			return null;
		}
		AttributeValue value = getAttributeValueByState(values, StateEnum.MODIFIED);
		if (value == null) {
			value = getAttributeValueByState(values, StateEnum.ORIGINAL);
		}
		return value;
	}
	
	private AttributeValue getAttributeValueByState(List<AttributeValue> values, final StateEnum state) {
		Optional<AttributeValue> value = Iterables.tryFind(values, new Predicate<AttributeValue>() {
			public boolean apply(AttributeValue value) {
				return value.getState() == state;
			}
		});
		return value.isPresent() ? value.get() : null;
	}

}

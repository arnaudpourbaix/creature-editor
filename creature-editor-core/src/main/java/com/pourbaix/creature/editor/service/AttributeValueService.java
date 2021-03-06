package com.pourbaix.creature.editor.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	public Set<AttributeValue> getCurrentAttributeValues(Creature creature, Set<AttributeEnum> attributeNames) {
		Set<AttributeValue> attributeValues = new HashSet<AttributeValue>(attributeNames.size());
		for (AttributeEnum attributeName : attributeNames) {
			attributeValues.add(getCurrentAttributeValue(creature, attributeName));
		}
		return attributeValues;
	}
	
	public AttributeValue getCurrentAttributeValue(Creature creature, AttributeEnum attributeName) {
		Attribute attribute = attributeRepository.findOne(attributeName);
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

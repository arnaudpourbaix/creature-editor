package com.pourbaix.infinity.factory;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.Attribute;
import com.pourbaix.creature.editor.domain.AttributeEnum;
import com.pourbaix.creature.editor.domain.AttributeValue;
import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.domain.GameEditionEnum;
import com.pourbaix.creature.editor.domain.StateEnum;
import com.pourbaix.creature.editor.repository.AttributeRepository;
import com.pourbaix.creature.editor.repository.GameEditionRepository;

@Service
public class AttributeFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AttributeFactory.class);
	
	@Resource
	private AttributeRepository attributeRepository;

	@Resource
	private GameEditionRepository gameEditionRepository;
	
	public AttributeValue getAttributeValue(Creature creature, AttributeEnum id, String value) {
		AttributeValue attributeValue = getAttributeValue(creature, id);
		attributeValue.setStringValue(value);
		return attributeValue;
	}

	public AttributeValue getAttributeValue(Creature creature, AttributeEnum id, long value) {
		AttributeValue attributeValue = getAttributeValue(creature, id);
		attributeValue.setNumberValue(value);
		return attributeValue;
	}
	
	private AttributeValue getAttributeValue(Creature creature, AttributeEnum id) {
		Attribute attribute = attributeRepository.findOne(id);
		AttributeValue attributeValue = new AttributeValue();
		attributeValue.setCreature(creature);
		attributeValue.setAttribute(attribute);
		attributeValue.setState(StateEnum.ORIGINAL);
		attributeValue.setGameEdition(gameEditionRepository.findOne(GameEditionEnum.SECOND_EDITION));
		return attributeValue;
	}
	
}

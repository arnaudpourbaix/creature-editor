package com.pourbaix.creature.editor.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.AttributeEnum;
import com.pourbaix.creature.editor.domain.AttributeValue;
import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.repository.AttributeRepository;
import com.pourbaix.creature.editor.repository.CreatureRepository;

@Service
public class CreatureService {

	@Resource
	private CreatureRepository creatureRepository;

	@Resource
	private AttributeRepository attributeRepository;
	
	@Resource
	private AttributeValueService attributeValueService;
	
	public Creature getById(Integer id) {
		return creatureRepository.findOne(id);
	}
	
	public Creature save(Creature creature) {
		creatureRepository.save(creature);
		return creature;
	}
	
	public void delete(Integer id) {
		creatureRepository.delete(id);
	}

	public List<Creature> list() {
		List<Creature> creatures = creatureRepository.findAll();
		for (Creature creature : creatures) {
			Set<AttributeValue> attributeValues = new HashSet<AttributeValue>(2);
			attributeValues.add(attributeValueService.getCurrentAttributeValue(creature, AttributeEnum.NAME));
			attributeValues.add(attributeValueService.getCurrentAttributeValue(creature, AttributeEnum.TOOLTIP));
			creature.setAttributeValues(attributeValues);
		}
		return creatures;
	}

}

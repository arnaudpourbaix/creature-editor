package com.pourbaix.creature.editor.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableSet;
import com.pourbaix.creature.editor.domain.AttributeEnum;
import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.domain.Game;
import com.pourbaix.creature.editor.domain.Mod;
import com.pourbaix.creature.editor.repository.AttributeRepository;
import com.pourbaix.creature.editor.repository.CreatureRepository;

@Service
public class CreatureService {

	private static final Logger logger = LoggerFactory.getLogger(CreatureService.class);
	
	@Resource
	private CreatureRepository creatureRepository;

	@Resource
	private AttributeRepository attributeRepository;
	
	@Resource
	private AttributeValueService attributeValueService;
	
	
	public static final ImmutableSet<AttributeEnum> creatureListAttributes = ImmutableSet.of(AttributeEnum.NAME);
	
	public Creature getById(Integer id) {
		return creatureRepository.findOne(id);
	}

	public Creature getById(Integer id, boolean loadAll) {
		if (loadAll) {
			return creatureRepository.getCreatureById(id);
		} else {
			return creatureRepository.findOne(id);
		}
	}
	
	public Creature save(Creature creature) {
		creatureRepository.save(creature);
		return creature;
	}

	public void save(Iterable<Creature> creatures) {
		creatureRepository.save(creatures);
	}
	
	public List<Creature> list() {
		List<Creature> creatures = creatureRepository.findAll();
		logger.debug("list - START");
//		for (Creature creature : creatures) {
//			logger.debug("-start-");
//			Set<AttributeValue> attributeValues = attributeValueService.getCurrentAttributeValues(creature, creatureListAttributes);
//			creature.setAttributeValues(attributeValues);
//		}
		logger.debug("list - END");
		return creatures;
	}

	public void delete(Integer id) {
		creatureRepository.delete(id);
	}

	public void deleteAll() {
		creatureRepository.deleteAll();
	}
	
	public void deleteByResourceAndGameAndMod(String resource, Game game, Mod mod) {
		List<Creature> creatures = creatureRepository.findByResourceAndGameAndMod(resource, game, mod);
		for (Creature creature : creatures) {
			creatureRepository.delete(creature);
		}
		
	}

}

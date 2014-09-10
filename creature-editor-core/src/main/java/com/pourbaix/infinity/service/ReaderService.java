package com.pourbaix.infinity.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.infinity.context.ReaderContext;
import com.pourbaix.infinity.domain.IdentifierFile;
import com.pourbaix.infinity.factory.CreatureFactory;
import com.pourbaix.infinity.factory.FactoryException;
import com.pourbaix.infinity.factory.IdentifierFactory;
import com.pourbaix.infinity.factory.SpellFactory;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;

@Service
public class ReaderService {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ReaderService.class);

	@Resource
	private ReaderContext readerContext;

	@Resource
	private GameService gameService;

	@Resource
	private SpellFactory spellFactory;

	@Resource
	private CreatureFactory creatureFactory;

	@Resource
	private IdentifierFactory identifierFactory;

	public List<ResourceEntry> getSpellResources() throws ServiceException {
		return Keyfile.getInstance().getResourceEntriesByExtension("spl");
	}

	public List<Spell> getSpells() throws ServiceException {
		try {
			List<Spell> spells = new ArrayList<>();
			for (ResourceEntry entry : Keyfile.getInstance().getResourceEntriesByExtension("spl")) {
				Spell spell = spellFactory.getSpell(entry);
				spells.add(spell);
			}
			return spells;
		} catch (FactoryException e) {
			throw new ServiceException(e.getCode(), e.getParam());
		}
	}

	public Spell getSpell(ResourceEntry entry) throws ServiceException {
		try {
			Spell spell = spellFactory.getSpell(entry);
			return spell;
		} catch (FactoryException e) {
			throw new ServiceException(e.getCode(), e.getParam());
		}
	}

	public Spell getSpell(String resource) throws ServiceException {
		try {
			Spell spell = spellFactory.getSpell(resource);
			return spell;
		} catch (FactoryException e) {
			throw new ServiceException(e.getCode(), e.getParam());
		}
	}

	public List<Creature> getCreatures() throws ServiceException {
		try {
			List<Creature> creatures = new ArrayList<>();
			for (ResourceEntry entry : Keyfile.getInstance().getResourceEntriesByExtension("cre")) {
				Creature creature = creatureFactory.getCreature(entry);
				creatures.add(creature);
			}
			return creatures;
		} catch (FactoryException e) {
			throw new ServiceException(e.getCode(), e.getParam());
		}
	}

	public Creature getCreature(String resource) throws ServiceException {
		try {
			Creature creature = creatureFactory.getCreature(resource);
			return creature;
		} catch (FactoryException e) {
			throw new ServiceException(e.getCode(), e.getParam());
		}
	}

	public Creature getCreature(ResourceEntry entry) throws ServiceException {
		try {
			Creature creature = creatureFactory.getCreature(entry);
			return creature;
		} catch (FactoryException e) {
			throw new ServiceException(e.getCode(), e.getParam());
		}
	}
	
	public List<IdentifierFile> getIdentifierFiles() throws ServiceException {
		List<IdentifierFile> ids = new ArrayList<>();
		for (ResourceEntry entry : Keyfile.getInstance().getResourceEntriesByExtension("ids")) {
			try {
				IdentifierFile id = identifierFactory.getIdentifierFile(entry);
				ids.add(id);
			} catch (FactoryException e) {
				throw new ServiceException(e.getCode(), e.getParam());
			}
		}
		return ids;
	}
}

package com.pourbaix.infinity.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.context.ReaderContext;
import com.pourbaix.infinity.domain.Creature;
import com.pourbaix.infinity.domain.IdentifierFile;
import com.pourbaix.infinity.domain.Spell;
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

	@Autowired
	private GameService gameService;

	@Autowired
	private SpellFactory spellFactory;

	@Autowired
	private CreatureFactory creatureFactory;

	@Autowired
	private IdentifierFactory identifierFactory;

	@PostConstruct
	private void init() throws ServiceException {
		gameService.openGame();
	}

	public void process(final String[] args) throws ServiceException {
		// if (args.length == 0) {
		// logger.error("you must provide a file or directory parameter !");
		// return;
		// }
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
			throw new ServiceException(e);
		}
	}

	public Spell getSpell(String resource) throws ServiceException {
		try {
			Spell spell = spellFactory.getSpell(resource);
			return spell;
		} catch (FactoryException e) {
			throw new ServiceException(e);
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
			throw new ServiceException(e);
		}
	}

	public Creature getCreature(String resource) throws ServiceException {
		try {
			Creature creature = creatureFactory.getCreature(resource);
			return creature;
		} catch (FactoryException e) {
			throw new ServiceException(e);
		}
	}

	public List<IdentifierFile> getIdentifierFiles() throws ServiceException {
		List<IdentifierFile> ids = new ArrayList<>();
		for (ResourceEntry entry : Keyfile.getInstance().getResourceEntriesByExtension("ids")) {
			try {
				IdentifierFile id = identifierFactory.getIdentifierFile(entry);
				ids.add(id);
			} catch (FactoryException e) {
				throw new ServiceException(e);
			}
		}
		return ids;
	}
}

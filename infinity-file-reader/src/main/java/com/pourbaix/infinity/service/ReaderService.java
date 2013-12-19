package com.pourbaix.infinity.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.cache.CacheException;
import com.pourbaix.infinity.context.GlobalContext;
import com.pourbaix.infinity.entity.IdentifierFile;
import com.pourbaix.infinity.entity.Spell;
import com.pourbaix.infinity.factory.IdentifierFactory;
import com.pourbaix.infinity.factory.SpellFactory;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;

@Service
public class ReaderService {

	private static final Logger logger = LoggerFactory.getLogger(ReaderService.class);

	@Resource
	private GlobalContext globalContext;

	@Autowired
	private GameService gameService;

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
				Spell spell = SpellFactory.getSpell(entry);
				spells.add(spell);
			}
			return spells;
		} catch (FactoryException | CacheException e) {
			throw new ServiceException(e);
		}
	}

	public Spell getSpell(String resource) throws ServiceException {
		try {
			Spell spell = SpellFactory.getSpell(resource);
			return spell;
		} catch (FactoryException | CacheException e) {
			throw new ServiceException(e);
		}
	}

	public List<IdentifierFile> getIdentifierFiles() throws ServiceException {
		List<IdentifierFile> ids = new ArrayList<>();
		for (ResourceEntry entry : Keyfile.getInstance().getResourceEntriesByExtension("ids")) {
			try {
				IdentifierFile id = IdentifierFactory.getIdentifierFile(entry);
				ids.add(id);
			} catch (FactoryException e) {
				throw new ServiceException(e);
			}
		}
		return ids;
	}
}

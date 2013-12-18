package com.pourbaix.infinity.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.context.GlobalContext;
import com.pourbaix.infinity.entity.IdentifierFile;
import com.pourbaix.infinity.entity.Spell;
import com.pourbaix.infinity.resource.FactoryException;
import com.pourbaix.infinity.resource.ids.IdentifierFactory;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.resource.spl.SpellFactory;

@Service
public class ReaderService {

	private static final Logger logger = LoggerFactory.getLogger(ReaderService.class);

	@Resource
	private GlobalContext globalContext;

	@Autowired
	private GameService gameService;

	public void process(final String[] args) throws ServiceException {
		// if (args.length == 0) {
		// logger.error("you must provide a file or directory parameter !");
		// return;
		// }
		try {
			gameService.openGame();
		} catch (ServiceException e) {
			logger.error(e.getMessage());
		}
		try {
			// logger.debug(IdentifierFactory.getResourceNameByIdentifier("CLERIC_DRAW_UPON_HOLY_MIGHT"));
			Spell spell = SpellFactory.getSpell("spwi112.spl");
			logger.debug(spell.toString());
			spell = SpellFactory.getSpell("SPPR214.spl");
			logger.debug(spell.toString());
			spell = SpellFactory.getSpell("SPCL321.spl");
			logger.debug(spell.toString());
		} catch (FactoryException e) {
			throw new ServiceException(e);
		}
		// // ResourceEntry entry2 = Keyfile.getInstance().getResourceEntry("KAHRK.CRE");
		// // CreResource creature = new CreResource(entry2);
		// // PlainTextResource f2da = new PlainTextResource(Keyfile.getInstance().getResourceEntry("spells.2da"));
		// logger.debug("end");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public List<Spell> getSpells() throws ServiceException {
		try {
			List<Spell> spells = new ArrayList<>();
			for (ResourceEntry entry : Keyfile.getInstance().getResourceEntriesByExtension("spl")) {
				Spell spell = SpellFactory.getSpell(entry);
				logger.debug(spell.toString());
				spells.add(spell);
			}
			return spells;
		} catch (FactoryException e) {
			throw new ServiceException(e);
		}
	}

	public Spell getSpell(String resource) throws ServiceException {
		try {
			Spell spell = SpellFactory.getSpell(resource);
			return spell;
		} catch (FactoryException e) {
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

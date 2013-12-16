package com.pourbaix.infinity.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.context.GlobalContext;
import com.pourbaix.infinity.entity.Spell;
import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.resource.spl.SpellFactory;
import com.pourbaix.infinity.resource.spl.SplResource;

@Service
public class ReaderService {

	private static final Logger logger = LoggerFactory.getLogger(ReaderService.class);

	@Resource
	private GlobalContext globalContext;

	@Autowired
	private GameService gameService;

	public void process(final String[] args) {
		// if (args.length == 0) {
		// logger.error("you must provide a file or directory parameter !");
		// return;
		// }
		try {
			gameService.openGame();
		} catch (GameServiceException e) {
			logger.error(e.getMessage());
		}
		try {
			String test = StringResource.getInstance().getStringRef(500);
			logger.debug("test=" + test);
			ResourceEntry entry = Keyfile.getInstance().getResourceEntry("SPWI112.SPL");
			Spell spell = SpellFactory.getSpell(entry);
			logger.debug(spell.toString());
			ResourceEntry entry2 = Keyfile.getInstance().getResourceEntry("SPWI219.SPL");
			Spell spell2 = SpellFactory.getSpell(entry2);
			logger.debug(spell2.toString());
			SplResource spl = new SplResource(entry);
			//			ResourceEntry entry2 = Keyfile.getInstance().getResourceEntry("KAHRK.CRE");
			//			CreResource creature = new CreResource(entry2);
			//			PlainTextResource ids = new PlainTextResource(Keyfile.getInstance().getResourceEntry("spell.ids"));
			//			PlainTextResource f2da = new PlainTextResource(Keyfile.getInstance().getResourceEntry("spells.2da"));
			logger.debug("end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Spell> getSpells() {
		return null;
	}

}

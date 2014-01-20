package com.pourbaix.creature.editor.web.spell;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.DeferredResult;

import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.creature.editor.repository.SpellRepository;
import com.pourbaix.creature.editor.spell.SpellImport;
import com.pourbaix.creature.editor.spell.SpellImportException;

@Controller
public class SpellController {

	private static final Logger logger = LoggerFactory.getLogger(SpellController.class);

	@Autowired
	private SpellRepository spellRepository;

	@Autowired
	private SpellImport spellImport;

	@RequestMapping(value = "/spell", params = "modId", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<Spell> listByMod(Integer modId) {
		List<Spell> spells = spellRepository.findByModId(modId);
		return spells;
	}

	@RequestMapping(value = "/spell/import", params = "modId", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public DeferredResult<Integer> importSpells(Integer modId) {
		final DeferredResult<Integer> result = new DeferredResult<>();
		spellImport.startImport(result, modId);
		return result;
	}

	@RequestMapping("/spell/import/gather")
	@ResponseBody
	public DeferredResult<List<Spell>> getImportedSpells() throws SpellImportException {
		final DeferredResult<List<Spell>> result = new DeferredResult<>();
		spellImport.getSpellsInQueue(result);
		return result;

	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ SpellImportException.class })
	public void handleImportException(SpellImportException exception) {
		logger.error(exception.getMessage());
	}

	@RequestMapping("/spell/import/cancel")
	@ResponseBody
	public void cancelImport() {
		spellImport.cancelImport();
	}

}

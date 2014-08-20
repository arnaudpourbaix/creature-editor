package com.pourbaix.creature.editor.web.spell;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.creature.editor.domain.SpellExclusionFlag;
import com.pourbaix.creature.editor.domain.SpellFlag;
import com.pourbaix.creature.editor.domain.SpellOffensiveFlag;
import com.pourbaix.creature.editor.repository.SpellRepository;
import com.pourbaix.creature.editor.service.SpellDataService;
import com.pourbaix.creature.editor.spell.SpellImportService;
import com.pourbaix.infinity.service.GameService;
import com.pourbaix.infinity.service.ReaderService;
import com.pourbaix.infinity.service.ServiceException;

@RestController
public class SpellController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(SpellController.class);

	@Autowired
	private SpellRepository spellRepository;

	@Autowired
	private SpellImportService spellImportService;

	@Autowired
	private GameService gameService;

	@Autowired
	private ReaderService readerService;

	@RequestMapping(value = "/spell", params = "modId", method = RequestMethod.GET, produces = "application/json")
	public List<Spell> listByMod(Integer modId) {
		List<Spell> spells = spellRepository.findByModId(modId);
		return spells;
	}

	@RequestMapping(value = "/spell/{id}", method = RequestMethod.GET, produces = "application/json")
	public Spell getById(@PathVariable Integer id) {
		return spellRepository.findById(id);
	}

	@RequestMapping(value = "/spell", method = RequestMethod.PUT, produces = "application/json")
	public Spell save(@RequestBody Spell spell) {
		spellRepository.save(spell);
		return spell;
	}

	@RequestMapping(value = "/spell/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		spellRepository.delete(id);
	}

	@RequestMapping(value = "/spell/import", params = "modId", method = RequestMethod.GET, produces = "application/json")
	public DeferredResult<Integer> importSpells(Integer modId) throws ServiceException {
		final DeferredResult<Integer> result = new DeferredResult<>();
		spellImportService.startImport(result, modId);
		return result;
	}

	@RequestMapping("/spell/import/gather")
	public DeferredResult<List<Spell>> getImportedSpells() throws ServiceException {
		final DeferredResult<List<Spell>> result = new DeferredResult<>();
		spellImportService.getSpellsInQueue(result);
		return result;

	}

	@RequestMapping("/spell/import/cancel")
	public void cancelImport() {
		spellImportService.cancelImport();
	}

	@RequestMapping("/spell/flags")
	public List<SpellFlag> getFlags() {
		return SpellDataService.flags;
	}

	@RequestMapping("/spell/exclusionFlags")
	public List<SpellExclusionFlag> getExclusionFlags() {
		return SpellDataService.exclusionFlags;
	}

	@RequestMapping("/spell/offensiveFlags")
	public List<SpellOffensiveFlag> getOffensiveFlags() {
		return SpellDataService.offensiveFlags;
	}

	@RequestMapping(value = "/spell/read/{resource}", method = RequestMethod.GET, produces = "application/json")
	public Spell readByResource(@PathVariable String resource) throws ServiceException {
		gameService.openGame();
		Spell spell = readerService.getSpell(resource + ".SPL");
		return spell;
	}

}

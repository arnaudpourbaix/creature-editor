package com.pourbaix.creature.editor.web.spell;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.creature.editor.repository.SpellRepository;

@Controller
public class SpellController {

	static final Logger LOG = LoggerFactory.getLogger(SpellController.class);

	@Autowired
	private SpellRepository spellRepository;

	@RequestMapping(value = "/spell", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<Spell> list() {
		List<Spell> spells = spellRepository.findAllFetchMod();
		return spells;
	}

	@RequestMapping(value = "/spell", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public DeferredResult importSpells() {
		final DeferredResult<String> deferredResult = new DeferredResult<>();
		// runInOtherThread(deferredResult);
		return deferredResult;
	}

}

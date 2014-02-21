package com.pourbaix.creature.editor.spell;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.persistence.PersistenceException;

import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.pourbaix.creature.editor.domain.Mod;
import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.creature.editor.repository.ModRepository;
import com.pourbaix.creature.editor.repository.SpellRepository;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.service.ReaderService;
import com.pourbaix.infinity.service.ServiceException;

@Service
public class SpellImport implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SpellImport.class);

	@Autowired
	private ReaderService readerService;

	@Autowired
	private SpellRepository spellRepository;

	@Autowired
	private ModRepository modRepository;

	private volatile boolean running = false;
	private Thread thread;
	private LinkedBlockingQueue<Spell> spells;
	private Integer modId;
	private List<ResourceEntry> resources;

	public void run() {
		Mod mod = modRepository.findOne(modId);
		spellRepository.deleteByModId(modId);
		spells = new LinkedBlockingQueue<>();
		for (ResourceEntry resource : resources) {
			if (!running) { // process has been cancelled
				break;
			}
			Spell spell = null;
			try {
				spell = readerService.getSpell(resource);
				spell.setMod(mod);
				spellRepository.save(spell);
				spells.add(spell);
			} catch (DataException | ServiceException | PersistenceException e) {
				logger.error(resource.getResourceName(), e.getMessage());
				running = false;
			}
		}
		running = false;
	}

	public void startImport(DeferredResult<Integer> deferredResult, Integer modId) throws ServiceException {
		if (running) {
			deferredResult.setResult(-1);
			return;
		}
		running = true;
		this.modId = modId;
		resources = readerService.getSpellResources();
		deferredResult.setResult(resources.size());

		thread = new Thread(this, "import");
		thread.start();
	}

	public void getSpellsInQueue(DeferredResult<List<Spell>> deferredResult) {
		List<Spell> resultSpells = new ArrayList<>();
		try {
			while (!spells.isEmpty()) {
				resultSpells.add(spells.take());
			}
			deferredResult.setResult(resultSpells);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}

	public void cancelImport() {
		if (running) {
			running = false;
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}

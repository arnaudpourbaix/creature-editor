package com.pourbaix.creature.editor.spell;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;

import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.pourbaix.creature.editor.domain.Mod;
import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.creature.editor.repository.ModRepository;
import com.pourbaix.creature.editor.repository.SpellRepository;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.service.GameService;
import com.pourbaix.infinity.service.ReaderService;
import com.pourbaix.infinity.service.ServiceException;

@Service
public class SpellImportService implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SpellImportService.class);

	@Resource
	private GameService gameService;

	@Resource
	private ReaderService readerService;

	@Resource
	private SpellRepository spellRepository;

	@Resource
	private ModRepository modRepository;

	private static final String IMPORT_SAVING_ERROR = "IMPORT_SAVING_ERROR";

	private volatile boolean running = false;
	private Thread thread;
	private LinkedBlockingQueue<Spell> spells;
	private Integer modId;
	private List<ResourceEntry> resources;
	private ServiceException exception;

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
			} catch (ServiceException e) {
				logger.error(resource.getResourceName(), e.getMessage());
				running = false;
				exception = e;
			} catch (DataException | PersistenceException e) {
				logger.error(resource.getResourceName(), e.getMessage());
				running = false;
				exception = new ServiceException(IMPORT_SAVING_ERROR, resource.getResourceName());
			}
		}
		running = false;
	}

	public void startImport(DeferredResult<Integer> deferredResult, Integer modId) throws ServiceException {
		if (running) {
			deferredResult.setResult(-1);
			return;
		}
		this.exception = null;
		this.modId = modId;
		gameService.openGame();
		resources = readerService.getSpellResources();
		deferredResult.setResult(resources.size());
		running = true;
		thread = new Thread(this, "import");
		thread.start();
	}

	public void getSpellsInQueue(DeferredResult<List<Spell>> deferredResult) throws ServiceException {
		if (!running && exception != null) {
			throw exception;
		}
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

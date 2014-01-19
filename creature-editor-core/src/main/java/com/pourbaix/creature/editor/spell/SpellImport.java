package com.pourbaix.creature.editor.spell;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.service.ReaderService;
import com.pourbaix.infinity.service.ServiceException;

@Service
public class SpellImport implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SpellImport.class);

	@Autowired
	private ReaderService readerService;

	private volatile boolean running = false;
	private Thread thread;
	private LinkedBlockingQueue<Spell> spells;
	private Integer modId;
	private List<ResourceEntry> resources;
	private final BlockingQueue<DeferredResult<Spell>> resultQueue = new LinkedBlockingQueue<>();

	public void run() {
		spells = new LinkedBlockingQueue<>();
		for (ResourceEntry resource : resources) {
			if (!running) { // process has been cancelled
				break;
			}
			logger.debug("reading " + resource.getResourceName());
			try {
				Spell spell = readerService.getSpell(resource);
				spells.add(spell);
			} catch (ServiceException e) {
				logger.error(e.getMessage());
			}
		}
		running = false;
	}

	public void startImport(DeferredResult<Integer> deferredResult, Integer modId) {
		if (running) {
			deferredResult.setResult(-1);
			return;
		}
		running = true;
		this.modId = modId;
		resources = readerService.getSpellResources();
		resources = Lists.newArrayList(Iterables.filter(resources, new Predicate<ResourceEntry>() {
			public boolean apply(ResourceEntry resource) {
				return resource.getResourceName().startsWith("SPWI1");
			}
		}));
		deferredResult.setResult(resources.size());

		thread = new Thread(this);
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

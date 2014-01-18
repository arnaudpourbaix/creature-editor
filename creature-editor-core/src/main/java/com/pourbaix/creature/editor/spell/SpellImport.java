package com.pourbaix.creature.editor.spell;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.pourbaix.infinity.domain.Spell;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.service.ReaderService;
import com.pourbaix.infinity.service.ServiceException;

@Service
public class SpellImport implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SpellImport.class);

	@Autowired
	private ReaderService readerService;

	private volatile boolean running = false;
	private int progress;
	private Thread thread;

	public void run() {
		progress = 0;
		List<ResourceEntry> resources = readerService.getSpellResources();
		List<ResourceEntry> lst = Lists.newArrayList(Iterables.filter(resources, new Predicate<ResourceEntry>() {
			public boolean apply(ResourceEntry resource) {
				return resource.getResourceName().startsWith("SPWI1");
			}

		}));
		int i = 1;
		int count = lst.size();
		logger.debug("reading spell count: " + count);
		for (ResourceEntry resource : lst) {
			try {
				logger.debug("reading: " + resource.getResourceName() + ", progress: " + progress);
				Spell spell = readerService.getSpell(resource);
				progress = 100 * i++ / count;
			} catch (ServiceException e) {
				logger.error(e.getMessage());
			}
		}
		running = false;
	}

	public boolean startImport(Integer modId) {
		if (running) {
			return false;
		}
		logger.debug("startImport for mod " + modId);
		running = true;
		thread = new Thread(this);
		thread.start();
		return true;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

}

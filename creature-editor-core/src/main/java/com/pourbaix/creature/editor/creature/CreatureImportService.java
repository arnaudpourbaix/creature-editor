package com.pourbaix.creature.editor.creature;

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

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.repository.CreatureRepository;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.service.GameService;
import com.pourbaix.infinity.service.ReaderService;
import com.pourbaix.infinity.service.ServiceException;

@Service
public class CreatureImportService implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(CreatureImportService.class);

	@Resource
	private GameService gameService;

	@Resource
	private ReaderService readerService;

	@Resource
	private CreatureRepository creatureRepository;

	private static final String IMPORT_SAVING_ERROR = "IMPORT_SAVING_ERROR";

	private volatile boolean running = false;
	private Thread thread;
	private LinkedBlockingQueue<Creature> creatures;
	private Integer modId;
	private List<ResourceEntry> resources;
	private ServiceException exception;

	public void run() {
//		Mod mod = modRepository.findOne(modId);
//		spellRepository.deleteByModId(modId);
		creatures = new LinkedBlockingQueue<>();
		for (ResourceEntry resource : resources) {
			if (!running) { // process has been cancelled
				break;
			}
			Creature creature = null;
			try {
				creature = readerService.getCreature(resource);
//				creature.setMod(mod);
				creatureRepository.save(creature);
				creatures.add(creature);
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
		resources = readerService.getCreatureResources();
		resources = FluentIterable
                .from(resources)
                .filter(new Predicate<ResourceEntry>() {
        			public boolean apply(ResourceEntry input) {
        				return input.getResourceName().startsWith("AL");
        			}
        		}).toImmutableList();
		deferredResult.setResult(resources.size());
		running = true;
		thread = new Thread(this, "import");
		thread.start();
	}

	public void getCreaturesInQueue(DeferredResult<List<Creature>> deferredResult) throws ServiceException {
		if (!running && exception != null) {
			throw exception;
		}
		List<Creature> resultCreatures = new ArrayList<>();
		try {
			while (!creatures.isEmpty()) {
				resultCreatures.add(creatures.take());
			}
			deferredResult.setResult(resultCreatures);
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

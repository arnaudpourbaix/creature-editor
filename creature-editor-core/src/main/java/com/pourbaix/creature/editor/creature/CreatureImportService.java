package com.pourbaix.creature.editor.creature;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.service.CreatureService;
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
	private CreatureService creatureService;

	private static final String IMPORT_SAVING_ERROR = "CREATURE.IMPORT.SAVING_ERROR";
	private static final String IMPORT_CANCELLED = "CREATURE.IMPORT.CANCELLED";
	private static final String IMPORT_ALREADY_RUNNING = "CREATURE.IMPORT.ALREADY_RUNNING";

	private volatile boolean running = false;
	private LinkedBlockingQueue<Creature> creatures;
	private CreatureImportOptions options;
	private List<ResourceEntry> resources;
	private CreatureImportException exception;
	
	public void run() {
		logger.trace("creature import - start");
		try {
			importCreatures();
		} catch (CreatureImportException e) {
			exception = e;
		}
		running = false;
		logger.trace("creature import - end");
	}

	public void startImport(DeferredResult<Integer> deferredResult, final CreatureImportOptions options) throws ServiceException, CreatureImportException {
		if (running) {
			throw new CreatureImportException(IMPORT_ALREADY_RUNNING);
		}
		this.exception = null;
		this.options = options;
		gameService.openGame();
		resources = readerService.getCreatureResources();
		if (StringUtils.isNotEmpty(options.getResource())) {
			resources = FluentIterable.from(resources).filter(new Predicate<ResourceEntry>() {
				public boolean apply(ResourceEntry entry) {
					return entry.getResourceName().matches(options.getResource());
				}
			}).toImmutableList();
		}
		deferredResult.setResult(resources.size());
		running = true;
		Thread thread = new Thread(this, "import-creature");
		thread.start();
	}

	public void getCreaturesInQueue(DeferredResult<List<Creature>> deferredResult) throws CreatureImportException {
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
			logger.trace("cancel creature import");
			running = false;
		}
	}

	@Transactional(rollbackFor=CreatureImportException.class)
	private void importCreatures() throws CreatureImportException {
		List<Creature> creaturesToInsert = new ArrayList<>();
		creatures = new LinkedBlockingQueue<>();
		for (ResourceEntry resource : resources) {
			if (!running) { // process has been cancelled
				logger.info("creature import was cancelled by user");
				throw new CreatureImportException(IMPORT_CANCELLED);
			}
			Creature creature = null;
			try {
				logger.trace("reading resource {}", resource);
				creature = readerService.getCreature(resource, options.isOnlyName());
				creature.setMod(options.getMod());
				if (options.isOverride()) {
					creatureService.deleteByResourceAndGameAndMod(creature.getResource(), creature.getGame(), creature.getMod());
				}
				//creatureService.save(creature);
				creaturesToInsert.add(creature);
				creatures.add(creature);
			} catch (ServiceException e) {
				logger.error(resource.getResourceName(), e.getMessage());
				throw new CreatureImportException(e);
			} catch (DataException | PersistenceException e) {
				logger.error(resource.getResourceName(), e.getMessage());
				throw new CreatureImportException(IMPORT_SAVING_ERROR, resource.getResourceName());
			}
		}
		creatureService.save(creaturesToInsert);
	}
	
}

package com.pourbaix.creature.editor.web.creature;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.pourbaix.creature.editor.creature.CreatureImportService;
import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.repository.CreatureRepository;
import com.pourbaix.infinity.service.ServiceException;

@RestController
@RequestMapping(value = "/creature")
public class CreatureController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CreatureController.class);

	@Resource
	private CreatureRepository creatureRepository;

	@Resource
	private CreatureImportService creatureImportService;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<Creature> list() {
		List<Creature> creatures = creatureRepository.findAll();
		return creatures;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public Creature getById(@PathVariable Integer id) {
		return creatureRepository.findOne(id);
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public Creature save(@RequestBody Creature creature) {
		creatureRepository.save(creature);
		return creature;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		creatureRepository.delete(id);
	}

	@RequestMapping(value = "/import", params = "modId", method = RequestMethod.GET, produces = "application/json")
	public DeferredResult<Integer> importCreatures(Integer modId) throws ServiceException {
		final DeferredResult<Integer> result = new DeferredResult<>();
		creatureImportService.startImport(result, modId);
		return result;
	}

	@RequestMapping("/import/gather")
	public DeferredResult<List<Creature>> getImportedCreatures() throws ServiceException {
		final DeferredResult<List<Creature>> result = new DeferredResult<>();
		creatureImportService.getCreaturesInQueue(result);
		return result;

	}

	@RequestMapping("/import/cancel")
	public void cancelImport() {
		creatureImportService.cancelImport();
	}

}

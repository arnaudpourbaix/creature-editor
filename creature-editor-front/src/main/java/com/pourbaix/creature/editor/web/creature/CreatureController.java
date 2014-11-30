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

import com.pourbaix.creature.editor.creature.CreatureImportException;
import com.pourbaix.creature.editor.creature.CreatureImportOptions;
import com.pourbaix.creature.editor.creature.CreatureImportService;
import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.service.CreatureService;
import com.pourbaix.infinity.service.ServiceException;

@RestController
@RequestMapping(value = "/creature")
public class CreatureController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CreatureController.class);

	@Resource
	private CreatureService creatureService;

	@Resource
	private CreatureImportService creatureImportService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Creature> list() {
		List<Creature> creatures = creatureService.list();
		return creatures;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Creature getById(@PathVariable Integer id) {
		return creatureService.getById(id);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public Creature save(@RequestBody Creature creature) {
		creatureService.save(creature);
		return creature;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		Creature c = creatureService.getById(id);
		creatureService.delete(id);
	}

	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public DeferredResult<Integer> importCreatures(@RequestBody CreatureImportOptions options) throws CreatureImportException, ServiceException {
		final DeferredResult<Integer> result = new DeferredResult<>();
		creatureImportService.startImport(result, options);
		return result;
	}

	@RequestMapping("/import/gather")
	public DeferredResult<List<Creature>> getImportedCreatures() throws CreatureImportException {
		final DeferredResult<List<Creature>> result = new DeferredResult<>();
		creatureImportService.getCreaturesInQueue(result);
		return result;

	}

	@RequestMapping("/import/cancel")
	public void cancelImport() {
		creatureImportService.cancelImport();
	}

}

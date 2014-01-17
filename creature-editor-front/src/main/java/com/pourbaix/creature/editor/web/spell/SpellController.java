package com.pourbaix.creature.editor.web.spell;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.creature.editor.repository.SpellRepository;

@Controller
public class SpellController {

	private static final Logger logger = LoggerFactory.getLogger(SpellController.class);

	@Autowired
	private SpellRepository spellRepository;

	@Autowired
	private DeferredResultService updateService;

	@RequestMapping(value = "/spell", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<Spell> list() {
		List<Spell> spells = spellRepository.findAllFetchMod();
		return spells;
	}

	@RequestMapping(value = "/spell/import", params = "modId", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public DeferredResult<String> importSpells(Integer modId) {
		updateService.subscribe();
		final DeferredResult<String> deferredResult = new DeferredResult<>();
		runInOtherThread(deferredResult);
		return deferredResult;
	}

	/**
	 * Get hold of the latest match report - when it arrives But in the process hold on to server resources
	 */
	@RequestMapping(value = "/spell/deferred", method = RequestMethod.GET)
	@ResponseBody
	public DeferredResult<Message> getUpdateImport() {
		final DeferredResult<Message> result = new DeferredResult<Message>();
		updateService.getUpdate(result);
		return result;
	}

	private final Queue<DeferredResult<String>> eventQueue = new ConcurrentLinkedQueue<>();

	@RequestMapping("/spell/deferred2")
	public @ResponseBody
	DeferredResult<String> deferredCall() {
		DeferredResult<String> result = new DeferredResult<>();
		this.eventQueue.add(result);
		return result;
	}

	@Scheduled(fixedRate = 5000)
	public void simulateExternalThread() throws InterruptedException {
		Thread.sleep(5000);
		for (DeferredResult<String> result : this.eventQueue) {
			result.setResult("Hello");
			this.eventQueue.remove(result);
		}
	}

	private void runInOtherThread(DeferredResult<String> deferredResult) {
		// seconds later in other thread...
		deferredResult.setResult("HTTP response is: 42");
	}

	@RequestMapping(value = "/spellImp/begin" + "", method = RequestMethod.GET)
	@ResponseBody
	public String start() {
		updateService.subscribe();
		return "OK";
	}

	@RequestMapping("/spellImp/deferred")
	@ResponseBody
	public DeferredResult<Message> getUpdate() {
		final DeferredResult<Message> result = new DeferredResult<Message>();
		updateService.getUpdate(result);
		return result;
	}

}

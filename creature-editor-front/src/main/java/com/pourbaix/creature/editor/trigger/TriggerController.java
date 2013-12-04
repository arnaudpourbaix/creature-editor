package com.pourbaix.creature.editor.trigger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.pourbaix.creature.editor.domain.Trigger;
import com.pourbaix.creature.editor.repository.TriggerRepository;

@Controller
public class TriggerController {

	static final Logger LOG = LoggerFactory.getLogger(TriggerController.class);

	@Autowired
	private TriggerRepository triggerRepository;

	@RequestMapping(value = "/trigger", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<String> list() {
		List<Trigger> triggers = triggerRepository.findAll();
		return Lists.transform(triggers, new Function<Trigger, String>() {
			@Override
			public String apply(Trigger input) {
				return input.getName();
			}
		});
	}
}

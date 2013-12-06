package com.pourbaix.creature.editor.web.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pourbaix.creature.editor.domain.Action;
import com.pourbaix.creature.editor.repository.ActionRepository;

@Controller
public class ActionController {

	static final Logger LOG = LoggerFactory.getLogger(ActionController.class);

	@Autowired
	private ActionRepository actionRepository;

	@RequestMapping(value = "/action", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<Action> list() {
		List<Action> actions = actionRepository.findAll();
		return actions;
	}

}

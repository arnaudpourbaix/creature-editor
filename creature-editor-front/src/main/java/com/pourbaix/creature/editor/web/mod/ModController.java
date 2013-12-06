package com.pourbaix.creature.editor.web.mod;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pourbaix.creature.editor.domain.Mod;
import com.pourbaix.creature.editor.repository.ModRepository;

@Controller
public class ModController {

	static final Logger LOG = LoggerFactory.getLogger(ModController.class);

	@Autowired
	private ModRepository modRepository;

	@RequestMapping(value = "/mod", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<Mod> list() {
		List<Mod> mods = modRepository.findAll();
		return mods;
	}

}

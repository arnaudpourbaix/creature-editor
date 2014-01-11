package com.pourbaix.creature.editor.web.mod;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

	@RequestMapping(value = "/mod/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Mod getById(@PathVariable Integer id) {
		return modRepository.findOne(id);
	}

	@RequestMapping(value = "/mod/name/{name}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Mod getByName(@PathVariable String name) {
		Mod mod = modRepository.findByName(name);
		return mod;
	}

	@RequestMapping(value = "/mod", method = RequestMethod.PUT, produces = "application/json")
	public @ResponseBody
	Mod save(@RequestBody Mod mod) {
		modRepository.save(mod);
		return mod;
	}

	@RequestMapping(value = "/mod/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		modRepository.delete(id);
	}

}

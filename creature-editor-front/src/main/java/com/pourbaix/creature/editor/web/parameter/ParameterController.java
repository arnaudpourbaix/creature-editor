package com.pourbaix.creature.editor.web.parameter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pourbaix.creature.editor.domain.Parameter;
import com.pourbaix.creature.editor.domain.ParameterType;
import com.pourbaix.creature.editor.repository.ParameterRepository;
import com.pourbaix.creature.editor.repository.ParameterTypeRepository;

@RestController
public class ParameterController {

	private static final Logger logger = LoggerFactory.getLogger(ParameterController.class);

	@Autowired
	private ParameterRepository parameterRepository;
	@Autowired
	private ParameterTypeRepository parameterTypeRepository;

	@RequestMapping(value = "/parameterType", method = RequestMethod.GET, produces = "application/json")
	public List<ParameterType> typeList() {
		List<ParameterType> types = parameterTypeRepository.findAll();
		return types;
	}

	@RequestMapping(value = "/parameter", params = "typeId", method = RequestMethod.GET, produces = "application/json")
	public List<Parameter> listByMod(String typeId) {
		List<Parameter> params = parameterRepository.findByTypeId(typeId);
		return params;
	}

	@RequestMapping(value = "/parameter/{id}", method = RequestMethod.GET, produces = "application/json")
	public Parameter getById(@PathVariable String id) {
		Parameter parameter = parameterRepository.findByName(id);
		return parameter;
	}

	@RequestMapping(value = "/parameter", method = RequestMethod.PUT, produces = "application/json")
	public Parameter save(@RequestBody Parameter parameter) {
		parameterRepository.save(parameter);
		return parameter;
	}

}

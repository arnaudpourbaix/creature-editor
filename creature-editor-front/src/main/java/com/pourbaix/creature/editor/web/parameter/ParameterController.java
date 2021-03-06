package com.pourbaix.creature.editor.web.parameter;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pourbaix.creature.editor.domain.Parameter;
import com.pourbaix.creature.editor.domain.ParameterBooleanValue;
import com.pourbaix.creature.editor.domain.ParameterEnum;
import com.pourbaix.creature.editor.domain.ParameterFolderValue;
import com.pourbaix.creature.editor.domain.ParameterIntegerValue;
import com.pourbaix.creature.editor.domain.ParameterListValue;
import com.pourbaix.creature.editor.domain.ParameterStringValue;
import com.pourbaix.creature.editor.domain.ParameterType;
import com.pourbaix.creature.editor.repository.ParameterRepository;
import com.pourbaix.creature.editor.repository.ParameterTypeRepository;
import com.pourbaix.creature.editor.service.ParameterEvent;
import com.pourbaix.infinity.service.GameService;

@RestController
public class ParameterController implements ApplicationEventPublisherAware {

	private static final Logger logger = LoggerFactory.getLogger(ParameterController.class);

	@Autowired
	private ParameterRepository parameterRepository;
	@Autowired
	private ParameterTypeRepository parameterTypeRepository;
	@Autowired
	private GameService gameService;
	private ApplicationEventPublisher publisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

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
	public Parameter getById(@PathVariable ParameterEnum id) {
		Parameter parameter = parameterRepository.findByName(id);
		return parameter;
	}

	@RequestMapping(value = "/parameter/checkFolder", params = "folder", method = RequestMethod.GET, produces = "application/json")
	public boolean checkFolder(String folder) {
		File file = new File(folder);
		return file.exists() && file.isDirectory();
	}

	@RequestMapping(value = "/parameter", params = "type=STRING", method = RequestMethod.PUT, produces = "application/json")
	public Parameter save(@RequestBody ParameterStringValue parameter) {
		return commonSave(parameter);
	}

	@RequestMapping(value = "/parameter", params = "type=BOOLEAN", method = RequestMethod.PUT, produces = "application/json")
	public Parameter save(@RequestBody ParameterBooleanValue parameter) {
		return commonSave(parameter);
	}

	@RequestMapping(value = "/parameter", params = "type=INT", method = RequestMethod.PUT, produces = "application/json")
	public Parameter save(@RequestBody ParameterIntegerValue parameter) {
		return commonSave(parameter);
	}

	@RequestMapping(value = "/parameter", params = "type=LIST", method = RequestMethod.PUT, produces = "application/json")
	public Parameter save(@RequestBody ParameterListValue parameter) {
		return commonSave(parameter);
	}

	@RequestMapping(value = "/parameter", params = "type=FOLDER", method = RequestMethod.PUT, produces = "application/json")
	public Parameter save(@RequestBody ParameterFolderValue parameter) {
		return commonSave(parameter);
	}

	private Parameter commonSave(Parameter parameter) {
		parameterRepository.save(parameter);
		publisher.publishEvent(new ParameterEvent(parameter));
		return parameter;
	}

}

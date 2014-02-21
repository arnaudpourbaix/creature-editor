package com.pourbaix.creature.editor.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.Parameter;
import com.pourbaix.creature.editor.domain.ParameterFolderValue;
import com.pourbaix.creature.editor.repository.ParameterRepository;

@Service
public class ParameterService {

	@Resource
	private ParameterRepository parameterRepository;

	public String getGameDirectory() {
		Parameter gameDirectory = parameterRepository.findOne("GAME_DIRECTORY");
		return ((ParameterFolderValue) gameDirectory).getValue();
	}

}

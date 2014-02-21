package com.pourbaix.creature.editor.service;

import java.io.File;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.Parameter;
import com.pourbaix.creature.editor.domain.ParameterFolderValue;
import com.pourbaix.creature.editor.domain.ParameterListValue;
import com.pourbaix.creature.editor.repository.ParameterRepository;
import com.pourbaix.infinity.datatype.GameVersionEnum;

@Service
public class ParameterService {

	enum ParamEnum {
		GAME_DIRECTORY, GAME_VERSION, DEFAULT_LANGUAGE
	}

	@Resource
	private ParameterRepository parameterRepository;

	public File getGameDirectory() {
		Parameter gameDirectory = parameterRepository.findOne(ParamEnum.GAME_DIRECTORY.toString());
		return new File(((ParameterFolderValue) gameDirectory).getValue());
	}

	public GameVersionEnum getGameVersion() {
		Parameter gameVersion = parameterRepository.findOne(ParamEnum.GAME_VERSION.toString());
		String value = ((ParameterListValue) gameVersion).getValue();
		return StringUtils.isNotBlank(value) ? GameVersionEnum.valueOf(value) : null;
	}

	public String getDefaultLanguage() {
		Parameter language = parameterRepository.findOne(ParamEnum.DEFAULT_LANGUAGE.toString());
		return ((ParameterListValue) language).getValue();
	}

}

package com.pourbaix.creature.editor.service;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.Parameter;
import com.pourbaix.creature.editor.domain.ParameterEnum;
import com.pourbaix.infinity.service.GameService;

@Service
public class ParameterTracker implements ApplicationListener<ParameterEvent> {

	@Resource
	private GameService gameService;

	@Override
	public void onApplicationEvent(ParameterEvent event) {
		Parameter parameter = (Parameter) event.getSource();
		if (parameter.getName() == ParameterEnum.GAME_DIRECTORY || parameter.getName() == ParameterEnum.GAME_VERSION) {
			gameService.closeGame();
		}
	}

}

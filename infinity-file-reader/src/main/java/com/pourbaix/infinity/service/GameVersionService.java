package com.pourbaix.infinity.service;

import java.io.File;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.context.GlobalContext;
import com.pourbaix.infinity.entity.GameVersionEnum;

@Service
public class GameVersionService {

	private static final Logger logger = LoggerFactory.getLogger(GameVersionService.class);

	@Resource
	private GlobalContext globalContext;

	public GameVersionEnum getVersion() throws GameVersionException {
		openGame();
		return GameVersionEnum.BaldurGateEE;
	}

	public void openGame() throws GameVersionException {
		if (globalContext.getGameDirectory().isEmpty()) {
			throw new GameVersionException("game directory is not defined in configuration");
		}
		File file = new File(globalContext.getGameDirectory());
		if (!file.isDirectory()) {
			throw new GameVersionException("game directory is invalid, please check configuration");
		}
		globalContext.setRootDirectory(file);
		if (!new File(globalContext.getRootDirectory(), "chitin.key").exists()) {
			throw new GameVersionException("no chitin key in game directory");
		}
	}

}

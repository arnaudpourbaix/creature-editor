package com.pourbaix.infinity.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.context.GlobalContext;

@Service
public class ReaderService {

	private static final Logger logger = LoggerFactory.getLogger(ReaderService.class);

	@Resource
	private GlobalContext globalContext;

	@Autowired
	private GameService gameService;

	public void process(final String[] args) {
		// if (args.length == 0) {
		// logger.error("you must provide a file or directory parameter !");
		// return;
		// }
		try {
			gameService.openGame();
		} catch (GameException e) {
			logger.error(e.getMessage());
		}
	}

}

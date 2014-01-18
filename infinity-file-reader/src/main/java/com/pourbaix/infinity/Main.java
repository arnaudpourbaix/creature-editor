package com.pourbaix.infinity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.pourbaix.infinity.datatype.GameVersionEnum;
import com.pourbaix.infinity.service.ReaderService;
import com.pourbaix.infinity.service.ServiceException;
import com.pourbaix.infinity.spring.RootConfig;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		try {
			AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
			ReaderService readerService = ctx.getBean(ReaderService.class);
			readerService.process(args);
			ctx.close();
		} catch (BeansException e) {
			displayError(e);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
		}
	}

	public static void displayError(BeansException e) {
		if (e.getMessage().contains(GameVersionEnum.class.getName())) {
			logger.error("Game version is not defined or has an invalid value, check configuration");
		} else if (e.getMessage().contains("game.directory")) {
			logger.error("Game directory is not defined, check configuration");
		} else if (e.getMessage().contains("default.language")) {
			logger.error("Default language is not defined, check configuration");
		} else if (e.getMessage().contains("IllegalArgumentException")) {
			logger.error("Configuration error !");
			logger.error(e.getMessage());
		} else {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

}

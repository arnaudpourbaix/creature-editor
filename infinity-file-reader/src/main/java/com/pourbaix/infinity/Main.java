package com.pourbaix.infinity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.pourbaix.infinity.config.RootConfig;
import com.pourbaix.infinity.service.ReaderService;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		try {
			AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
			ReaderService readerService = ctx.getBean(ReaderService.class);
			readerService.process(args);
			ctx.close();
		} catch (BeanCreationException e) {
			if (e.getMessage().contains("IllegalArgumentException")) {
				logger.error("Configuration error !");
				logger.error(e.getMessage());
			} else {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}

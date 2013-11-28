package com.pourbaix.script.creature.generator;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.pourbaix.script.creature.config.RootConfig;

@Component
public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	@Resource
	private GeneratorService generatorService;

	public static void main(final String[] args) {
		try {
			AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
			ctx.register(RootConfig.class);
			ctx.refresh();
			Main main = ctx.getBean(Main.class);
			main.generatorService.generate(args);
			((AnnotationConfigApplicationContext) ctx).close();
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

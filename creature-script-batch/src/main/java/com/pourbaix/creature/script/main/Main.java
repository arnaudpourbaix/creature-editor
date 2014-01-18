package com.pourbaix.creature.script.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.pourbaix.creature.editor.spring.CoreConfig;
import com.pourbaix.creature.script.generator.GeneratorService;
import com.pourbaix.creature.script.spring.ScriptConfig;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		try {
			AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(CoreConfig.class, ScriptConfig.class);
			GeneratorService generatorService = ctx.getBean(GeneratorService.class);
			generatorService.generate(args);
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

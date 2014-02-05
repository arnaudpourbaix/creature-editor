package com.pourbaix.creature.editor.web.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebAppInitializer {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(new String[] { "com.pourbaix.creature.editor.web.spring", "com.pourbaix.creature.editor.spring" }, args);
	}

}
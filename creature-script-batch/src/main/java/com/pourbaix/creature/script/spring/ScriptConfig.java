package com.pourbaix.creature.script.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.pourbaix.creature.script.context.CheckContext;
import com.pourbaix.creature.script.context.GlobalContext;
import com.pourbaix.creature.script.context.ProbablityContext;
import com.pourbaix.creature.script.context.TargetContext;

@Configuration
public class ScriptConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		return configurer;
	}

	@Bean
	public GlobalContext globalContext() {
		return new GlobalContext();
	}

	@Bean
	public TargetContext targetContext() {
		return new TargetContext();
	}

	@Bean
	public ProbablityContext probablityContext() {
		return new ProbablityContext();
	}

	@Bean
	public CheckContext checkContext() {
		return new CheckContext();
	}

}
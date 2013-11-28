package com.pourbaix.script.creature.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.pourbaix.script.creature.context.CheckContext;
import com.pourbaix.script.creature.context.GlobalContext;
import com.pourbaix.script.creature.context.ProbablityContext;
import com.pourbaix.script.creature.context.TargetContext;

@Configuration
@ComponentScan(basePackages = { "com.pourbaix.script.creature" })
public class RootConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		Resource[] resources = new Resource[] { new ClassPathResource("/conf/config.properties"), new ClassPathResource("/hibernate/persistence.properties") };
		// configurer.setLocation(new ClassPathResource("/conf/config.properties"));
		configurer.setLocations(resources);
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
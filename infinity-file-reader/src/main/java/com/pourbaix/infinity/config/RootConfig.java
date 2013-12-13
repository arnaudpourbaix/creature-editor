package com.pourbaix.infinity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.pourbaix.infinity.context.GlobalContext;
import com.pourbaix.infinity.resource.StringResource;

@Configuration
@ComponentScan(basePackages = { "com.pourbaix.infinity" })
public class RootConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		// Resource[] resources = new Resource[] { new ClassPathResource("config.properties"), new ClassPathResource("/hibernate/persistence.properties") };
		Resource[] resources = new Resource[] { new ClassPathResource("config.properties") };
		configurer.setLocations(resources);
		return configurer;
	}

	@Bean
	public GlobalContext globalContext() {
		return new GlobalContext();
	}

	@Bean
	public StringResource stringResource() {
		return new StringResource();
	}

}
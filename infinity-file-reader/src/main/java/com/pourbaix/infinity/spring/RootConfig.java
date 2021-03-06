package com.pourbaix.infinity.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.pourbaix.infinity.context.GlobalContext;

@Configuration
@ComponentScan(basePackages = { "com.pourbaix.infinity" })
public class RootConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		Resource[] resources = new Resource[] { new ClassPathResource("config.properties"), new ClassPathResource("/persistence.properties") };
		configurer.setLocations(resources);
		return configurer;
	}

	@Bean
	public GlobalContext globalContext() {
		return new GlobalContext();
	}

}
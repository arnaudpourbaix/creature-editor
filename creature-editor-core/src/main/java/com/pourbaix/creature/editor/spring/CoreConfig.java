package com.pourbaix.creature.editor.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.pourbaix.infinity.context.ReaderContext;

@Configuration
@ComponentScan(basePackages = { "com.pourbaix" })
@PropertySource({ "classpath:reader.properties", "classpath:/hibernate/persistence.properties" })
public class CoreConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		return configurer;
	}

	@Bean
	public ReaderContext readerContext() {
		return new ReaderContext();
	}

}
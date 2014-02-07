package com.pourbaix.creature.editor.spring;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.pourbaix.infinity.context.ReaderContext;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.pourbaix" })
@PropertySource({ "classpath:reader.properties", "classpath:/hibernate/persistence.properties" })
public class CoreConfig {

	@Bean
	public ReaderContext readerContext() {
		return new ReaderContext();
	}

}
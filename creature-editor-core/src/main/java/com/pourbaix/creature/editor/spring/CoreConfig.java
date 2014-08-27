package com.pourbaix.creature.editor.spring;

import javax.persistence.EntityManagerFactory;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.pourbaix.infinity.context.ReaderContext;

@Configuration
@EnableAutoConfiguration(exclude = { EntityManagerFactory.class })
@ComponentScan(basePackages = { "com.pourbaix" })
@EntityScan(basePackages = { "com.pourbaix" })
@PropertySource({ "classpath:/hibernate/persistence.properties" })
public class CoreConfig {

	@Bean
	public ReaderContext readerContext() {
		return new ReaderContext();
	}

}
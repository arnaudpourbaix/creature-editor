package com.pourbaix.creature.editor.web.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class WebAppInitializer {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(new String[] { "com.pourbaix.creature.editor.web.spring", "com.pourbaix.creature.editor.spring" }, args);
	}

	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
		registration.addUrlMappings("/rest/*");
		return registration;
	}

}
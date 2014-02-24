package com.pourbaix.creature.editor.service;

import org.springframework.context.ApplicationEvent;

public class ParameterEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public ParameterEvent(Object source) {
		super(source);
	}

}

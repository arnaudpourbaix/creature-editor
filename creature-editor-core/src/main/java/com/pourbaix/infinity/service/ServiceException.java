package com.pourbaix.infinity.service;

import com.pourbaix.creature.editor.exception.AbstractException;

public class ServiceException extends AbstractException {

	private static final long serialVersionUID = 1L;

	public ServiceException(String code) {
		super(code);
	}

	public ServiceException(String code, String param) {
		super(code, param);
	}
	
}

package com.pourbaix.creature.editor.domain;

import com.pourbaix.creature.editor.exception.AbstractException;

public class DomainException extends AbstractException {

	private static final long serialVersionUID = 1L;

	public DomainException(String code) {
		super(code);
	}

	public DomainException(String code, String param) {
		super(code, param);
	}
	
}

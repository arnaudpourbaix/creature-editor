package com.pourbaix.creature.editor.creature;

import com.pourbaix.creature.editor.exception.AbstractException;

public class CreatureImportException extends AbstractException {

	private static final long serialVersionUID = 1L;

	public CreatureImportException(Exception e) {
		super(e);
	}
	
	public CreatureImportException(String code) {
		super(code);
	}

	public CreatureImportException(String code, String param) {
		super(code, param);
	}
	
}

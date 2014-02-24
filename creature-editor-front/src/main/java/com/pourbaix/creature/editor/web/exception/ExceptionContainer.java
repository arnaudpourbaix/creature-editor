package com.pourbaix.creature.editor.web.exception;

public class ExceptionContainer {

	private final String code;
	private final String param;

	public ExceptionContainer(String code, String param) {
		this.code = code;
		this.param = param;
	}

	public ExceptionContainer(String code) {
		this(code, null);
	}

	public String getCode() {
		return code;
	}

	public String getParam() {
		return param;
	}

}

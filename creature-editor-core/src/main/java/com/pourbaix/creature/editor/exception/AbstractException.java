package com.pourbaix.creature.editor.exception;

public class AbstractException extends Exception {

	private static final long serialVersionUID = 1L;

	protected String code;
	protected String param;

	public AbstractException(Exception e) {
		super(e);
	}
	
	public AbstractException(final String code) {
		super(code);
		this.code = code;
	}

	public AbstractException(final String code, Exception e) {
		super(code, e);
		this.code = code;
	}

	public AbstractException(final String code, String param) {
		super(code + ": " + param);
		this.code = code;
		this.param = param;
	}

	public AbstractException(final String code, String param, Exception e) {
		super(code + ": " + param, e);
		this.code = code;
		this.param = param;
	}
	
	public String getCode() {
		return code;
	}

	public String getParam() {
		return param;
	}

}

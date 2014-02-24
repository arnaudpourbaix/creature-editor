package com.pourbaix.infinity.factory;

public class FactoryException extends Exception {

	private static final long serialVersionUID = 1L;

	private String code;
	private String param;

	public FactoryException(final String code, String param) {
		super(code + ": " + param);
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

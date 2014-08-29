package com.pourbaix.infinity.domain;

public class DimensionalArrayFileException extends Exception {

	private static final long serialVersionUID = 1L;

	private String code;
	private String param;

	public DimensionalArrayFileException(final String code, String param) {
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

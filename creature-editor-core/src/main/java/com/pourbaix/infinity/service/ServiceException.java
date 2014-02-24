package com.pourbaix.infinity.service;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	private String code;
	private String param;

	public ServiceException(final String code) {
		super(code);
		this.code = code;
	}

	public ServiceException(final String code, String param) {
		super(code + ": " + param);
		this.code = code;
		this.param = param;
	}

	//	public ServiceException(final Throwable cause) {
	//		super(cause);
	//	}
	//
	//	public ServiceException(final String message, final Throwable cause) {
	//		super(message, cause);
	//	}

	public String getCode() {
		return code;
	}

	public String getParam() {
		return param;
	}

}

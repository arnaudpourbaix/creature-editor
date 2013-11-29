package com.pourbaix.creature.script.instruction;

public class RawKeywordParam {

	private String keyword;
	private boolean result;
	private String param;

	public RawKeywordParam(String keyword) {

	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

}

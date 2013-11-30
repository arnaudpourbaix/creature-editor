package com.pourbaix.creature.script.instruction;

import java.util.ArrayList;
import java.util.List;

public class KeywordParam {

	private String keyword;
	private boolean result;
	private String param;
	private final List<RawKeywordParam> params = new ArrayList<>();

	public KeywordParam(String keyword, boolean result) {
		this.keyword = keyword;
		this.result = result;
	}

	@Override
	public String toString() {
		return "result: " + this.result + ", keyword: " + this.keyword + ", param: " + this.param;
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

	public List<RawKeywordParam> getParams() {
		return params;
	}

}

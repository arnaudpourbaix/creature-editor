package com.pourbaix.creature.script.instruction;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

public class KeywordParam {

	private T keyword;
	private boolean result;
	private final List<KeywordParam> params = new ArrayList<>();

	public KeywordParam() {
	}

	@Override
	public String toString() {
		return null;
	}

}

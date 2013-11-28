package com.pourbaix.creature.script.generator;

public class Line {

	private String rawText;
	private String text;
	private int tabulationCount = 0;

	public Line() {
		super();
	}

	public Line(final String text, final int tabulationCount) {
		this.text = text;
		this.tabulationCount = tabulationCount;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(final String rawText) {
		this.rawText = rawText;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public int getTabulationCount() {
		return tabulationCount;
	}

	public void setTabulationCount(final int tabulationCount) {
		this.tabulationCount = tabulationCount;
	}

}

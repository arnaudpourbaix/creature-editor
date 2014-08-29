package com.pourbaix.infinity.domain;

import java.util.List;

public final class DimensionalArrayRow {

	private List<String> columns;

	public DimensionalArrayRow(List<String> columns) {
		this.columns = columns;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

}

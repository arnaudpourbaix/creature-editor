package com.pourbaix.infinity.domain;

import java.util.ArrayList;
import java.util.List;

public final class DimensionalArrayFile {

	private List<String> headers;
	private final List<DimensionalArrayRow> rows = new ArrayList<>(0);

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<DimensionalArrayRow> getRows() {
		return rows;
	}

	public void addRow(List<String> row) {
		this.rows.add(new DimensionalArrayRow(row));
	}

}

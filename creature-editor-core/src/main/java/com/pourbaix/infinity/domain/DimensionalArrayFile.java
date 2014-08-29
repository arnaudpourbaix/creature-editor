package com.pourbaix.infinity.domain;

import java.util.ArrayList;
import java.util.List;

public final class DimensionalArrayFile {

	private List<String> headers;
	private final List<DimensionalArrayRow> rows = new ArrayList<>(0);

	public DimensionalArrayRow findByColumn(String column, long value) throws DimensionalArrayFileException {
		DimensionalArrayRow result = null;
		int index = getHeaderIndex(column);
		for (DimensionalArrayRow row : rows) {
			String strVal = row.getColumns().get(index);
			if (!strVal.matches("0x\\d+|\\d+")) {
				continue;
			}
			long rowValue;
			if (strVal.substring(0, 2).equalsIgnoreCase("0x")) {
				rowValue = Long.parseLong(strVal.substring(2), 16);
			} else {
				rowValue = Long.parseLong(strVal);
			}
			if (rowValue == value) {
				result = row;
				break;
			}
		}
		return result;
	}

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

	private int getHeaderIndex(String name) throws DimensionalArrayFileException {
		int i = 0;
		boolean found = false;
		while (i < headers.size() && !found) {
			if (headers.get(i).equalsIgnoreCase(name)) {
				found = true;
			} else {
				i++;
			}
		}
		if (!found) {
			throw new DimensionalArrayFileException("Header not found", name);
		}
		return i;
	}

}

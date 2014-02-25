package com.pourbaix.infinity.datatype;

public class Flag {
	private long value;
	private int length;
	String[] table;
	String nodesc;

	public Flag(long value, int length, String[] stable) {
		this.value = value;
		this.length = length;
		nodesc = stable[0];
		table = new String[8 * length];
		for (int i = 1; i < stable.length; i++)
			table[i - 1] = stable[i];
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("( ");
		if (value == 0)
			sb.append(nodesc).append(' ');
		else {
			for (int i = 0; i < 8 * length; i++)
				if (isFlagSet(i)) {
					if (i < table.length && table[i] != null && !table[i].equals(""))
						sb.append(table[i]).append('(').append(i).append(") ");
					else
						sb.append("Unknown(").append(i).append(") ");
				}
		}
		sb.append(')');
		return sb.toString();
	}

	public String getString(int i) {
		return table[i];
	}

	public boolean isFlagSet(int i) {
		long bitnr = (long) Math.pow((double) 2, (double) i);
		return (value & bitnr) == bitnr;
	}

	public long getValue() {
		return value;
	}

}

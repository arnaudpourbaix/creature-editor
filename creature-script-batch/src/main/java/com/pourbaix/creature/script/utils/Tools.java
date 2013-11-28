package com.pourbaix.creature.script.utils;

public class Tools {

	public static boolean equalsIgnoreCase(final String s1, final String s2) {
		if (s1 == null || s2 == null) {
			return false;
		}
		return s1.toLowerCase().equals(s2.toLowerCase());
	}

	public static boolean containsIgnoreCase(final String s1, final String s2) {
		if (s1 == null || s2 == null) {
			return false;
		}
		return s1.toLowerCase().contains(s2.toLowerCase());
	}

	public static String encloseString(final String s) {
		return encloseString(s, '\"');
	}

	public static String encloseString(final String s, final char c) {
		if (c == '[' || c == ']') {
			return "[" + s + "]";
		} else if (c == '(' || c == ')') {
			return "(" + s + ")";
		}
		return c + s + c;
	}

}

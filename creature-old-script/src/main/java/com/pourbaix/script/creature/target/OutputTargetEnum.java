package com.pourbaix.script.creature.target;

public enum OutputTargetEnum {
	MYSELF("Myself"), ENEMY("<ENEMY>"), LASTSEEN("LastSeenBy(Myself)");

	private String output;

	private OutputTargetEnum(final String output) {
		this.output = output;
	}

	public String getOutput() {
		return this.output;
	}

}

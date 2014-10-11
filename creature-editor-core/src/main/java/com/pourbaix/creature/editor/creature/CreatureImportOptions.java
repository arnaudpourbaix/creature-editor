package com.pourbaix.creature.editor.creature;

import com.pourbaix.creature.editor.domain.Mod;

public class CreatureImportOptions {
	private Mod mod;
	private String resource;
	private boolean override;
	private boolean onlyName;

	public Mod getMod() {
		return mod;
	}

	public void setMod(Mod mod) {
		this.mod = mod;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public boolean isOverride() {
		return override;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	public boolean isOnlyName() {
		return onlyName;
	}

	public void setOnlyName(boolean onlyName) {
		this.onlyName = onlyName;
	}

}

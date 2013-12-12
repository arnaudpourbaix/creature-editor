package com.pourbaix.infinity.context;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;

public class GlobalContext {

	@Value("${game.directory}")
	private String gameDirectory;
	private File rootDirectory;

	public String getGameDirectory() {
		return gameDirectory;
	}

	public void setGameDirectory(String gameDirectory) {
		this.gameDirectory = gameDirectory;
	}

	public File getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(File rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

}

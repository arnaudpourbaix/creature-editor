package com.pourbaix.infinity.context;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;

import com.pourbaix.infinity.entity.GameVersionEnum;

public class GlobalContext {

	@Value("${default.language}")
	private String defaultLanguage;
	@Value("${default.language}")
	private String language;
	@Value("${game.version}")
	private GameVersionEnum gameVersion;
	@Value("${game.directory}")
	private File gameDirectory;
	private File userGameDirectory;
	private File languageDirectory;
	private File dialogFile;
	private File[] rootDirectories;

	public GameVersionEnum getGameVersion() {
		return gameVersion;
	}

	public void setGameVersion(GameVersionEnum gameVersion) {
		this.gameVersion = gameVersion;
	}

	public File getGameDirectory() {
		return gameDirectory;
	}

	public void setGameDirectory(File gameDirectory) {
		this.gameDirectory = gameDirectory;
	}

	public File getUserGameDirectory() {
		return userGameDirectory;
	}

	public void setUserGameDirectory(File userGameDirectory) {
		this.userGameDirectory = userGameDirectory;
	}

	public File getDialogFile() {
		return dialogFile;
	}

	public void setDialogFile(File dialogFile) {
		this.dialogFile = dialogFile;
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public File[] getRootDirectories() {
		return rootDirectories;
	}

	public void setRootDirectories(File[] rootDirectories) {
		this.rootDirectories = rootDirectories;
	}

	public File getLanguageDirectory() {
		return languageDirectory;
	}

	public void setLanguageDirectory(File languageDirectory) {
		this.languageDirectory = languageDirectory;
	}

}

package com.pourbaix.infinity.context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.pourbaix.infinity.datatype.GameVersionEnum;

public class GlobalContext {

	@Value("${default.language}")
	private String defaultLanguage;
	@Value("${default.language}")
	private String language;
	@Value("${game.version}")
	private GameVersionEnum gameVersion;
	@Value("${ignore.read.errors}")
	private boolean ignoreReadErrors;
	@Value("${game.directory}")
	private File gameDirectory;
	private File userGameProfileDirectory;
	private File languageDirectory;
	private File dialogFile;
	private File chitinKey;

	public boolean isEnhancedEdition() {
		return gameVersion == GameVersionEnum.BG1EE || gameVersion == GameVersionEnum.BG2EE;
	}

	public List<File> getRootDirectories() {
		List<File> rootDirs = new ArrayList<File>();
		if (userGameProfileDirectory != null) {
			rootDirs.add(userGameProfileDirectory);
		}
		if (languageDirectory != null) {
			rootDirs.add(languageDirectory);
		}
		rootDirs.add(gameDirectory);
		return rootDirs;
	}

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

	public File getUserGameProfileDirectory() {
		return userGameProfileDirectory;
	}

	public void setUserGameProfileDirectory(File userGameProfileDirectory) {
		this.userGameProfileDirectory = userGameProfileDirectory;
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

	public File getLanguageDirectory() {
		return languageDirectory;
	}

	public void setLanguageDirectory(File languageDirectory) {
		this.languageDirectory = languageDirectory;
	}

	public boolean isIgnoreReadErrors() {
		return ignoreReadErrors;
	}

	public void setIgnoreReadErrors(boolean ignoreReadErrors) {
		this.ignoreReadErrors = ignoreReadErrors;
	}

	public File getChitinKey() {
		return chitinKey;
	}

	public void setChitinKey(File chitinKey) {
		this.chitinKey = chitinKey;
	}

}

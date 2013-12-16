package com.pourbaix.infinity.resource;

import java.io.File;
import java.util.List;

import com.pourbaix.infinity.entity.GameVersionEnum;
import com.pourbaix.infinity.util.NIFile;

public class ResourceFactory {

	private static ResourceFactory instance;

	private GameVersionEnum gameVersion;
	private List<File> rootDirs;

	public static ResourceFactory getInstance() {
		if (ResourceFactory.instance == null) {
			ResourceFactory.instance = new ResourceFactory();
		}
		return ResourceFactory.instance;
	}

	public File getFile(String filename) {
		File file = NIFile.getFile(rootDirs.toArray(new File[rootDirs.size()]), filename);
		if (file.exists()) {
			return file;
		}
		// for (final File biffDir : biffDirs) {
		// file = new File(biffDir, filename);
		// if (file.exists())
		// return file;
		// }
		return null;
	}

	public boolean isEnhancedEdition() {
		return gameVersion == GameVersionEnum.BG1EE || gameVersion == GameVersionEnum.BG2EE;
	}

	public GameVersionEnum getGameVersion() {
		return gameVersion;
	}

	public void setGameVersion(GameVersionEnum gameVersion) {
		this.gameVersion = gameVersion;
	}

	public List<File> getRootDirs() {
		return rootDirs;
	}

	public void setRootDirs(List<File> rootDirs) {
		this.rootDirs = rootDirs;
	}

}

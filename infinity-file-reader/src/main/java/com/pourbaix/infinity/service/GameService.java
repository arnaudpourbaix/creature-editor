package com.pourbaix.infinity.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.context.GlobalContext;
import com.pourbaix.infinity.entity.GameVersionEnum;
import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.util.Constant;

@Service
public class GameService {

	private static final Logger logger = LoggerFactory.getLogger(GameService.class);

	@Resource
	private GlobalContext globalContext;
	@Resource
	private StringResource stringResource;

	public void openGame() throws GameException {
		if (globalContext.getGameDirectory() == null) {
			throw new GameException("game directory is not defined in configuration");
		}
		if (!globalContext.getGameDirectory().isDirectory()) {
			throw new GameException("game directory is invalid, please check configuration");
		}
		fetchUserGameDirectory();
		fetchLanguage();
		fetchRootDirectories();
		fetchDialogFile();
		loadResources();
		logger.debug("language:" + globalContext.getLanguage());
		try {
			String test = stringResource.getStringRef(500);
			logger.debug("test=" + test);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if (!new File(globalContext.getRootDirectory(), "chitin.key").exists()) {
		// throw new GameException("no chitin key in game directory");
		// }
		logger.debug("success");
	}

	public void loadResources() throws GameException {
		// Get resources from keyfile
		// keyfile.addBIFFResourceEntries(treeModel);
		stringResource.init(globalContext.getDialogFile());
		//
		// // Add other resources
		// for (final String extraDir : games[currentGame].extraDirs) {
		// for (final File root : rootDirs) {
		// File directory = NIFile.getFile(root, extraDir);
		// if (directory.exists())
		// treeModel.addDirectory((ResourceTreeFolder) treeModel.getRoot(), directory);
		// }
		// }
		//
		// boolean overrideInOverride = (BrowserMenuBar.getInstance() != null && BrowserMenuBar.getInstance().getOverrideMode() ==
		// BrowserMenuBar.OVERRIDE_IN_OVERRIDE);
		// for (final File rootDir : rootDirs) {
		// File overrideDir = NIFile.getFile(rootDir, OVERRIDEFOLDER);
		// if (overrideDir.exists()) {
		// File overrideFiles[] = overrideDir.listFiles();
		// for (final File overrideFile : overrideFiles) {
		// if (!overrideFile.isDirectory()) {
		// String filename = overrideFile.getName().toUpperCase();
		// ResourceEntry entry = getResourceEntry(filename);
		// if (entry == null) {
		// FileResourceEntry fileEntry = new FileResourceEntry(overrideFile, true);
		// treeModel.addResourceEntry(fileEntry, fileEntry.getTreeFolder());
		// } else if (entry instanceof BIFFResourceEntry) {
		// ((BIFFResourceEntry) entry).setOverride(true);
		// if (overrideInOverride) {
		// treeModel.removeResourceEntry(entry, entry.getExtension());
		// treeModel.addResourceEntry(new FileResourceEntry(overrideFile, true), OVERRIDEFOLDER);
		// }
		// }
		// }
		// }
		// }
		// }
		// treeModel.sort();
	}

	/**
	 * Attempts to find the user-profile game folder (supported by BG1EE and BG2EE)
	 */
	private void fetchUserGameDirectory() {
		if (globalContext.getGameVersion() != GameVersionEnum.BG1EE && globalContext.getGameVersion() != GameVersionEnum.BG2EE) {
			return;
		}
		File userHomeDirectory = FileSystemView.getFileSystemView().getDefaultDirectory();
		File userGameDirectory = new File(userHomeDirectory, globalContext.getGameVersion().getName());
		if (userGameDirectory.exists()) {
			globalContext.setUserGameDirectory(userGameDirectory);
		}

		String userGameDirectoryPrefix = null;
		String userGameDirectorySuffix = null;
		if (System.getProperty("os.name").contains(Constant.OperatingSystemName.WINDOWS)) {
			try {
				Process p = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
				p.waitFor();
				InputStream in = p.getInputStream();
				byte[] b = new byte[in.available()];
				in.read(b);
				in.close();
				String[] splitted = new String(b).split("\\s\\s+");
				userGameDirectoryPrefix = splitted[splitted.length - 1];
			} catch (Throwable t) {
				return;
			}
			userGameDirectorySuffix = globalContext.getGameVersion().getName();
		} else if (System.getProperty("os.name").contains(Constant.OperatingSystemName.MAC)) {
			userGameDirectoryPrefix = System.getProperty("user.home");
			userGameDirectorySuffix = File.separator + "Documents" + File.separator + globalContext.getGameVersion().getName();
		}

		if (userGameDirectoryPrefix == null || userGameDirectorySuffix == null) {
			return;
		}

		userGameDirectory = new File(userGameDirectoryPrefix, userGameDirectorySuffix);
		if (userGameDirectory.exists()) {
			globalContext.setUserGameDirectory(userGameDirectory);
		}
	}

	private void fetchLanguage() throws GameException {
		if (globalContext.getDefaultLanguage().isEmpty()) {
			throw new GameException("default language is not defined in configuration");
		}
		if (globalContext.getGameVersion() != GameVersionEnum.BG1EE && globalContext.getGameVersion() != GameVersionEnum.BG2EE) {
			return;
		}
		File iniFile = new File(globalContext.getUserGameDirectory(), Constant.INI_FILENAME);
		if (!iniFile.exists()) {
			return;
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(iniFile));
			String line = br.readLine();
			while (line != null) {
				if (line.contains("'Language'")) {
					String[] entries = line.split(",");
					if (entries.length == 3) {
						String lang = entries[2].replace('\'', ' ').trim();
						if (lang.matches("[a-z]{2}_[A-Z]{2}")) {
							if (new File(globalContext.getGameDirectory(), "lang" + File.separator + lang).exists()) {
								globalContext.setLanguage(lang);
								break;
							}
						}
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			logger.error("Error parsing " + Constant.INI_FILENAME + ". Using language defaults.");
		}
	}

	private void fetchRootDirectories() throws GameException {
		File langRoot = new File(globalContext.getGameDirectory(), "lang" + File.separator + globalContext.getLanguage());
		if (langRoot.exists()) {
			globalContext.setLanguageDirectory(langRoot);
		}
		if (globalContext.getUserGameDirectory() != null && langRoot.exists()) {
			globalContext.setRootDirectories(new File[] { langRoot, globalContext.getUserGameDirectory(), globalContext.getGameDirectory() });
		} else if (globalContext.getUserGameDirectory() != null) {
			globalContext.setRootDirectories(new File[] { globalContext.getUserGameDirectory(), globalContext.getGameDirectory() });
		} else if (langRoot.exists()) {
			globalContext.setRootDirectories(new File[] { langRoot, globalContext.getGameDirectory() });
		} else {
			globalContext.setRootDirectories(new File[] { globalContext.getGameDirectory() });
		}
	}

	private void fetchDialogFile() {
		if (globalContext.getLanguageDirectory() != null) {
			globalContext.setDialogFile(new File(globalContext.getLanguageDirectory(), Constant.DIALOG_FILENAME));
		} else {
			globalContext.setDialogFile(new File(globalContext.getGameDirectory(), Constant.DIALOG_FILENAME));
		}
	}
}

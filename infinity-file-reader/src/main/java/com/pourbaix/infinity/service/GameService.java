package com.pourbaix.infinity.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.context.GlobalContext;
import com.pourbaix.infinity.resource.ResourceFactory;
import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.resource.key.BiffResourceEntry;
import com.pourbaix.infinity.resource.key.FileResourceEntry;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.Constant;

@Service
public class GameService {

	private static final Logger logger = LoggerFactory.getLogger(GameService.class);

	@Resource
	private GlobalContext globalContext;

	public void openGame() throws ServiceException {
		checkGameDirectory();
		if (globalContext.isEnhancedEdition()) {
			fetchUserGameProfileDirectory();
			fetchLanguage();
		}
		fetchChitinKey();
		fetchDialogFile();
		ResourceFactory.getInstance().setGameVersion(globalContext.getGameVersion());
		ResourceFactory.getInstance().setRootDirs(globalContext.getRootDirectories());
		loadResources();
	}

	private void checkGameDirectory() throws ServiceException {
		if (globalContext.getGameDirectory() == null) {
			throw new ServiceException("game directory is not defined in configuration");
		}
		if (!globalContext.getGameDirectory().isDirectory()) {
			throw new ServiceException("game directory is invalid, please check configuration");
		}
	}

	/**
	 * Attempts to find the user-profile game folder (supported by BG1EE and BG2EE)
	 */
	private void fetchUserGameProfileDirectory() {
		File userHomeDirectory = FileSystemView.getFileSystemView().getDefaultDirectory();
		File userGameProfileDirectory = new File(userHomeDirectory, globalContext.getGameVersion().getName());
		if (userGameProfileDirectory.exists()) {
			globalContext.setUserGameProfileDirectory(userGameProfileDirectory);
			return;
		}

		String userGameProfileDirectoryPrefix = null;
		String userGameProfileDirectorySuffix = null;
		if (System.getProperty("os.name").contains(Constant.OperatingSystemName.WINDOWS)) {
			try {
				Process p = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
				p.waitFor();
				InputStream in = p.getInputStream();
				byte[] b = new byte[in.available()];
				in.read(b);
				in.close();
				String[] splitted = new String(b).split("\\s\\s+");
				userGameProfileDirectoryPrefix = splitted[splitted.length - 1];
			} catch (Throwable t) {
				return;
			}
			userGameProfileDirectorySuffix = globalContext.getGameVersion().getName();
		} else if (System.getProperty("os.name").contains(Constant.OperatingSystemName.MAC)) {
			userGameProfileDirectoryPrefix = System.getProperty("user.home");
			userGameProfileDirectorySuffix = File.separator + "Documents" + File.separator + globalContext.getGameVersion().getName();
		}

		if (userGameProfileDirectoryPrefix == null) {
			logger.warn("can not locate user game profile directory !");
			return;
		}

		userGameProfileDirectory = new File(userGameProfileDirectoryPrefix, userGameProfileDirectorySuffix);
		if (!userGameProfileDirectory.exists()) {
			logger.warn("can not locate user game profile directory !");
			return;
		}

		globalContext.setUserGameProfileDirectory(userGameProfileDirectory);
	}

	/**
	 * Attempts to find language and dialog directory (supported by BG1EE and BG2EE)
	 */
	private void fetchLanguage() throws ServiceException {
		try {
			if (globalContext.getDefaultLanguage().isEmpty() || !globalContext.getDefaultLanguage().matches("\\w{2}_\\w{2}")) {
				throw new ServiceException("default language is not defined or invalid");
			}
			File iniFile = new File(globalContext.getUserGameProfileDirectory(), Constant.INI_FILENAME);
			if (!iniFile.exists()) {
				throw new FileNotFoundException();
			}
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
		} catch (FileNotFoundException e) {
			logger.warn(Constant.INI_FILENAME + " not found. Using default language: " + globalContext.getDefaultLanguage().substring(0, 2));
		} catch (IOException e) {
			logger.warn("Error parsing " + Constant.INI_FILENAME + ". Using default language: " + globalContext.getDefaultLanguage().substring(0, 2));
		} finally {
			logger.info("setting language directory based on " + globalContext.getLanguage());
			File langDirectory = new File(globalContext.getGameDirectory(), "lang" + File.separator + globalContext.getLanguage());
			if (langDirectory.exists()) {
				globalContext.setLanguageDirectory(langDirectory);
			}
		}
	}

	private void fetchChitinKey() throws ServiceException {
		File chitinKey = new File(globalContext.getGameDirectory(), "chitin.key");
		if (!chitinKey.exists()) {
			throw new ServiceException("no chitin key in game directory !");
		}
		globalContext.setChitinKey(chitinKey);
	}

	private void fetchDialogFile() throws ServiceException {
		if (globalContext.getLanguageDirectory() != null) {
			globalContext.setDialogFile(new File(globalContext.getLanguageDirectory(), Constant.DIALOG_FILENAME));
		} else {
			globalContext.setDialogFile(new File(globalContext.getGameDirectory(), Constant.DIALOG_FILENAME));
		}
		if (!globalContext.getDialogFile().exists()) {
			throw new ServiceException(Constant.DIALOG_FILENAME + " not found !");
		}
	}

	private void loadResources() throws ServiceException {
		try {
			StringResource.getInstance().init(globalContext.getDialogFile(), globalContext.isEnhancedEdition());
			Keyfile.getInstance().init(globalContext.getChitinKey());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		// Add override resources
		for (final File rootDir : globalContext.getRootDirectories()) {
			File overrideDir = new File(rootDir, Constant.OVERRIDE_DIRECTORY);
			if (!overrideDir.exists() || !overrideDir.isDirectory()) {
				continue;
			}
			for (final File overrideFile : overrideDir.listFiles()) {
				if (overrideFile.isDirectory()) {
					continue;
				}
				String filename = overrideFile.getName().toUpperCase();
				ResourceEntry entry = Keyfile.getInstance().getResourceEntry(filename);
				if (entry == null) {
					ResourceEntry fileEntry = new FileResourceEntry(overrideFile);
					Keyfile.getInstance().addResourceEntry(fileEntry);
				} else if (entry instanceof BiffResourceEntry) {
					((BiffResourceEntry) entry).setOverrideFile(overrideFile);
				}
			}
		}
	}

}

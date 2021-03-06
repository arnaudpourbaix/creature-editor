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

import com.pourbaix.creature.editor.domain.Game;
import com.pourbaix.creature.editor.repository.GameRepository;
import com.pourbaix.creature.editor.service.ParameterService;
import com.pourbaix.infinity.context.ReaderContext;
import com.pourbaix.infinity.resource.ResourceFactory;
import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.resource.StringResourceException;
import com.pourbaix.infinity.resource.key.BiffResourceEntry;
import com.pourbaix.infinity.resource.key.FileResourceEntry;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.Constant;

@Service
public class GameService {

	private static final Logger logger = LoggerFactory.getLogger(GameService.class);

	private static final String UNDEFINED_GAME_DIRECTORY = "UNDEFINED_GAME_DIRECTORY";
	private static final String INVALID_GAME_DIRECTORY = "INVALID_GAME_DIRECTORY";
	private static final String UNDEFINED_GAME_VERSION = "UNDEFINED_GAME_VERSION";
	private static final String UNDEFINED_DEFAULT_LANGUAGE = "UNDEFINED_DEFAULT_LANGUAGE";
	private static final String MISSING_CHITIN_KEY = "MISSING_CHITIN_KEY";
	private static final String MISSING_DIALOG_FILE = "MISSING_DIALOG_FILE";
	private static final String INIT_DIALOG_FILE_ERROR = "INIT_DIALOG_FILE_ERROR";
	private static final String INIT_CHITIN_KEY_ERROR = "INIT_CHITIN_KEY_ERROR";

	@Resource
	private ReaderContext readerContext;

	@Resource
	private ParameterService parameterService;
	
	@Resource
	private GameRepository gameRepository;

	public void openGame() throws ServiceException {
		if (readerContext.isGameOpened()) {
			return;
		}
		checkGameDirectory();
		checkGameVersion();
		if (readerContext.isEnhancedEdition()) {
			fetchUserGameProfileDirectory();
			fetchLanguage();
		}
		fetchChitinKey();
		fetchDialogFile();
		ResourceFactory.getInstance().setGameVersion(readerContext.getGameVersion());
		ResourceFactory.getInstance().setRootDirs(readerContext.getRootDirectories());
		loadResources();
		readerContext.setGameOpened(true);
	}

	public void closeGame() {
		if (!readerContext.isGameOpened()) {
			return;
		}
		readerContext.setGameOpened(false);
	}
	
	public Game getGame() {
		return gameRepository.findOne(parameterService.getGameVersion());
	}

	private void checkGameDirectory() throws ServiceException {
		readerContext.setGameDirectory(parameterService.getGameDirectory());
		if (readerContext.getGameDirectory() == null) {
			throw new ServiceException(UNDEFINED_GAME_DIRECTORY);
		}
		if (!readerContext.getGameDirectory().isDirectory()) {
			throw new ServiceException(INVALID_GAME_DIRECTORY);
		}
	}

	private void checkGameVersion() throws ServiceException {
		readerContext.setGameVersion(parameterService.getGameVersion());
		if (readerContext.getGameVersion() == null) {
			throw new ServiceException(UNDEFINED_GAME_VERSION);
		}
	}

	/**
	 * Attempts to find the user-profile game folder (supported by BG1EE and BG2EE)
	 */
	private void fetchUserGameProfileDirectory() {
		File userHomeDirectory = FileSystemView.getFileSystemView().getDefaultDirectory();
		File userGameProfileDirectory = new File(userHomeDirectory, readerContext.getGameVersion().getName());
		if (userGameProfileDirectory.exists()) {
			readerContext.setUserGameProfileDirectory(userGameProfileDirectory);
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
			userGameProfileDirectorySuffix = readerContext.getGameVersion().getName();
		} else if (System.getProperty("os.name").contains(Constant.OperatingSystemName.MAC)) {
			userGameProfileDirectoryPrefix = System.getProperty("user.home");
			userGameProfileDirectorySuffix = File.separator + "Documents" + File.separator + readerContext.getGameVersion().getName();
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

		readerContext.setUserGameProfileDirectory(userGameProfileDirectory);
	}

	/**
	 * Attempts to find language and dialog directory (supported by BG1EE and BG2EE)
	 */
	private void fetchLanguage() throws ServiceException {
		try {
			readerContext.setDefaultLanguage(parameterService.getDefaultLanguage());
			readerContext.setLanguage(readerContext.getDefaultLanguage());
			if (readerContext.getDefaultLanguage().isEmpty() || !readerContext.getDefaultLanguage().matches("\\w{2}_\\w{2}")) {
				throw new ServiceException(UNDEFINED_DEFAULT_LANGUAGE);
			}
			File iniFile = new File(readerContext.getUserGameProfileDirectory(), Constant.INI_FILENAME);
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
							if (new File(readerContext.getGameDirectory(), "lang" + File.separator + lang).exists()) {
								readerContext.setLanguage(lang);
								break;
							}
						}
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			logger.warn(Constant.INI_FILENAME + " not found. Using default language: " + readerContext.getDefaultLanguage().substring(0, 2));
		} catch (IOException e) {
			logger.warn("Error parsing " + Constant.INI_FILENAME + ". Using default language: " + readerContext.getDefaultLanguage().substring(0, 2));
		} finally {
			logger.info("setting language directory based on " + readerContext.getLanguage());
			File langDirectory = new File(readerContext.getGameDirectory(), "lang" + File.separator + readerContext.getLanguage());
			if (langDirectory.exists()) {
				readerContext.setLanguageDirectory(langDirectory);
			}
		}
	}

	private void fetchChitinKey() throws ServiceException {
		File chitinKey = new File(readerContext.getGameDirectory(), "chitin.key");
		if (!chitinKey.exists()) {
			throw new ServiceException(MISSING_CHITIN_KEY);
		}
		readerContext.setChitinKey(chitinKey);
	}

	private void fetchDialogFile() throws ServiceException {
		if (readerContext.getLanguageDirectory() != null) {
			readerContext.setDialogFile(new File(readerContext.getLanguageDirectory(), Constant.DIALOG_FILENAME));
		} else {
			readerContext.setDialogFile(new File(readerContext.getGameDirectory(), Constant.DIALOG_FILENAME));
		}
		if (!readerContext.getDialogFile().exists()) {
			throw new ServiceException(MISSING_DIALOG_FILE);
		}
	}

	private void loadResources() throws ServiceException {
		try {
			StringResource.getInstance().init(readerContext.getDialogFile(), readerContext.isEnhancedEdition());
		} catch (StringResourceException e) {
			throw new ServiceException(INIT_DIALOG_FILE_ERROR);
		}
		try {
			Keyfile.getInstance().init(readerContext.getChitinKey());
		} catch (IOException e) {
			throw new ServiceException(INIT_CHITIN_KEY_ERROR);
		}
		// Add override resources
		for (final File rootDir : readerContext.getRootDirectories()) {
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

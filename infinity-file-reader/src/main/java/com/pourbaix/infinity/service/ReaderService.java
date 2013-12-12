package com.pourbaix.infinity.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.context.GlobalContext;
import com.pourbaix.infinity.entity.GameVersionEnum;

@Service
public class ReaderService {

	private static final Logger logger = LoggerFactory.getLogger(ReaderService.class);

	@Resource
	private GlobalContext globalContext;

	@Autowired
	private GameVersionService gameVersionService;

	public void process(final String[] args) {
		// if (args.length == 0) {
		// logger.error("you must provide a file or directory parameter !");
		// return;
		// }
		try {
			GameVersionEnum gameVersion = gameVersionService.getVersion();
			logger.debug(gameVersion.toString());
		} catch (GameVersionException e) {
			logger.error(e.getMessage());
		}
		// File f = new File(args[0]);
		// if (f.isDirectory()) {
		// String path = args[0];
		// final FilenameFilter filter = new FilenameFilter() {
		// @Override
		// public boolean accept(final File dir, final String name) {
		// return name.toUpperCase().endsWith(Constant.SCRIPT_EXT);
		// }
		// };
		// final String[] chld = f.list(filter);
		// for (final String c : chld) {
		// final String filename = path + "\\" + c;
		// generateFile(filename);
		// }
		// } else if (f.isFile()) {
		// generateFile(args[0]);
		// } else {
		// logger.error("bad path: " + args[0]);
		// }
	}

}

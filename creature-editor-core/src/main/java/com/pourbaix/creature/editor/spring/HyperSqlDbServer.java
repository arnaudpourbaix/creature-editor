package com.pourbaix.creature.editor.spring;

import java.io.IOException;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

public class HyperSqlDbServer implements SmartLifecycle {

	private final Logger logger = LoggerFactory.getLogger(HyperSqlDbServer.class);

	private HsqlProperties properties;
	private Server server;
	private boolean running = false;

	public HyperSqlDbServer() {
		String path = ClassLoader.getSystemClassLoader().getResource(".").getPath();
		path = "C:\\Users\\Arnaud\\git\\creature-editor\\launcher\\database\\creatureEditor";
		properties = new HsqlProperties();
		properties.setProperty("server.database.0", "file:" + path);
		properties.setProperty("server.dbname.0", "creatureEditor");
		properties.setProperty("server.remote_open", true);
	}

	@Override
	public boolean isRunning() {
		if (server != null)
			server.checkRunning(running);
		return running;
	}

	@Override
	public void start() {
		if (server == null) {
			logger.info("Starting HSQL server...");
			server = new Server();
			try {
				server.setProperties(properties);
				// server.start();
				running = true;
			} catch (AclFormatException afe) {
				logger.error("Error starting HSQL server.", afe);
			} catch (IOException e) {
				logger.error("Error starting HSQL server.", e);
			}
		}
	}

	@Override
	public void stop() {
		logger.info("Stopping HSQL server...");
		if (server != null) {
			server.stop();
			running = false;
		}
	}

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable runnable) {
		stop();
		runnable.run();
	}
}
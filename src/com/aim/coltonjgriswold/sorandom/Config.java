package com.aim.coltonjgriswold.sorandom;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	
	public static File dataDir;
	
	public static void initialize(FileConfiguration config, File pluginDir,Logger log) {
		try {
			dataDir = pluginDir;
			if (!dataDir.exists()) {
				dataDir.mkdir();
			}
		} catch (Exception ex) {
			log.logp(Level.SEVERE, "ERROR", "Config Is Unable To Load!", "Please Check Your Configuration!", ex);
		}
	}
}

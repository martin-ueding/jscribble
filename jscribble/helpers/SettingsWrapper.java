// Copyright © 2011 Martin Ueding <dev@martin-ueding.de>

/*
 * This file is part of jscribble.
 *
 * jscribble is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 2 of the License, or (at your option)
 * any later version.
 *
 * jscribble is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * jscribble.  If not, see <http://www.gnu.org/licenses/>.
 */

package jscribble.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import jscribble.NoteBookProgram;

/**
 * Serves as an interface to the config file. Provides several convenience
 * functions for various types in lookup.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class SettingsWrapper {
	/**
	 * Main config used for every settings as of now.
	 */
	private static Properties mainConfig;

	/**
	 * Looks up a boolean value in the config file.
	 *
	 * @param key Key to look up.
	 * @param defaultValue Default value in case there is no such key.
	 * @return Config value or default as boolean.
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		return Boolean.parseBoolean(getConfig().getProperty(key, String.valueOf(defaultValue)));
	}

	/**
	 * Gets the config, initializes if needed.
	 *
	 * @return Initialized config.
	 */
	private static Properties getConfig() {
		if (mainConfig == null) {
			initMainConfig();
		}

		return mainConfig;
	}

	/**
	 * Looks up a integer value in the config file.
	 *
	 * @param key Key to look up.
	 * @param defaultValue Default value in case there is no such key.
	 * @return Config value or default as integer.
	 */
	public static int getInteger(String key, int defaultValue) {
		return Integer.parseInt(getConfig().getProperty(key, String.valueOf(defaultValue)));
	}

	/**
	 * Looks up a string value in the config file.
	 *
	 * @param key Key to look up.
	 * @param defaultValue Default value in case there is no such key.
	 * @return Config value or default as string.
	 */
	public static String getString(String key, String defaultValue) {
		return getConfig().getProperty(key, defaultValue);
	}

	/**
	 * Creates a new config and loads a config file if there is one.
	 */
	private static void initMainConfig() {
		mainConfig = new Properties();
		try {
			File configfile = new File(NoteBookProgram.getDotDir() + File.separator +
			        "config.txt");
			if (configfile.exists()) {
				mainConfig.load(new FileInputStream(configfile));
			}
		}
		catch (FileNotFoundException e) {
			Logger.handleError(Localizer.get("Could not find the config file. (This should *not* happen!)"));
			e.printStackTrace();
		}
		catch (IOException e) {
			Logger.handleError(Localizer.get(
			            "IO error while reading config file."));
			e.printStackTrace();
		}
	}
}

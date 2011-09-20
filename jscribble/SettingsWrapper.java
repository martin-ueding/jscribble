package jscribble;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SettingsWrapper {
	/**
	 * Main config used for every settings as of now.
	 */
	private static Properties mainConfig;

	public static boolean getBoolean(String key, boolean defaultValue) {
		return Boolean.parseBoolean(getConfig().getProperty(key, String.valueOf(defaultValue)));
	}

	/**
	 * Retrieves a config value from the file.
	 *
	 * @param key Name of the option to look up.
	 * @return Value of the option.
	 */
	private static Properties getConfig() {
		if (mainConfig == null) {
			initMainConfig();
		}

		return mainConfig;
	}

	public static int getInteger(String key, int defaultValue) {
		return Integer.parseInt(getConfig().getProperty(key, String.valueOf(defaultValue)));
	}

	public static String getString(String key, String defaultValue) {
		return getConfig().getProperty(key, defaultValue);
	}

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
			NoteBookProgram.handleError(Localizer.get("Could not find the config file. (This should *not* happen!)"));
			e.printStackTrace();
		}
		catch (IOException e) {
			NoteBookProgram.handleError(Localizer.get(
			            "IO error while reading config file."));
			e.printStackTrace();
		}
	}
}

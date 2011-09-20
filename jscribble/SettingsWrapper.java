package jscribble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class SettingsWrapper {
	/**
	 * Main config used for every settings as of now.
	 */
	private static Properties mainConfig;

	/**
	 * Retrieves a config value from the file.
	 *
	 * @param key Name of the option to look up.
	 * @return Value of the option.
	 */
	private static Properties getConfig() {
		if (mainConfig == null) {
			mainConfig = new Properties();
			try {
				File configfile = new File(NoteBookProgram.getDotDir() + File.separator +
				        "config.txt");
				if (configfile.exists()) {
					mainConfig.load(new FileInputStream(configfile));
				}
				// Create a new config file if there is none, fill it with the
				// defaults from the distribution.
				else {
					NoteBookProgram.log(NoteBookProgram.class.getClass().getName(),
					    Localizer.get("Could not find config file. Writing a default one."));
					InputStreamReader isr = new InputStreamReader(
					    ClassLoader.getSystemClassLoader()
					    .getResourceAsStream("jscribble/config.txt"));
					BufferedReader br = new BufferedReader(isr);
					FileWriter fw = new FileWriter(configfile);
					String line;
					while ((line = br.readLine()) != null) {
						fw.write(line + "\n");
					}
					br.close();
					isr.close();
					fw.close();
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

		return mainConfig;
	}
	
	public static String getString(String key, String defaultValue) {
		return getConfig().getProperty(key, defaultValue);
	}
	
	public static int getInteger(String key, int defaultValue) {
		return Integer.parseInt(getConfig().getProperty(key, String.valueOf(defaultValue)));
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return Boolean.parseBoolean(getConfig().getProperty(key, String.valueOf(defaultValue)));
	}
}

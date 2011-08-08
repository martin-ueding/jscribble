// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

/*
 * This file is part of jscribble.
 *
 * Foobar is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 *
 * jscribble is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * jscribble.  If not, see <http://www.gnu.org/licenses/>.
 */

package jscribble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JOptionPane;

import jscribble.selectionWindow.NoteBookSelectionWindow;

/**
 * The main program.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class NoteBookProgram {
	/**
	 * The folder where everything is stored.
	 */
	private static File dotDir = new File(System.getProperty("user.home") +
	        File.separator + "." + NoteBookProgram.getProgramname());


	/**
	 * Generates a "generated by <program name> <version>" string.
	 *
	 * @return version string
	 */
	public static String generatedComment() {
		return "generated by " + NoteBookProgram.getProgramname() + " " +
		       VersionName.version;
	}


	/**
	 * Getter for dotDir.
	 */
	public static File getDotDir() {
		return dotDir;
	}


	/**
	 * Returns the name of the program.
	 */
	public static String getProgramname() {
		return "jscribble";
	}


	/**
	 * Handles some error message centrally, right now it just displays a
	 * dialog box with the error message.
	 *
	 * @param errorMessage error message
	 */
	public static void handleError(String errorMessage) {
		log("ERROR", errorMessage);
		JOptionPane.showMessageDialog(null, errorMessage);
	}


	/**
	 * Writes a message to a log file.
	 *
	 * @param reportingClass name of the reporting class
	 * @param message message
	 */
	public static void log(String reportingClass, String message) {
		if (debug) {
			Calendar c = Calendar.getInstance();
			String date = String.format("%d-%02d-%02d %02d:%02d:%02d.%03d",
			        c.get(Calendar.YEAR),
			        c.get(Calendar.MONTH),
			        c.get(Calendar.DAY_OF_MONTH),
			        c.get(Calendar.HOUR_OF_DAY),
			        c.get(Calendar.MINUTE),
			        c.get(Calendar.SECOND),
			        c.get(Calendar.MILLISECOND)
			                           );
			String output =  date + " " + reportingClass + ":\t" + message;
			System.out.println(output);
		}
	}

	private static boolean debug = false;


	/**
	 * Displays the NotebookSelectionWindow.
	 *
	 * @param args CLI arguments
	 */
	public static void main(String[] args) {
		for (String string : args) {
			if (string.equalsIgnoreCase("-v")) {
				debug = true;
			}
		}



		printVersionIfNeeded(args);

		log(getProgramname(), Localizer.get("Starting up."));

		showSelectionWindow();

		log(NoteBookProgram.class.getClass().getName(),
		    Localizer.get("Entering interactive mode."));
	}

	private static Properties mainConfig;


	public static String getConfigValue(String key) {
		if (mainConfig == null) {
			mainConfig = new Properties();
			try {
				File configfile = new File(getDotDir() + File.separator + "config.txt");
				if (configfile.exists()) {
					mainConfig.load(new FileInputStream(configfile));
				}
				else {
					log(NoteBookProgram.class.getClass().getName(), Localizer.get("Could not find config file. Writing a default one."));
					InputStreamReader isr = new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream("jscribble/config.txt"));
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
				handleError(Localizer.get("Could not find the config file. (This should *not* happen!)"));
				e.printStackTrace();
			}
			catch (IOException e) {
				handleError(Localizer.get("IO error while reading config file."));
				e.printStackTrace();
			}
		}

		return mainConfig.getProperty(key, "");
	}


	/**
	 * Prints version number and exists if there is a "--version" argument.
	 *
	 * @param args CLI arguments
	 */
	private static void printVersionIfNeeded(String[] args) {
		if (args.length > 0 && args[0].equals("--version")) {
			System.out.println(String.format(Localizer.get("Version: %s"),
			        VersionName.version));
			System.exit(0);
		}
	}


	/**
	 * Shows the NotebookSelectionWindow.
	 */
	private static void showSelectionWindow() {
		NoteBookSelectionWindow nsw = new NoteBookSelectionWindow();
		nsw.showDialog();
	}
}

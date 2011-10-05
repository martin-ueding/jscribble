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

package jscribble;

import java.io.File;

import jscribble.helpers.Localizer;
import jscribble.helpers.Logger;
import jscribble.helpers.Config;
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
	private static File dotDir;

	/**
	 * Getter for dotDir.
	 */
	public static File getDotDir() {
		dotDir = new File(
		    System.getProperty("user.home") +
		    File.separator + "." + NoteBookProgram.getProgramname()
		);
		return dotDir;
	}

	/**
	 * Returns the name of the program.
	 */
	public static String getProgramname() {
		return Config.getString("program_name");
	}

	/**
	 * Displays the NotebookSelectionWindow.
	 *
	 * @param args CLI arguments
	 */
	public static void main(String[] args) {
		// Parse the command line options.
		for (int i = 0; i < args.length; i++) {
			String string = args[i];
			if (string.equalsIgnoreCase("-v")) {
				Logger.setDebug(true);
			}
			else {
				if (string.startsWith("--") && i + 1 < args.length && !args[i+1].startsWith("--")) {
					String newValue = args[i+1];
					String key = string.substring(2);
					Config.set(key, newValue);
				}
			}
		}

		printVersionIfNeeded(args);

		Logger.log(getProgramname(), Localizer.get("Starting up."));

		showSelectionWindow();

		Logger.log(NoteBookProgram.class.getClass().getName(),
		           Localizer.get("Entering interactive mode."));
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

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

import java.util.Calendar;

import javax.swing.JOptionPane;

/**
 * Handles logging and error reporting
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class Logger {
	/**
	 * Whether to show debug information by default. This can be overridden by
	 * the "-v" command line option.
	 */
	private static boolean debug = false;

	/**
	 * Handles some error message centrally, right now it just displays a
	 * dialog box with the error message.
	 *
	 * @param errorMessage error message
	 */
	public static void handleError(String errorMessage) {
		log("ERROR", errorMessage);
		JOptionPane.showMessageDialog(null, errorMessage);
		System.exit(1);
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

	public static void setDebug(boolean b) {
		debug = b;
	}
}

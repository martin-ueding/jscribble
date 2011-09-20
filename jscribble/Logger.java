package jscribble;

import java.util.Calendar;

import javax.swing.JOptionPane;

public class Logger {
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
	 * Whether to show debug information by default. This can be overridden by
	 * the "-v" command line option.
	 */
	private static boolean debug = false;

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

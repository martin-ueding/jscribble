// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import javax.swing.JOptionPane;

/**
 * The main program.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class NoteBookProgram {
	/**
	 * Displays the NotebookSelectionWindow.
	 *
	 * @param args ignored CLI arguments
	 */
	public static void main(String[] args) {
		// TODO add --version CLI parameter to show the version

		NotebookSelectionWindow nsw = new NotebookSelectionWindow();
		nsw.showDialog();
	}


	/**
	 * Handles some error message centrally, right now it just displays a dialog box with the error message.
	 *
	 * @param string error message
	 */
	public static void handleError(String string) {
		JOptionPane.showMessageDialog(null, string);
	}

	/**
	 * Generates a "generated by <programname> <version>" string.
	 *
	 * @return version string
	 */
	public static String generatedComment() {
		return "generated by jscribble " + VersionName.version;
	}

	/**
	 * Suffix that is appended to every NoteBook configuration file.
	 */
	public static String configFileSuffix = ".jscribble-notebook";


	public static void log(String string) {
		System.out.println(string);
	}

}
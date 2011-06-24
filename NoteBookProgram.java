// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import javax.swing.JOptionPane;


public class NoteBookProgram {
	public static void main(String[] args) {
		NotebookSelectionWindow nsw = new NotebookSelectionWindow();
		nsw.showDialogue();
	}

	public static void handleError(String string) {
		JOptionPane.showMessageDialog(null, string);
	}
}

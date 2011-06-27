package jscribble.selectionWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScribbleActionListener implements ActionListener {
	private NotebookSelectionWindow window;

	public ScribbleActionListener(NotebookSelectionWindow window) {
		super();
		this.window = window;
	}

	/**
	 * Triggers the deletion of a NoteBook.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		window.scribbleEvent();
	}
}

package jscribble.selectionWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteActionListener implements ActionListener {


	private NotebookSelectionWindow window;

	public DeleteActionListener(NotebookSelectionWindow window) {
		super();
		this.window = window;
	}

	/**
	 * Triggers the deletion of a NoteBook.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		window.deleteEvent();
	}

}

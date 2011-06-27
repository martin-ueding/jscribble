package jscribble.selectionWindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ListListener extends MouseAdapter {
	private NotebookSelectionWindow window;

	public ListListener(NotebookSelectionWindow window) {
		this.window = window;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {

			window.openEvent();
		}
	}
}

// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package jscribble.selectionWindow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import jscribble.notebook.NoteBook;

class MovementListener implements KeyListener {
	private final NoteBook notebook;

	public MovementListener(NoteBook notebook) {
		this.notebook = notebook;
	}

	public void keyPressed(KeyEvent arg0) {}

	public void keyReleased(KeyEvent ev) {
		if (ev.getKeyChar() == 'j' ||
		        ev.getKeyCode() == KeyEvent.VK_DOWN ||
		        ev.getKeyCode() == KeyEvent.VK_RIGHT ||
		        ev.getKeyCode() == KeyEvent.VK_SPACE ||
		        ev.getKeyCode() == KeyEvent.VK_ENTER) {
			notebook.goForward();
		}

		if (ev.getKeyChar() == 'k' ||
		        ev.getKeyCode() == KeyEvent.VK_UP ||
		        ev.getKeyCode() == KeyEvent.VK_LEFT ||
		        ev.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			notebook.goBackwards();
		}

		if (ev.getKeyChar() == 'f' ||
		        ev.getKeyCode() == KeyEvent.VK_HOME) {
			notebook.gotoFirst();
		}

		if (ev.getKeyChar() == 'l' ||
		        ev.getKeyCode() == KeyEvent.VK_END) {
			notebook.gotoLast();
		}
	}

	public void keyTyped(KeyEvent arg0) {}
}

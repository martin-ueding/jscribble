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

package jscribble.selectionWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tells the NotebookSelectionWindow to perform an action.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
class ScribbleActionListener implements ActionListener {
	/**
	 * Window to notify.
	 */
	private NoteBookSelectionWindow window;

	/**
	 * Creates a new listener.
	 *
	 * @param window Window to relay actions to.
	 */
	public ScribbleActionListener(NoteBookSelectionWindow window) {
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

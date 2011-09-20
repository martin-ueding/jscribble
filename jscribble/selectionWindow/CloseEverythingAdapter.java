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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.JFrame;

import jscribble.helpers.Localizer;
import jscribble.helpers.Logger;
import jscribble.notebook.NoteBook;

class CloseEverythingAdapter extends WindowAdapter {
	private LinkedList<NoteBook> openedNotebooks;
	private JFrame frame;

	public CloseEverythingAdapter(LinkedList<NoteBook> openedNotebooks,
	        JFrame frame) {
		super();
		this.openedNotebooks = openedNotebooks;
		this.frame = frame;
	}

	public void windowClosing(WindowEvent winEvt) {
		for (NoteBook notebook : openedNotebooks) {
			notebook.saveToFiles();
			Logger.log(getClass().getName(),
			           String.format(Localizer.get("Closing NoteBook \"%s\"."),
			                   notebook.getName()));
		}
		frame.setVisible(false);
	}
}

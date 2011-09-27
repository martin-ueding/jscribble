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

import java.util.LinkedList;

import jscribble.notebook.NoteBook;

public class ShutdownHook extends Thread {
	/**
	 * All opened NoteBook.
	 */
	private LinkedList<NoteBook> openedNotebooks;

	/**
	 * Hooks a new adapter to the closing.
	 *
	 * @param openedNotebooks List with opened NoteBook
	 * @param frame Frame to close.
	 */
	public ShutdownHook() {
		super();

		openedNotebooks = new LinkedList<NoteBook>();
	}

	public void run() {
		Logger.log(getClass().getName(), Localizer.get("Shutting down …"));


		for (NoteBook notebook : openedNotebooks) {
			notebook.saveToFiles();
			Logger.log(getClass().getName(),
			           String.format(Localizer.get("Closing NoteBook \"%s\"."),
			                   notebook.getName()));
		}

		Logger.log(getClass().getName(), Localizer.get("Everything saved properly. Thanks for waiting!"));
	}

	public void add(NoteBook notebook) {
		openedNotebooks.add(notebook);
	}
}

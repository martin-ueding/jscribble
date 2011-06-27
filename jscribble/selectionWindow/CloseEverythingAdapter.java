// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package jscribble.selectionWindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.JFrame;

import jscribble.NoteBookProgram;
import jscribble.notebook.NoteBook;

public final class CloseEverythingAdapter extends WindowAdapter {
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
			NoteBookProgram.log(getClass().getName(), String.format("Closing NoteBook \"%s\".", notebook.getName()));
		}
		frame.setVisible(false);
	}
}

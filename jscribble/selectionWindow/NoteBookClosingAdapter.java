// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package jscribble.selectionWindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import jscribble.Localizer;
import jscribble.NoteBookProgram;
import jscribble.notebook.NoteBook;

class NoteBookClosingAdapter extends WindowAdapter {
	private final NoteBook notebook;
	private final JFrame f;

	public NoteBookClosingAdapter(NoteBook notebook, JFrame f) {
		this.notebook = notebook;
		this.f = f;
	}

	public void windowClosing(WindowEvent winEvt) {
		notebook.saveToFiles();
		NoteBookProgram.log(getClass().getName(), String.format(Localizer.get("Closing NoteBook \"%s\"."), notebook.getName()));
		f.setVisible(false);
	}
}

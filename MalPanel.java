// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class MalPanel extends JPanel {
	public MalPanel (NoteBook notebook) {
		this.notebook = notebook;
		notebook.setDoneDrawing(new Redrawer(this));
	}
	private NoteBook notebook;
	ImageObserver io = this;
	protected void paintComponent (Graphics g) {		
		Graphics2D g2 = (Graphics2D)g;

		g2.drawImage(notebook.getCurrentImage(), 0, 0, io);
	}
}

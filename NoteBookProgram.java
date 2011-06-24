// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class NoteBookProgram {
	public static final int width = 1024, height = 600;

	static DrawPanel panel;

	static NoteBook notebook;

	// TODO add a better chooser for notebooks

	public static void main(String[] args) {
		NotebookSelectionWindow nsw = new NotebookSelectionWindow(width, height);
	}

	public static void openNotebook(final NoteBook notebook) {
		JFrame f = new JFrame("Notebook");
		f.setSize(width, height);

		f.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				System.out.println("caught exit event");
				notebook.saveToFiles();
				System.exit(0);
			}
		});


		panel = new DrawPanel(notebook);
		f.add(panel);


		f.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {}

			public void keyReleased(KeyEvent ev) {
				if (ev.getKeyChar() == 'j') {
					notebook.forward();
				}

				if (ev.getKeyChar() == 'k') {
					notebook.backward();
				}

			}

			public void keyTyped(KeyEvent arg0) {}
		});


		ColonListener cl = new ColonListener(panel);
		f.addKeyListener(cl);
		cl.addChangeListener(new Redrawer(panel));


		if (Toolkit.getDefaultToolkit().getScreenSize().equals(new Dimension(width, height))) {
			GraphicsDevice myDevice = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

			if (myDevice.isFullScreenSupported()) {
				f.setUndecorated(true);
				f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
				myDevice.setFullScreenWindow(f);

				f.setLocation(0, 0);
			}
		}

		f.setVisible(true);
	}
}

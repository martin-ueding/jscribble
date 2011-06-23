// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Notizbuch {
	public static final int br = 1024, ho = 600;

	static MalPanel panel;

	static NoteBook notebook;

	public static void main(String[] args) {
		String nickname = JOptionPane.showInputDialog("Nickname of your Notebook:");

		File in = null;
		if (nickname != null) {

			JFileChooser ladenChooser = new JFileChooser();
			ladenChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);


			int result = ladenChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				in = ladenChooser.getSelectedFile();
			}

			// if there is no file selected, abort right here
			if (in == null) {
				System.exit(1);
			}
		}


		notebook = new NoteBook(br, ho, in, nickname);
		JFrame f = new JFrame("Notebook");
		f.setSize(br, ho);

		f.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				notebook.saveToFiles();
				System.exit(0);
			}
		});


		panel = new MalPanel(notebook);
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


		if (Toolkit.getDefaultToolkit().getScreenSize().equals(new Dimension(br, ho))) {
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

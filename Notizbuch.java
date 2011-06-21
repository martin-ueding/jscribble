// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Notizbuch {
	public static final int br = 1024, ho = 600;
	public static final String preDir = "";
	
	static MalPanel panel;
	
	static NoteBook notebook;
	
	public static void main (String[] args) {	
		notebook = new NoteBook(br, ho);	
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
		
		
		panel.addMouseMotionListener(new PaintListener(notebook));
		
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
		

		
		GraphicsDevice myDevice = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		if (myDevice.isFullScreenSupported()) {
			f.setUndecorated(true);
			f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
			myDevice.setFullScreenWindow(f);

			f.setLocation(0, 0);

		}
		else
			System.exit(0);

		f.setVisible(true);

		
	}
}

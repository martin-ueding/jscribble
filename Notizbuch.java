// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Notizbuch {
	public static final int br = 1024, ho = 600;
	public static final String preDir = "";
	
	static MalPanel panel;
	
	static NoteBook notebook;
	
	public static void main (String[] args) {	
		notebook = new NoteBook(br, ho);	
		JFrame f = new JFrame("neuenotiz");
		f.setSize(br, ho);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		

		f.setVisible(true);
		
//		GraphicsDevice myDevice = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//		
//		if (myDevice.isFullScreenSupported()) {
//			f.setUndecorated(true);
//			f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
//			myDevice.setFullScreenWindow(f);
//
//			f.setLocation(0, 0);
//
//		}
//		else
//			System.exit(0);

		
	}
}

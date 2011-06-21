// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class MalPanel extends JPanel {
	ImageObserver io = this;
	protected void paintComponent (Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
		
		Graphics2D g2 = (Graphics2D)g;

		g2.drawImage(Notizbuch.img, 0, 0, io);
	}
}

// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


/**
 * Listens to mouse movements on the displaying panel and updates the notebook.
 */
public class PaintListener implements MouseMotionListener, MouseListener {

	private NoteBook notebook;
	
	private Point lastPosition = new Point(-1, -1);

	public PaintListener(NoteBook notebook) {
		this.notebook = notebook;
	}

	public void mouseDragged(MouseEvent arg0) {
		notebook.drawLine(lastPosition.x, lastPosition.y, arg0.getX(), arg0.getY());
		lastPosition = arg0.getPoint();
	}

	public void mouseMoved(MouseEvent arg0) {
		lastPosition = arg0.getPoint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		notebook.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}

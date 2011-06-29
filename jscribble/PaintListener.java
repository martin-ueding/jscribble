// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package jscribble;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import jscribble.notebook.NoteBook;


/**
 * Listens to mouse movements on the displaying panel and updates the NoteBook.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class PaintListener implements MouseMotionListener, MouseListener {
	/**
	 * The NoteBook which receives the painting.
	 */
	private NoteBook notebook;


	/**
	 * The last position of the pointer. Since the line commands needs two
	 * points, but the mouseDragged() only gives the current location, the
	 * class needs to remember where the pointer was before.
	 */
	private Point lastPosition = new Point(-1, -1);


	/**
	 * Generates a new Listener that relays its commands to a given NoteBook.
	 * @param notebook
	 */
	public PaintListener(NoteBook notebook) {
		this.notebook = notebook;
	}


	/**
	 * Draws a single dot when the mouse is clicked.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
			notebook.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
		}
	}


	/**
	 * Tells the NoteBook to draw a line.
	 */
	public void mouseDragged(MouseEvent arg0) {
		if (arg0.getModifiers() == MouseEvent.BUTTON1_MASK) {
			notebook.drawLine(lastPosition.x, lastPosition.y,
			        arg0.getX(), arg0.getY());
			lastPosition = arg0.getPoint();
		}
	}


	/**
	 * Ignored.
	 */
	@Override
	public void mouseEntered(MouseEvent e) { }


	/**
	 * Ignored.
	 */
	@Override
	public void mouseExited(MouseEvent e) { }


	/**
	 * Sets last mouse position so that the next line is not drawn across the
	 * screen.
	 */
	public void mouseMoved(MouseEvent arg0) {
		lastPosition = arg0.getPoint();
	}


	/**
	 * Ignored.
	 */
	@Override
	public void mousePressed(MouseEvent e) { }


	/**
	 * Ignored.
	 */
	@Override
	public void mouseReleased(MouseEvent e) { }
}

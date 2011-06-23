// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


/**
 * Listens to mouse movements on the displaying panel and updates the notebook.
 */
public class PaintListener implements MouseMotionListener, MouseListener {

	private NoteBook notebook;

	public PaintListener(NoteBook notebook) {
		this.notebook = notebook;
	}

	private int x = -1;

	private int y = -1;

	public void mouseDragged(MouseEvent arg0) {
		notebook.drawLine(x, y, arg0.getX(), arg0.getY());
		x = arg0.getX();
		y = arg0.getY();
	}

	public void mouseMoved(MouseEvent arg0) {
		x = arg0.getX();
		y = arg0.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		notebook.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}

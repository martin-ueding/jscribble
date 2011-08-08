package jscribble;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import jscribble.notebook.NoteBook;

public class ScrollListener implements MouseListener {

	private DrawPanel drawPanel;
	private NoteBook notebook;

	public ScrollListener(DrawPanel drawPanel, NoteBook notebook) {
		this.drawPanel = drawPanel;
		this.notebook = notebook;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		
		if (x <= Integer.parseInt(NoteBookProgram.getConfigValue("scroll_panel_width"))) {
			notebook.goBackwards();
		}
		if (x >= drawPanel.getWidth() - Integer.parseInt(NoteBookProgram.getConfigValue("scroll_panel_width"))) {
			notebook.goForward();
		}


	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}

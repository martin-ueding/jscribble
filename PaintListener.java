import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class PaintListener implements MouseMotionListener {
	
	private int x = -1, y = -1;
	private NoteBook notebook;
	
	public PaintListener(NoteBook notebook) {
		this.notebook = notebook;
	}
	
	public void mouseDragged(MouseEvent arg0) {
		notebook.drawLine(x, y, arg0.getX(), arg0.getY());
		x = arg0.getX();
		y = arg0.getY();
	}

	public void mouseMoved(MouseEvent arg0) {
		x = arg0.getX();
		y = arg0.getY();
	}


}

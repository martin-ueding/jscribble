// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

/**
 * Displays a NoteBook.
 */
@SuppressWarnings("serial")
public class MalPanel extends JPanel {
	private NoteBook notebook;

	/**
	 * Creates a new display panel that will listen to changes from a specific notebook.
	 *
	 * @param notebook
	 */
	public MalPanel(NoteBook notebook) {
		this.notebook = notebook;
		notebook.setDoneDrawing(new Redrawer(this));

		PaintListener pl = new PaintListener(notebook);
		addMouseMotionListener(pl);
		addMouseListener(pl);

		r = Runtime.getRuntime();
	}

	private ImageObserver io = this;
	private boolean lines = false;

	private static final Color lineColor = new Color(200, 200, 200);
	private static final int lineSpacing = 40;

	private Runtime r;

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		g2.drawImage(notebook.getCurrentSheet().getImg(), 0, 0, io);

		if (lines) {
			g2.setColor(lineColor);
			for (int i = lineSpacing; i < getWidth(); i += lineSpacing) {
				g2.drawLine(i, 0, i, getHeight());
			}


			for (int i = lineSpacing; i < getHeight(); i += lineSpacing) {
				g2.drawLine(0, i, getWidth(), i);
			}
		}

		g2.setColor(Color.BLUE);
		g2.drawString("Page " + notebook.getCurrentSheet().getPagenumber() + "/" + notebook.getSheetCount(), 20, 20);

		g2.drawString(String.format("%d MB used, %d MB free, %d MB total", (r.totalMemory() - r.freeMemory()) / 1024 / 1024, r.freeMemory() / 1024 / 1024, r.totalMemory() / 1024 / 1024), getWidth() / 2, 20);

	}

	public void setLines(boolean b) {
		this.lines  = b;
	}
}

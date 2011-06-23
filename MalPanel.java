// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

/**
 * Displays the current page of a notebook. It also listens to the mouse and
 * relays the movements to the notebook as line drawing commands. It also
 * features a command listener for more user interaction.
 */
@SuppressWarnings("serial")
public class MalPanel extends JPanel {
	private NoteBook notebook;

	/**
	 * Creates a new display panel that will listen to changes from a specific
	 * notebook.
	 *
	 * @param notebook the notebook to display
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
		g2.drawString(String.format("Page %d/%d",
		                            notebook.getCurrentSheet().getPagenumber(),
		                            notebook.getSheetCount()), 15, 15);

		g2.drawString(String.format("%d MB used, %d MB free, %d MB total",
		                            (r.totalMemory() - r.freeMemory()) / 1024 / 1024,
		                            r.freeMemory() / 1024 / 1024, r.totalMemory() / 1024 / 1024),
		              getWidth() / 2, 15);

	}

	/**
	 * Set whether help lines are to be drawn.
	 * @param b status of the lines
	 */
	public void setLines(boolean b) {
		this.lines  = b;
	}
}

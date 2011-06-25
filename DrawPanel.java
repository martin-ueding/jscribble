// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

/**
 * Displays the current page of a NoteBook. It also listens to the mouse and
 * relays the movements to the NoteBook as line drawing commands. It also
 * features a command listener for more user interaction.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	/**
	 * Color of the help lines.
	 */
	private static final Color lineColor = new Color(200, 200, 200);

	/**
	 * The spacing between the help lines.
	 */
	private static final int lineSpacing = 40;

	/**
	 * The NoteBook that is displayed.
	 */
	private NoteBook notebook;

	private ImageObserver io = this;

	/**
	 * Whether helping lines are enabled.
	 */
	private boolean lines = false;

	/**
	 * Creates a new display panel that will listen to changes from a specific
	 * NoteBook.
	 *
	 * @param notebook the NoteBook to display
	 */
	public DrawPanel(NoteBook notebook) {
		this.notebook = notebook;
		notebook.setDoneDrawing(new Redrawer(this));

		PaintListener pl = new PaintListener(notebook);
		addMouseMotionListener(pl);
		addMouseListener(pl);
	}

	/**
	 * Set whether help lines are to be drawn.
	 *
	 * @param b status of the lines
	 */
	public void setLines(boolean b) {
		this.lines  = b;
	}


	/**
	 * Draws the NoteSheet and page number. If lines are on, they are drawn on
	 * top of the image as well.
	 *
	 * @param g graphics context (usually given by Java itself).
	 */
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

		g2.drawImage(notebook.getCurrentSheet().getImg(), 0, 0, io);

		if (lines) {
			// TODO draw the lines below the drawing
			g2.setColor(lineColor);
			for (int i = lineSpacing; i < getWidth(); i += lineSpacing) {
				g2.drawLine(i, 0, i, getHeight());
			}


			for (int i = lineSpacing; i < getHeight(); i += lineSpacing) {
				g2.drawLine(0, i, getWidth(), i);
			}
		}

		g2.setColor(Color.BLUE);
		g2.drawString(String.format("Page %d/%d", notebook.getCurrentSheet().getPagenumber(), notebook.getSheetCount()), getWidth() / 2, 15);
	}
}

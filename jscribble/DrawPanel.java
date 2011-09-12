// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

/*
 * This file is part of jscribble.
 *
 * jscribble is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 2 of the License, or (at your option)
 * any later version.
 *
 * jscribble is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * jscribble.  If not, see <http://www.gnu.org/licenses/>.
 */

package jscribble;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

import jscribble.notebook.NoteBook;
import jscribble.notebook.NoteSheet;

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
	 * A container for a key and help text.
	 *
	 * @author Martin Ueding <dev@martin-ueding.de>
	 */
	class HelpItem {
		/**
		 * The buttons(s) that cause some action.
		 */
		public String key;


		/**
		 * The action the buttons cause.
		 */
		public String helptext;


		/**
		 * Generates a new HelpItem.
		 */
		public HelpItem(String key, String helptext) {
			this.key = key;
			this.helptext = helptext;
		}
	}


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


	/**
	 * Handles the image output.
	 */
	private ImageObserver io = this;


	/**
	 * Whether helping lines are enabled.
	 */
	private boolean lines = false;


	/**
	 * Whether to display the help panel.
	 */
	private boolean showHelp = false;


	/**
	 * A list with all the HelpItem to display.
	 */
	private HelpItem[] helpItems = {
		new HelpItem("h", Localizer.get("show help")),
		new HelpItem("j, <Space>, <Enter>, <DownArrow>, <RightArrow>",
		Localizer.get("go forward")),
		new HelpItem("k, <Backspace>, <UpArrow>, <LeftArrow>",
		Localizer.get("go backward")),
		new HelpItem("f, <Pos1>", Localizer.get("goto first")),
		new HelpItem("l, <End", Localizer.get("goto last")),
		new HelpItem("<Alt-F4> / <CMD-Q>", Localizer.get("save & exit"))

	};


	private boolean onionMode;


	public void toggleOnion() {
		if (onionMode) {
			resetCachedImage();
		}
		onionMode = !onionMode;
		repaint();
	}


	/**
	 * Creates a new display panel that will listen to changes from a specific
	 * NoteBook.
	 *
	 * @param notebook the NoteBook to display
	 */
	public DrawPanel(NoteBook notebook) {
		this.notebook = notebook;
		notebook.setDoneDrawing(new Redrawer(this));

		PaintListener pl = new PaintListener(this);
		addMouseMotionListener(pl);
		addMouseListener(pl);
	}


	/**
	 * Draws the help screen if needed.
	 *
	 * @param g2 Graphics2D to draw in
	 */
	private void drawHelp(Graphics2D g2) {
		if (showHelp) {
			g2.setColor(new Color(0, 0, 0, 200));
			g2.fillRoundRect(50, 50, getWidth() - 100, getHeight() - 100, 20,
			        20);
			g2.setColor(Color.WHITE);

			int i = 0;
			int vspacing = 30;
			int spacing = 150;
			int padding = 70;
			for (HelpItem h : helpItems) {
				g2.drawString(h.helptext, padding, i * vspacing + padding);
				g2.drawString(h.key, spacing + padding, i * vspacing +
				        padding);
				i++;
			}

			g2.setColor(Color.GRAY);
			g2.drawString(String.format(Localizer.get("Version %s"),
			        VersionName.version), padding, getHeight() - padding);
		}
	}


	/**
	 * Draws the helping lines if needed.
	 *
	 * @param g2 Graphics2D to draw on
	 */
	private void drawLines(Graphics2D g2) {
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
	}


	/**
	 * Draws the page number on top.
	 *
	 * @param g2 Graphics2D to draw on
	 */
	private void drawPageNumber(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		g2.drawString(String.format(Localizer.get("Page %d/%d"),
		        notebook.getCurrentSheet().getPagenumber(),
		        notebook.getSheetCount()), getWidth() / 2, 15);
	}


	/**
	 * Draws the NoteSheet and page number. If lines are on, they are drawn on
	 * top of the image as well.
	 *
	 * @param g graphics context (usually given by Java itself).
	 */
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHints(new
		        RenderingHints(RenderingHints.KEY_ANTIALIASING,
		                RenderingHints.VALUE_ANTIALIAS_ON));


		// Draw the current image.
		g2.drawImage(getCachedImage(), 0, 0, io);

		g2.setComposite(AlphaComposite.Src);

		drawLines(g2);
		drawPageNumber(g2);
		drawScrollPanels(g2);
		drawHelp(g2);
	}

	/**
	 * A cached image that is used instead of the original images in the onion
	 * mode to conserve performance.
	 */
	private BufferedImage cachedImage;

	private BufferedImage getCachedImage() {
		if (onionMode) {
			if (cachedImage == null) {
				cachedImage = new BufferedImage(getWidth(), getHeight(),
				        BufferedImage.TYPE_BYTE_GRAY);
				Graphics2D g2 = (Graphics2D) cachedImage.getGraphics();

				// Draw the previous sheet in the NoteBook.
				NoteSheet prevSheet = notebook.getPreviousSheet();

				// If we are not on the first NoteSheet ...
				if (prevSheet != null) {
					BufferedImage prev = prevSheet.getImg();
					g2.setComposite(AlphaComposite.Src);
					g2.drawImage(prev, 0, 0, io);
					g2.setComposite(AlphaComposite.getInstance(
					            AlphaComposite.SRC_ATOP, (float) 0.8));
				}
				g2.drawImage(notebook.getCurrentSheet().getImg(), 0, 0, io);
			}
			return cachedImage;
		}
		else {
			return notebook.getCurrentSheet().getImg();
		}
	}


	private void drawScrollPanels(Graphics2D g) {
		if (NoteBookProgram.getConfigValue("show_scroll_panels").equalsIgnoreCase("true")) {
			try {
				int scrollPanelRadius = Integer.parseInt(NoteBookProgram.getConfigValue("scroll_panel_width"));
				int scrollPanelPadding = Integer.parseInt(NoteBookProgram.getConfigValue("scroll_panel_padding"));
				g.setColor(new Color(0, 0, 0, 100));
				g.fillRoundRect(-scrollPanelRadius, scrollPanelPadding,
				        2 * scrollPanelRadius, getHeight() - 2 * scrollPanelPadding,
				        scrollPanelRadius, scrollPanelRadius);
				g.fillRoundRect(getWidth() - scrollPanelRadius,
				        scrollPanelPadding, 2 * scrollPanelRadius,
				        getHeight() - 2 * scrollPanelPadding,
				        scrollPanelRadius, scrollPanelRadius);
			}
			catch (NumberFormatException e) {
				NoteBookProgram.handleError(Localizer.get("Malformed entry in config file."));
			}
		}

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
	 * Sets whether the help dialog is displayed.
	 */
	public void setShowHelp(boolean showHelp) {
		this.showHelp = showHelp;
	}


	/**
	 * Whether to display the help panel.
	 */
	public void toggleHelp() {
		showHelp = !showHelp;
	}


	public void drawLine(int x, int y, int x2, int y2) {
		if (onionMode) {
			Graphics2D g2 = (Graphics2D) getCachedImage().getGraphics();
			g2.setRenderingHints(new
			        RenderingHints(RenderingHints.KEY_ANTIALIASING,
			                RenderingHints.VALUE_ANTIALIAS_ON));
			g2.setColor(Color.BLACK);
			g2.drawLine(x, y, x2, y2);
		}
		notebook.drawLine(x, y, x2, y2);
	}


	public void goBackwards() {
		resetCachedImage();
		notebook.goBackwards();
	}


	private void resetCachedImage() {
		cachedImage = null;
	}


	public void goForward() {
		resetCachedImage();
		notebook.goForward();
	}


	public void gotoLast() {
		resetCachedImage();
		notebook.gotoLast();
	}


	public void gotoFirst() {
		resetCachedImage();
		notebook.gotoFirst();
	}
}

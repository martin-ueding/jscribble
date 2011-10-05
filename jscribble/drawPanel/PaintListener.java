// Copyright © 2011 Martin Ueding <dev@martin-ueding.de>

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

package jscribble.drawPanel;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

import jscribble.helpers.Config;

/**
 * Listens to mouse movements on the displaying panel and updates the NoteBook.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class PaintListener implements MouseMotionListener, MouseListener {
	/**
	 * The last position of the pointer. Since the line commands needs two
	 * points, but the mouseDragged() only gives the current location, the
	 * class needs to remember where the pointer was before.
	 */
	private Point lastPosition = new Point(-1, -1);

	/**
	 * The panel to notify when something is drawn.
	 */
	private DrawPanel drawPanel;

	/**
	 * Generates a new Listener that relays its commands to a given DrawPanel.
	 *
	 * @param drawPanel Relay to this.
	 */
	public PaintListener(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		drawPanel.addMouseListener(this);
		drawPanel.addMouseMotionListener(this);
	}

	/**
	 * Draws a single dot when the mouse is clicked. Handles the scroll panels
	 * in the mouse only mode. Erases on single right click.
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		int x = event.getX();

		if (Config.getBoolean("scroll_panels_show")) {
			if (x <= Config.getInteger("scroll_panel_width")) {
				drawPanel.goBackwards();
				return;
			}
			if (x >= drawPanel.getWidth() -
			        Config.getInteger("scroll_panel_width")) {
				drawPanel.goForward();
				return;
			}
		}

		if (Config.isButtonForCommand(event, "notebook_draw_mouse_button")) {
			Line2D line = new Line2D.Float(event.getPoint(), event.getPoint());
			drawPanel.drawLine(line);
		}
		else if (Config.getBoolean("notebook_erase_enable")
		         && Config.isButtonForCommand(event, "notebook_erase_mouse_button")) {
			Line2D line = new Line2D.Float(event.getPoint(), event.getPoint());
			drawPanel.eraseLine(line);
		}
	}

	/**
	 * Tells the NoteBook to draw a line on it.
	 */
	public void mouseDragged(MouseEvent event) {
		if (Config.isButtonForCommand(event, "notebook_draw_mouse_button")) {
			Line2D line = new Line2D.Float(lastPosition, event.getPoint());
			drawPanel.drawLine(line);
		}
		else if (Config.getBoolean("notebook_erase_enable")
		         && Config.isButtonForCommand(event, "notebook_erase_mouse_button")) {
			Line2D line = new Line2D.Float(lastPosition, event.getPoint());
			drawPanel.eraseLine(line);
		}

		lastPosition = event.getPoint();
	}

	/**
	 * Ignored.
	 */
	@Override
	public void mouseEntered(MouseEvent ignored) { }

	/**
	 * Ignored.
	 */
	@Override
	public void mouseExited(MouseEvent ignored) { }

	/**
	 * Sets last mouse position so that the next line is not drawn across the
	 * screen.
	 */
	public void mouseMoved(MouseEvent event) {
		lastPosition = event.getPoint();
	}

	/**
	 * Ignored.
	 */
	@Override
	public void mousePressed(MouseEvent ignored) { }

	/**
	 * Ignored.
	 */
	@Override
	public void mouseReleased(MouseEvent ignored) { }
}

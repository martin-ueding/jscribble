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

package jscribble.notebook;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import jscribble.helpers.SettingsWrapper;

/**
 * Wraps drawing on an image.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class BufferedImageWrapper {
	/**
	 * Image to draw onto.
	 */
	private BufferedImage img;

	/**
	 * Graphics context for the image.
	 */
	private Graphics2D graphics;

	/**
	 * Stroke for drawing.
	 */
	private Stroke drawStroke = new BasicStroke(
	    SettingsWrapper.getInteger("stroke_width_draw"));

	/**
	 * Stroke for erasing.
	 */
	private Stroke eraseStroke = new BasicStroke(
	    SettingsWrapper.getInteger("stroke_width_erase"));

	/**
	 * Background color.
	 */
	private Color background = SettingsWrapper.getColor("note_sheet_background_color");

	/**
	 * Foreground color.
	 */
	private Color foreground = SettingsWrapper.getColor("note_sheet_foreground_color");

	/**
	 * Creates a new wrapper around the given image.
	 *
	 * @param image Image to wrap.
	 */
	public BufferedImageWrapper(BufferedImage image) {
		img = image;

		graphics = (Graphics2D) img.getGraphics();
		graphics.setRenderingHints(new
		        RenderingHints(RenderingHints.KEY_ANTIALIASING,
		                RenderingHints.VALUE_ANTIALIAS_ON));
	}

	/**
	 * Draws a line on the picture.
	 *
	 * @param line Line to draw.
	 */
	public void drawLine(Line2D line) {
		graphics.setColor(foreground);
		graphics.setStroke(drawStroke);
		graphics.drawLine((int) line.getX1(),
		        (int) line.getY1(),
		        (int) line.getX2(),
		        (int) line.getY2());
	}

	/**
	 * Erases a line from the picture.
	 *
	 * @param line Line to erase.
	 */
	public void eraseLine(Line2D line) {
		graphics.setColor(background);
		graphics.setStroke(eraseStroke);
		graphics.drawLine((int) line.getX1(),
		        (int) line.getY1(),
		        (int) line.getX2(),
		        (int) line.getY2());
	}

	/**
	 * Getter for the wrapped image.
	 *
	 * @return Image.
	 */
	public BufferedImage getImg() {
		return img;
	}
}

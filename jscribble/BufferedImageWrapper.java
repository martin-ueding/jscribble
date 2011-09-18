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

package jscribble;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

public class BufferedImageWrapper {
	private BufferedImage img;

	private Graphics2D graphics;

	private Stroke drawStroke = new BasicStroke(
	    Integer.parseInt(NoteBookProgram.getConfig()
	            .getProperty("draw_stroke_width", "1")));

	private Stroke eraseStroke = new BasicStroke(
	    Integer.parseInt(NoteBookProgram.getConfig()
	            .getProperty("erase_stroke_width", "5")));

	private Color background = Color.WHITE;
	private Color foreground = Color.BLACK;

	public BufferedImageWrapper(BufferedImage image) {
		img = image;

		graphics = (Graphics2D) img.getGraphics();
		graphics.setRenderingHints(new
		        RenderingHints(RenderingHints.KEY_ANTIALIASING,
		                RenderingHints.VALUE_ANTIALIAS_ON));
	}

	public void drawLine(int x, int y, int x2, int y2) {
		graphics.setColor(foreground);
		graphics.setStroke(drawStroke);
		graphics.drawLine(x, y, x2, y2);
	}

	public void eraseLine(int x, int y, int x2, int y2) {
		graphics.setColor(background);
		graphics.setStroke(eraseStroke);
		graphics.drawLine(x, y, x2, y2);
	}

	public BufferedImage getImg() {
		return img;
	}
}

// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

/*
 * This file is part of jscribble.
 *
 * Foobar is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
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

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * A set of an image and a filename that is supposed to be loaded or saved to
 * disk.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class ImageSwapTask {
	/**
	 * The image that is to be saved.
	 */
	private BufferedImage img;


	/**
	 * The file the image is to be saved to.
	 */
	private File outfile;


	/**
	 * A task for the WriteoutTread.
	 *
	 * @param img image to save
	 * @param outfile file to save the image to
	 */
	public ImageSwapTask(BufferedImage img, File outfile) {
		this.img = img;
		this.outfile = outfile;
	}


	/**
	 * @return the image
	 */
	public BufferedImage getImg() {
		return img;
	}


	/**
	 * @return the file to write to
	 */
	public File getOutfile() {
		return outfile;
	}
}

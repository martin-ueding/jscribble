// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * A set of an image and a filename that is supposed to be loaded or saved to
 * disk.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class ImageSwapTask {
	private BufferedImage img;
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


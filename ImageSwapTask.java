// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * A set of an image and a filename that is supposed to be loaded or saved to
 * disk.
 */
public class ImageSwapTask {
	private BufferedImage img;
	private File outfile;

	public ImageSwapTask(BufferedImage img, File outfile) {
		this.img = img;
		this.outfile = outfile;
	}

	public BufferedImage getImg() {
		return img;
	}

	public File getOutfile() {
		return outfile;
	}



}

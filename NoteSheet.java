// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Represents a single note sheet.
 */
public class NoteSheet {
	/**
	 * Page number for the current page.
	 */
	private int pagenumber;

	/**
	 * The picture for storing the drawing on.
	 */
	private BufferedImage img;

	private Graphics2D graphics;

	private int width;

	private int height;

	/**
	 * Whether the picture is swapped to disk. To save RAM, the pictures might
	 * be swapped into a temp file if the user is in another part of the
	 * notebook.
	 */
	private boolean isSwapped() {
		return img == null;
	}

	private boolean touched = false;

	private File filename;
	
	private static WriteoutThread writethread;

	/**
	 * Creates an empty note sheet.
	 *
	 * @param width width of the sheet in pixels
	 * @param height height of the sheet in pixels
	 */
	public NoteSheet(int width, int height, int pagenumber, File infile) {
		if (writethread == null) {
			writethread = new WriteoutThread();
		}
		
		this.width = width;
		this.height = height;
		this.pagenumber = pagenumber;
		this.filename = infile;

		if (filename != null && filename.exists()) {
			loadFromFile();
		}
		else {
			if (filename == null) {
				System.out.println("generating tempfile");
				try {
					filename = File.createTempFile("jscribble-", ".png");
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

			graphics = getGraphics();
			graphics.setColor(new Color(255, 255, 255));
			graphics.fillRect(0, 0, width, height);
		}
	}

	public int getPagenumber() {
		return pagenumber;
	}


	public BufferedImage getImg() {
		if (isSwapped()) {
			loadFromFile();
		}
		return img;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}


	/**
	 * Loads the image from a given file. Everything else is left intact.
	 *
	 * @param infile filename to load from
	 */
	public void loadFromFile() {
		try {
			System.out.println("reading " + filename.getCanonicalPath());
			img = javax.imageio.ImageIO.read(filename);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Saves the picture to a PNG file.
	 *
	 * @param outfile filename to use, needs to be png
	 */
	public void saveToFile() {
		if (touched) {
			writethread.schedule(new ImageSwapTask(img, filename));
		}

		// remove the image from the memory
		img = null;

		touched = false;
	}

	/**
	 * Draws a line onto the sheet.
	 */
	public void drawLine(int x, int y, int x2, int y2) {
		touched = true;

		Graphics2D graphics = getGraphics();

		graphics.setColor(new Color(0, 0, 0));
		graphics.drawLine(x, y, x2, y2);
	}

	private Graphics2D getGraphics() {
		if (graphics == null) {
			graphics = (Graphics2D)(getImg().getGraphics());
			graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		}

		return graphics;
	}

	/**
	 * @return whether this sheet has any lines drawn onto it
	 */
	public boolean touched() {
		return touched;
	}
}


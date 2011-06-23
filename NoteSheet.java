// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	 * Count of pages. Latest page number is pagecount-1.
	 */
	private static int pagecount;

	/**
	 * The picture for storing the drawing on.
	 */
	private BufferedImage img;

	private Graphics2D graphics;

	private int width;

	private int height;

	/**
	 * Whether the picture is swapped to disk. To save RAM, the pictures might
	 * be swapped into a tempfile if the user is in another part of the
	 * notebook.
	 */
	private boolean isSwapped = false;

	private String swapname = null;

	private boolean touched = false;

	/**
	 * Creates an empty note sheet.
	 *
	 * @param width width of the sheet in pixels
	 * @param height height of the sheet in pixels
	 */
	public NoteSheet(int width, int height) {
		pagenumber = pagecount++;

		this.width = width;
		this.height = height;

		img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		
		graphics = getGraphics();
		graphics.setColor(new Color(255, 255, 255));
		graphics.fillRect(0, 0, width, height);
	}
	
	public NoteSheet(int pagenumber, File infile) {
		this.pagenumber = pagenumber;
	}

	public int getPagenumber() {
		return pagenumber;
	}

	/**
	 * @return The number of pages given out so far.
	 */
	public static int getPagecount() {
		return pagecount;
	}


	public BufferedImage getImg() {
		if (isSwapped) {
			loadFromSwap();
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
	 * Save the image to a temp file. This can be used to free up RAM but keep
	 * the image.
	 */
	public void saveToSwap() {
		if (!isSwapped) {
			if (swapname == null) {
				swapname = String.format("/tmp/jscribble-%d-%d.png", (int)(Math.random() * 10000), pagenumber);
			}

			saveToFile(swapname);

			// delete the image from the heap
			img = null;


			isSwapped = true;
		}
	}

	/**
	 * Loads the image from the swap file. This is done automatically when the
	 * image is requested with the getter.
	 */
	private void loadFromSwap() {
		if (isSwapped) {
			if (swapname != null) {
				loadFromFile(swapname);
			}
		}
	}

	/**
	 * Loads the image from a given file. Everything else is left intact.
	 *
	 * @param infile filename to load from
	 */
	public void loadFromFile(String infile) {
		// TODO implement loading

		isSwapped = false;
	}

	/**
	 * Saves the picture to a PNG file.
	 *
	 * @param outfile filename to use, needs to be png
	 */
	public void saveToFile(String outfile) {
		if (touched) {
			try {
				javax.imageio.ImageIO.write(getImg(), "png", new FileOutputStream(outfile));
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
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
			graphics = (Graphics2D)(img.getGraphics());
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


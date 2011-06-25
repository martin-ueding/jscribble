// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Represents a single note sheet.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class NoteSheet {
	/**
	 * whether to write debug messages while loading and writing NoteSheet.
	 */
	private static final boolean showLoadWriteMessages = true;


	/**
	 * Page number for the current page.
	 */
	private int pagenumber;


	/**
	 * The picture for storing the drawing on.
	 */
	private BufferedImage img;

	private Graphics2D graphics;


	/**
	 * The size of this sheet in pixels.
	 */
	private Dimension noteSize;


	/**
	 * Whether a picture has any lines drawn on it.
	 */
	private boolean touched = false;


	/**
	 * Whether the picture was changed after the last save. A newly created
	 * picture is not unsaved since there is nothing worth saving.
	 */
	private boolean unsaved = false;


	/**
	 * Storage point of the image of this sheet.
	 */
	private File filename;


	/**
	 * A singleton thread that handles all the writing to disk.
	 */
	private static WriteoutThread writethread;


	/**
	 * Creates an empty note sheet.
	 *
	 * @param noteSize size of the sheet
	 * @param pagenumber given page number
	 * @param infile File to save image in
	 */
	public NoteSheet(Dimension noteSize, int pagenumber, File infile) {
		if (writethread == null) {
			writethread = new WriteoutThread();
		}

		this.noteSize = noteSize;
		this.pagenumber = pagenumber;
		this.filename = infile;

		if (filename == null || !filename.exists()) {
			if (filename == null) {
				try {
					filename = File.createTempFile("jscribble-", ".png");
				}
				catch (IOException e) {
					NoteBookProgram.handleError("Could not create a temp file.");
					e.printStackTrace();
				}
			}

			img = new BufferedImage(noteSize.width, noteSize.height, BufferedImage.TYPE_BYTE_GRAY);

			graphics = getGraphics();
			graphics.setColor(new Color(255, 255, 255));
			graphics.fillRect(0, 0, noteSize.width, noteSize.height);

			unsaved = true;
		}
	}


	/**
	 * Get the sheet's page number.
	 *
	 * @return page number
	 */
	public int getPagenumber() {
		return pagenumber;
	}


	/**
	 * Gets the image, loads it from disk id needed.
	 *
	 * @return the image
	 */
	public BufferedImage getImg() {
		if (isSwapped()) {
			loadFromFile();
		}
		return img;
	}


	/**
	 * Width of the image.
	 *
	 * @return width
	 */
	public int getWidth() {
		return noteSize.width;
	}


	/**
	 * Height of the image.
	 *
	 * @return height
	 */
	public int getHeight() {
		return noteSize.height;
	}


	/**
	 * Loads the image from file. Everything else is left intact.
	 */
	public void loadFromFile() {
		try {
			if (showLoadWriteMessages) {
				System.out.println(String.format("loading %s", filename.getAbsolutePath()));
			}
			img = ImageIO.read(filename);
		}
		catch (FileNotFoundException e) {
			NoteBookProgram.handleError("Could not find the note sheet image.");
			e.printStackTrace();
		}
		catch (IOException e) {
			NoteBookProgram.handleError("Could not read the note sheet image.");
			e.printStackTrace();
		}

		touched = true;
	}


	/**
	 * Saves the picture to a PNG file. The image is then removed from the heap.
	 */
	public void saveToFile() {
		if (touched && unsaved) {
			if (showLoadWriteMessages) {
				System.out.println(String.format("writing %s", filename.getAbsolutePath()));
			}
			writethread.schedule(new ImageSwapTask(img, filename));
		}

		// remove the image from the memory
		img = null;

		touched = false;
		unsaved = false;
	}


	/**
	 * Draws a line onto the sheet. The sheet is then marked as "touched".
	 */
	public void drawLine(int x, int y, int x2, int y2) {
		touched = true;
		unsaved = true;

		Graphics2D graphics = getGraphics();

		graphics.setColor(new Color(0, 0, 0));
		graphics.drawLine(x, y, x2, y2);
	}


	/**
	 * @return whether this sheet has any lines drawn onto it
	 */
	public boolean touched() {
		return touched;
	}


	/**
	 * @return whether this sheet has unsaved changes
	 */
	public boolean unsaved() {
		return unsaved;
	}


	/**
	 * Stops the WriteoutThread and waits for it. This ensures that everything
	 * is written to disk properly.
	 */
	public void stopWriteoutThread() {
		try {
			writethread.stopAfterLast();
			writethread.join();
		}
		catch (InterruptedException e) {
			NoteBookProgram.handleError("WriteThread was interrupted.");
			e.printStackTrace();
		}
	}


	/**
	 * Gets the graphics context of the image.
	 *
	 * @return graphics object
	 */
	private Graphics2D getGraphics() {
		if (graphics == null) {
			graphics = (Graphics2D)(getImg().getGraphics());
			graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		}

		return graphics;
	}


	/**
	 * Whether the picture is swapped to disk. To save RAM, the pictures might
	 * be swapped into a temporary file if the user is in another part of the
	 * NoteBook.
	 */
	private boolean isSwapped() {
		return img == null;
	}
}


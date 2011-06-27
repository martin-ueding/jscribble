// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package jscribble.notebook;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import jscribble.NoteBookProgram;

/**
 * Represents a single note sheet.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
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


	/**
	 * Graphics context of the image.
	 */
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

		this.noteSize = noteSize;
		this.pagenumber = pagenumber;
		this.filename = infile;

		// If no filename or a filename that does not exist yet is given or the file is empty (like a newly created temporary file), a new
		// image needs to be created.
		if (filename == null || !filename.exists() || filename.length() == 0) {
			initNewImage();
		}

		// If this branch is reached, then a image file does exist and it is not empty. This sheet has something drawn on it then.
		else {
			touched = true;
		}
	}


	/**
	 * Draws a line onto the sheet. The sheet is then marked as "touched".
	 */
	public void drawLine(int x, int y, int x2, int y2) {
		touched = true;
		unsaved = true;

		getGraphics().setColor(new Color(0, 0, 0));
		getGraphics().drawLine(x, y, x2, y2);
	}


	public File getFilename() {
		return filename;
	}


	/**
	 * Gets the graphics context of the image.
	 */
	private Graphics2D getGraphics() {
		if (graphics == null) {
			BufferedImage im = getImg();
			if (im == null) {
				NoteBookProgram.log(getClass().getName(), "BufferedImage is null.");
			}
			graphics = (Graphics2D)(im.getGraphics());
			graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		}

		return graphics;
	}


	/**
	 * Gets the image, loads it from disk id needed.
	 */
	public BufferedImage getImg() {
		if (isSwapped()) {
			loadFromFile();
		}
		if (img == null) {
			throw new NullPointerException("Could not load image from disk.");
		}
		return img;
	}


	/**
	 * Get the sheet's page number.
	 */
	public int getPagenumber() {
		return pagenumber;
	}


	private void initNewImage() {
		if (filename == null) {
			try {
				filename = File.createTempFile(NoteBookProgram.getProgramname() + "-", ".png");
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

		unsaved = false;
	}


	/**
	 * Whether the picture is swapped to disk. To save RAM, the pictures might
	 * be swapped into a temporary file if the user is in another part of the
	 * NoteBook.
	 */
	private boolean isSwapped() {
		return img == null;
	}


	/**
	 * Loads the image from file. Everything else is left intact.
	 */
	public void loadFromFile() {
		if (!filename.exists() || filename.length() == 0L) {
			NoteBookProgram.log(getClass().getName(), "Image file does not exist.");

			// if the file is empty, then the WriteoutThread did not get a change to write the file yet. Join with the thread.
			stopWriteoutThread();
		}


		if (!filename.exists() || filename.length() == 0L) {
			NoteBookProgram.log(getClass().getName(), "Image file does not exist after stopping WriteoutThread.");
		}


		try {
			NoteBookProgram.log(getClass().getName(), String.format("loading %s", filename.getAbsolutePath()));

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

		if (img == null) {
			throw new NullPointerException("Could not load image from disk.");
		}

		touched = true;
	}


	/**
	 * Saves the picture to a PNG file. The image is then removed from the heap.
	 */
	public void saveToFile() {
		NoteBookProgram.log(getClass().getName(), "Picture " + pagenumber + " is " + (touched ? "touched" : "untouched") + " and " + (unsaved ? "unsaved" : "saved") + ".");
		if (touched && unsaved) {
			NoteBookProgram.log(getClass().getName(), String.format("Scheduling %s for writing.", filename.getAbsolutePath()));


			if (writethread == null || !writethread.isAlive()) {
				writethread = new WriteoutThread();
			}
			writethread.schedule(new ImageSwapTask(img, filename));
		}

		// remove the image from the memory
		img = null;
		graphics = null;

		unsaved = false;
	}


	/**
	 * Stops the WriteoutThread and waits for it. This ensures that everything
	 * is written to disk properly.
	 */
	public void stopWriteoutThread() {
		try {
			if (writethread != null) {
				writethread.stopAfterLast();
				writethread.join();
			}
		}
		catch (InterruptedException e) {
			NoteBookProgram.handleError("WriteThread was interrupted.");
			e.printStackTrace();
		}
	}


	/**
	 * Whether this sheet has any lines drawn onto it.
	 */
	public boolean touched() {
		return touched;
	}


	/**
	 * Whether this sheet has unsaved changes.
	 */
	public boolean unsaved() {
		return unsaved;
	}
}

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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import jscribble.NoteBookProgram;
import jscribble.helpers.Localizer;
import jscribble.helpers.Logger;
import jscribble.helpers.SettingsWrapper;
import jscribble.notebook.writeoutThread.ImageSwapTask;
import jscribble.notebook.writeoutThread.WriteoutThread;

/**
 * Represents a single note sheet. A NoteSheet consist a grayscale image. The
 * image is displayed, allowing the user to paint on it. Each image has its
 * individual file name that is used automatically.
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
	private File imagefile;

	/**
	 * Wrapper to handle drawing on the image.
	 */
	private BufferedImageWrapper imageWrapper;

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
		this.imagefile = infile;

		// If no filename or a filename that does not exist yet is given or the
		// file is empty (like a newly created temporary file), a new image
		// needs to be created.
		if (imagefile == null || !imagefile.exists() ||
		        imagefile.length() == 0) {
			initNewImage();
		}

		// If this branch is reached, then a image file does exist and it is
		// not empty. This sheet has something drawn on it then.
		else {
			touched = true;
		}
	}

	/**
	 * Draws a line onto the sheet. The sheet is then marked as "touched".
	 *
	 * @param line Line to draw.
	 */
	public void drawLine(Line2D line) {
		touched = true;
		unsaved = true;

		getImageWrapper().drawLine(line);
	}

	/**
	 * Erases a line from the sheet.
	 *
	 * @param line Line to erase.
	 */
	public void eraseLine(Line2D line) {
		touched = true;
		unsaved = true;

		getImageWrapper().eraseLine(line);
	}

	/**
	 * Getter for the file of the image.
	 */
	public File getFile() {
		return imagefile;
	}

	/**
	 * Gets the graphics context of the image.
	 */
	private Graphics2D getGraphics() {
		if (graphics == null) {
			BufferedImage im = getImg();
			if (im == null) {
				Logger.log(getClass().getName(),
				           Localizer.get("BufferedImage is null."));
			}
			graphics = (Graphics2D)(im.getGraphics());
			graphics.setRenderingHints(new
			        RenderingHints(RenderingHints.KEY_ANTIALIASING,
			                RenderingHints.VALUE_ANTIALIAS_ON));
		}

		return graphics;
	}

	/**
	 * Retrieves and initializes the BufferedImageWrapper.
	 *
	 * @return Initialized BufferedImageWrapper.
	 */
	private BufferedImageWrapper getImageWrapper() {
		if (imageWrapper == null) {
			imageWrapper = new BufferedImageWrapper(getImg());
		}

		return imageWrapper;
	}

	/**
	 * Gets the image, loads it from disk id needed.
	 */
	public BufferedImage getImg() {
		if (isSwapped()) {
			loadFromFile();
		}
		if (img == null) {
			throw new NullPointerException(Localizer.get(
			            "Could not load image from disk."));
		}
		return img;
	}

	/**
	 * Get the sheet's page number.
	 */
	public int getPagenumber() {
		return pagenumber;
	}

	/**
	 * Generates a new image and allocates a temporary file if none was set.
	 */
	private void initNewImage() {
		if (imagefile == null) {
			try {
				// generate a temporary file
				imagefile = File.createTempFile(
				        NoteBookProgram.getProgramname() + "-", ".png");

				imagefile.setReadable(false, false);
				imagefile.setWritable(false, false);

				assert(!imagefile.canRead());
				assert(!imagefile.canWrite());

				imagefile.setReadable(true, true);
				imagefile.setWritable(true, true);


				// Since this is only a temporary file, delete if after using
				// it.
				imagefile.deleteOnExit();
			}
			catch (IOException e) {
				Logger.handleError(Localizer.get(
				            "Could not create a temp file."));
				e.printStackTrace();
			}
		}

		img = new BufferedImage(noteSize.width, noteSize.height,
		        BufferedImage.TYPE_BYTE_GRAY);

		graphics = getGraphics();
		graphics.setColor(SettingsWrapper.getColor("notebook_background_color"));
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
		// If the image was scheduled for writing but not written now, force
		// the thread to write it. If the file is empty, then the
		// WriteoutThread did not get a change to write the file yet. Join with
		// the thread.

		if (!imagefile.exists() || imagefile.length() == 0L) {
			Logger.log(getClass().getName(),
			           Localizer.get("Image file does not exist."));
			if (writethread.isFileInQueue(imagefile)) {
				stopWriteoutThread();
			}
		}

		// If the file still does not exist, it has not been written out.
		if (!imagefile.exists() || imagefile.length() == 0L) {
			Logger.log(getClass().getName(), Localizer.get(
			        "Image file does" +
			        " not exist after stopping WriteoutThread."
			           ));

			initNewImage();
		}

		// If the file exists, load it up.
		else {
			try {
				Logger.log(getClass().getName(),
				           String.format(Localizer.get("Loading %s."),
				                   imagefile.getAbsolutePath()));

				img = ImageIO.read(imagefile);
			}
			catch (FileNotFoundException e) {
				Logger.handleError(Localizer.get(
				            "Could not find the note sheet image."));
				e.printStackTrace();
			}
			catch (IOException e) {
				Logger.handleError(Localizer.get(
				            "Could not read the note sheet image."));
				e.printStackTrace();
			}

			// The image *should* be loaded by now.
			if (img == null) {
				throw new NullPointerException("Could not load im" +
				        "age from disk.");
			}

			// Since it was saved, it has to have some lines on it.
			touched = true;

			// Reset the BufferedImageWrapper that has an invalid image
			resetImageWrapper();
		}
	}

	/**
	 * Resets the BufferedImageWrapper.
	 */
	private void resetImageWrapper() {
		imageWrapper = null;
	}

	/**
	 * Saves the picture to a PNG file. The image is then removed from the
	 * heap.
	 */
	public void saveToFile() {
		Logger.log(getClass().getName(),
		           String.format(Localizer.get("Picture %d is %s and %s."),
		                   pagenumber,
		                   (touched ? Localizer.get("touched") :
		                           Localizer.get("untouched")),
		                   (unsaved ? Localizer.get("unsaved") :
		                           Localizer.get("saved"))
		                        )
		          );
		if (touched && unsaved) {
			Logger.log(getClass().getName(), String.format(
			        Localizer.get("Scheduling %s for writing."),
			        imagefile.getAbsolutePath()));


			if (writethread == null || !writethread.isAlive()) {
				writethread = new WriteoutThread();
			}
			writethread.schedule(new ImageSwapTask(img, imagefile));
		}

		unsaved = false;
	}

	public void freeImage() {
		if (unsaved()) {
			saveToFile();
		}

		// remove the image from the memory
		img = null;
		graphics = null;
		resetImageWrapper();
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
			Logger.handleError(Localizer.get(
			            "WriteThread was interrupted."));
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

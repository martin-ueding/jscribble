// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package jscribble.notebook;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import jscribble.NoteBookProgram;



/**
 * A container for several NoteSheet.
 *
 * The NoteBook contains at least one NoteSheet and adds new sheets whenever
 * the forward() function is called. The whole NoteBook can be saved into
 * individual pictures.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class NoteBook {
	/**
	 * A List with all the NoteSheet this NoteBook contains,
	 */
	private LinkedList<NoteSheet> sheets;


	/**
	 * The currently opened page.
	 */
	private int currentSheet = 0;


	/**
	 * The folder which contains the NoteSheet.
	 */
	private File folder;


	/**
	 * The currently opened page -- actual object.
	 */
	private NoteSheet current;


	/**
	 * Size of the individual NoteSheet.
	 */
	private Dimension noteSize;


	/**
	 * Listener that needs to be notified after the current sheet is changed.
	 */
	private ActionListener doneDrawing;


	/**
	 * Count of pages. Latest page number is pagecount.
	 */
	private int pagecount = 1;


	/**
	 * How many images to cache back and front.
	 */
	private int cacheWidth = 10;


	/**
	 * The name of the NoteBook. This is also used as a prefix for the file
	 * names.
	 */
	private String name;

	private File configFile;

	private NoteBook() {
		sheets = new LinkedList<NoteSheet>();
	}


	/**
	 * Creates an empty note book with a single note sheet.
	 *
	 * @param noteSize size of the NoteSheet within the NoteBook
	 * @param folder place to store images
	 * @param name name of the NoteBook
	 */
	public NoteBook(Dimension noteSize, File folder, String name) {
		this();
		this.noteSize = noteSize;

		this.folder = folder;
		this.name = name;

		// if a NoteBook should be used
		if (folder != null && name != null) {
			loadImagesFromFolder();
		}
	}


	/**
	 * Creates a NoteBook with data read from a configuration file.
	 */
	public NoteBook(File configFile) {
		this();
		this.configFile = configFile;

		Properties p = new Properties();
		try {
			p.loadFromXML(new FileInputStream(configFile));

			noteSize = new Dimension(Integer.parseInt(p.getProperty("width")), Integer.parseInt(p.getProperty("height")));
			folder = new File(p.getProperty("folder"));
			name = p.getProperty("name");
		}
		catch (InvalidPropertiesFormatException e) {
			NoteBookProgram.handleError("The NoteBook config file is malformed.");
			e.printStackTrace();
		}
		catch (FileNotFoundException e) {
			NoteBookProgram.handleError("The NoteBook config file could not be found.");
			e.printStackTrace();
		}
		catch (IOException e) {
			NoteBookProgram.handleError("IO during NoteBook config file loading.");
			e.printStackTrace();
		}

		loadImagesFromFolder();
	}


	/**
	 * Add an empty sheet if the NoteBook would be empty otherwise.
	 */
	private void addPageIfEmpty() {
		if (sheets.size() == 0) {
			sheets.add(new NoteSheet(noteSize, pagecount, generateNextFilename(pagecount)));
			pagecount++;
			currentSheet = 0;
		}
	}


	/**
	 * Asks the user to delete the NoteBook.
	 */
	public void delete() {
		int answer = JOptionPane.showConfirmDialog(null, String.format("Do you really want to delete \"%s\"?", name), "Really delete?", JOptionPane.YES_NO_OPTION);

		if (answer == 0) {
			deleteSure();
		};
	}


	/**
	 * Delete the NoteBook from the file system.
	 */
	public void deleteSure() {
		if (configFile != null) {
			configFile.delete();
			configFile = null;
		}
	}


	/**
	 * Draws a line onto the current sheet.
	 */
	public void drawLine(int x, int y, int x2, int y2) {
		current.drawLine(x, y, x2, y2);

		fireDoneDrawing();
	}


	/**
	 * Tell the listener (the DrawPanel) that the NoteBook has changed and
	 * needs to be redrawn.
	 */
	private void fireDoneDrawing() {
		if (doneDrawing != null) {
			doneDrawing.actionPerformed(null);
		}
	}


	/**
	 * Generates the File for the next NoteSheet.
	 *
	 * @param pagenumber page number to use
	 * @return File object with correct name
	 */
	private File generateNextFilename(int pagenumber) {
		if (folder != null && name != null) {
			try {
				return new File(folder.getCanonicalPath() + File.separator + name + "-" + String.format("%06d", pagenumber) + ".png");
			}
			catch (IOException e) {
				NoteBookProgram.handleError("Could not determine path of NoteBook folder.");
				e.printStackTrace();
			}
		}
		return null;
	}


	public File getConfigFile() {
		return configFile;
	}


	/**
	 * Gets the NoteSheet object which the currently open page of the NoteBook.
	 */
	public NoteSheet getCurrentSheet() {
		return sheets.get(currentSheet);
	}


	/**
	 * Returns the name of the NoteBook.
	 */
	public String getName() {
		return name;
	}


	/**
	 * Number of sheets in the NoteBook
	 */
	public int getSheetCount() {
		if (sheets == null) {
			return 0;
		}

		if (sheets.size() == 1 && !sheets.get(0).touched()) {
			return 0;
		}

		return sheets.size();
	}


	/**
	 * Goes back one sheet.
	 */
	public void goBackwards() {
		if (currentSheet > 0) {
			if (currentSheet + cacheWidth < sheets.size()) {
				sheets.get(currentSheet + cacheWidth).saveToFile();
			}

			currentSheet--;
			updateCurrrentItem();
			fireDoneDrawing();
		}
	}


	/**
	 * Flip the pages forward. It creates a new page if needed. If the current
	 * page is a blank page, no new blank page will be added.
	 */
	public void goForward() {
		if (sheets.size() > currentSheet + 1) {
			currentSheet++;
		}
		else if (current.touched()) {
			sheets.add(new NoteSheet(noteSize, pagecount, generateNextFilename(pagecount)));
			currentSheet++;

			pagecount++;
		}
		else {
			return;
		}


		if (currentSheet >= cacheWidth) {
			sheets.get(currentSheet - cacheWidth).saveToFile();
		}

		updateCurrrentItem();
		fireDoneDrawing();
	}


	/**
	 * Goes to the first page in the NoteBook.
	 */
	public void gotoFirst() {
		currentSheet = 0;
		updateCurrrentItem();
		fireDoneDrawing();
	}


	/**
	 * Goes to the last page in the NoteBook.
	 */
	public void gotoLast() {
		currentSheet = Math.max(0, sheets.size() - 1);
		updateCurrrentItem();
		fireDoneDrawing();
	}


	/**
	 * Loads the images from the previously set folder.
	 */
	private void loadImagesFromFolder() {
		if (!folder.exists()) {
			folder.mkdirs();
		}

		// try to load all images that match the name
		File[] allImages = folder.listFiles(new NoteSheetFileFilter(name));

		if (allImages != null && allImages.length > 0) {
			Arrays.sort(allImages, new FileComparator());


			Pattern p = Pattern.compile("\\D+-(\\d+)\\.png");

			for (File file : allImages) {
				String[] nameparts = file.getName().split(Pattern.quote(File.separator));
				String basename = nameparts[nameparts.length-1];
				Matcher m = p.matcher(basename);
				if (m.matches()) {
					pagecount = Math.max(pagecount, Integer.parseInt(m.group(1)));
					sheets.add(new NoteSheet(noteSize, Integer.parseInt(m.group(1)), file));
				}
			}
			pagecount++;
		}


		updateCurrrentItem();
	}


	/**
	 * Tells the WriteoutThread that this NoteBook has no more sheets to save.
	 */
	private void quitWithWriteoutThread() {
		sheets.getFirst().stopWriteoutThread();

	}


	/**
	 * Persist this NoteBook in the configuration file.
	 *
	 * @param configdir folder where the config file goes
	 */
	public void saveToConfig(File configdir) {
		//
		Properties p = new Properties();
		p.setProperty("width", String.valueOf(noteSize.width));
		p.setProperty("height", String.valueOf(noteSize.height));
		p.setProperty("folder", folder.getAbsolutePath());
		p.setProperty("name", name);

		try {
			configFile = new File(configdir.getAbsolutePath() + File.separator + name + NoteBookProgram.configFileSuffix);
			p.storeToXML(new FileOutputStream(configFile), NoteBookProgram.generatedComment());
		}
		catch (FileNotFoundException e) {
			NoteBookProgram.handleError("Could not find NoteBook config file for writing.");
			e.printStackTrace();
		}
		catch (IOException e) {
			NoteBookProgram.handleError("IO error while writing NoteBook config file.");
			e.printStackTrace();
		}
	}


	/**
	 * Persists the whole NoteBook into individual files.
	 */
	public void saveToFiles() {
		NoteBookProgram.log(getClass().getName(), "Starting to write out image files.");
		for (NoteSheet s : sheets) {
			s.saveToFile();
		}
		quitWithWriteoutThread();
	}


	/**
	 * Sets an action listener to be called when something new was drawn.
	 *
	 * @param doneDrawing ActionListener to be called after drawing a new line.
	 */
	public void setDoneDrawing(ActionListener doneDrawing) {
		this.doneDrawing = doneDrawing;
	}


	/**
	 * Returns a string representation of the NoteBook, consisting of the name
	 * and pagecount.
	 */
	public String toString() {
		return String.format("%s (%d)", name, getSheetCount());
	}


	/**
	 * If the index of the current item was changed, the object reference needs
	 * to be updated as well. This method does just that.
	 */
	private void updateCurrrentItem() {
		addPageIfEmpty();
		if (currentSheet < 0 || currentSheet >= sheets.size()) {
			throw new IndexOutOfBoundsException(String.format("Index error with NoteBook \"%s\", Index %d of %s", name, currentSheet, sheets.size()));
		}
		current = sheets.get(currentSheet);
	}
}

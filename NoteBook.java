// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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


	/**
	 * Creates an empty note book with a single note sheet.
	 *
	 * @param noteSize size of the NoteSheet within the NoteBook
	 * @param folder place to store images
	 * @param name name of the NoteBook
	 */
	public NoteBook(Dimension noteSize, File folder, String name) {
		this.noteSize = noteSize;

		this.folder = folder;
		this.name = name;

		sheets = new LinkedList<NoteSheet>();

		// if a NoteBook should be used
		if (folder != null && name != null) {
			if (!folder.exists()) {
				folder.mkdirs();
			}

			// try to load all images that match the name
			File[] allImages = folder.listFiles(new NoteSheetFileFilter(name));

			if (allImages != null && allImages.length > 0) {
				Arrays.sort(allImages, new Comparator<File>() {

					private Collator c = Collator.getInstance();

					public int compare(File o1, File o2) {
						if (o1 == o2) {
							return 0;
						}

						File f1 = (File) o1;
						File f2 = (File) o2;

						if (f1.isDirectory() && f2.isFile()) {
							return -1;
						}
						if (f1.isFile() && f2.isDirectory()) {
							return 1;
						}

						return c.compare(f1.getName(), f2.getName());
					}
				});


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
		}

		// add an empty sheet if the NoteBook would be empty otherwise
		if (sheets.size() == 0) {
			sheets.add(new NoteSheet(noteSize, pagecount, generateNextFilename(pagecount)));
			pagecount++;
		}

		updateCurrrentItem();
	}


	/**
	 * Draws a line onto the current sheet.
	 */
	public void drawLine(int x, int y, int x2, int y2) {
		current.drawLine(x, y, x2, y2);

		fireDoneDrawing();
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
	 * @return The number of pages given out so far.
	 */
	public int getPagecount() {
		return pagecount;
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
	 * Persists the whole NoteBook into individual files.
	 */
	public void saveToFiles() {
		for (NoteSheet s : sheets) {
			s.saveToFile();
		}
		quitWithWriteoutThread();

	}


	/**
	 * Returns a string representation of the NoteBook, consisting of the name
	 * and pagecount.
	 */
	public String toString() {
		return String.format("%s (%d)", name, getSheetCount());
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
	 * Gets the NoteSheet object which the currently open page of the NoteBook.
	 *
	 * @return current NoteSheet
	 */
	public NoteSheet getCurrentSheet() {
		return sheets.get(currentSheet);
	}


	/**
	 * @return number of sheets in the NoteBook
	 */
	public int getSheetCount() {
		return sheets.size();
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
		currentSheet = sheets.size() - 1;
		updateCurrrentItem();
		fireDoneDrawing();
	}


	/**
	 * Returns the name of the NoteBook.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * If the index of the current item was changed, the object reference needs
	 * to be updated as well. This method does just that.
	 */
	private void updateCurrrentItem() {
		assert(currentSheet >= 0);
		assert(currentSheet < sheets.size());
		current = sheets.get(currentSheet);
	}


	/**
	 * Tells the WriteoutThread that this NoteBook has no more sheets to save.
	 */
	private void quitWithWriteoutThread() {
		sheets.getFirst().stopWriteoutThread();

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
}

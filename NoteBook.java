// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;


/**
 * A container for several NoteSheets.
 *
 * The NoteBook contains at least one NoteSheet and adds new sheets whenever
 * the forward() function is called. The whole notebook can be saved into
 * individual pictures.
 */
public class NoteBook {
	private LinkedList<NoteSheet> sheets;

	private int currentSheet = 0;

	private File folder;

	private NoteSheet current;

	private Dimension noteSize;

	private ActionListener doneDrawing;

	/**
	 * Count of pages. Latest page number is pagecount.
	 */
	private int pagecount = 1;

	private String name;

	/**
	 * Creates an empty note book with a single note sheet.
	 *
	 * @param width width of the individual sheets
	 * @param height height of the individual sheets
	 */
	public NoteBook(Dimension noteSize, File folder, String name) {
		this.noteSize = noteSize;

		this.folder = folder;
		this.name = name;

		sheets = new LinkedList<NoteSheet>();

		// if a notebook should be used
		if (folder != null && name != null) {
			if (!folder.exists()) {
				folder.mkdirs();
			}

			// try to load all images that match the name
			File[] allImages = folder.listFiles(new NoteSheetFileFilter(name));

			if (allImages != null) {
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

				for (File file : allImages) {
					sheets.add(new NoteSheet(noteSize, pagecount++, file));
				}
			}

		}

		// add an empty sheet if the notebook would be empty otherwise
		if (sheets.size() == 0) {
			sheets.add(new NoteSheet(noteSize, pagecount, generateNextFilename(pagecount)));
			pagecount++;
		}


		updateCurrrentItem();
	}

	private File generateNextFilename(int pagenumber) {
		if (folder != null && name != null) {
			try {
				return new File(folder.getCanonicalPath() + File.separator + name + "-" + String.format("%06d", pagenumber) + ".png");
			}
			catch (IOException e) {
				NoteBookProgram.handleError("Could not determine path of notebook folder.");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Draws a line onto the current sheet.
	 */
	public void drawLine(int x, int y, int x2, int y2) {
		current.drawLine(x, y, x2, y2);

		fireDoneDrawing();
	}

	/**
	 * Sets an action listener to be called when something new was drawn.
	 *
	 * @param doneDrawing ActionListener to be called after drawing a new line.
	 */
	public void setDoneDrawing(ActionListener doneDrawing) {
		this.doneDrawing = doneDrawing;
	}

	private void fireDoneDrawing() {
		if (doneDrawing != null) {
			doneDrawing.actionPerformed(null);
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
	 * @return The number of pages given out so far.
	 */
	public int getPagecount() {
		return pagecount;
	}

	/**
	 * How many images to cache back and front.
	 */
	private int cacheWidth = 10;

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

	public NoteSheet getCurrentSheet() {
		return sheets.get(currentSheet);
	}

	private void updateCurrrentItem() {
		assert(currentSheet >= 0);
		assert(currentSheet < sheets.size());
		current = sheets.get(currentSheet);
	}

	/**
	 * Persists the whole notebook into individual files.
	 */
	public void saveToFiles() {
		for (NoteSheet s : sheets) {
			s.saveToFile();
		}
		quitWithWriteoutThread();

	}

	/**
	 * @return number of sheets in the notebook
	 */
	public int getSheetCount() {
		return sheets.size();
	}

	private void quitWithWriteoutThread() {
		sheets.getFirst().stopWriteoutThread();

	}

	public String toString() {
		return name;
	}
	
	public void gotoFirst() {
		currentSheet = 0;
		updateCurrrentItem();
		fireDoneDrawing();
	}
	
	public void gotoLast() {
		currentSheet = sheets.size()-1;
		updateCurrrentItem();
		fireDoneDrawing();
	}

	public String getName() {
		return name;
	}

}

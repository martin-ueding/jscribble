// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package tests.jscribble.notebook;

import java.io.File;

import jscribble.NoteBookProgram;
import jscribble.notebook.NoteBook;
import jscribble.notebook.NoteSheet;
import junit.framework.TestCase;

public class NoteBookTest extends TestCase {

	public NoteBookTest() {
		super();
	}

	/**
	 * Creates a NoteBook stored in a temporary folder with a unique name.
	 */
	private NoteBook createTempNoteBook() {
		return new NoteBook(null);
	}

	/**
	 * Tests whether the current sheet of a new NoteBook is not null.
	 */
	public void testCurrentSheet() {
		NoteBook nb = createTempNoteBook();
		assertNotNull(nb.getCurrentSheet());
	}

	/**
	 * Creates a NoteBook with many pages and deletes one of them. The NoteBook
	 * is then reloaded from the configuration file. The NoteBook should now
	 * have one page less than before.
	 */
	public void testDeletionOfPictureFile() {
		NoteBook nb = new NoteBook(null);

		File[] filenames = new File[20];

		nb.drawLine(0, 0, 0, 0);

		for (int i = 0; i < filenames.length; i++) {
			nb.goForward();
			nb.drawLine(0, 0, 0, 0);
			filenames[i] = nb.getCurrentSheet().getFilename();
		}

		nb.saveToFiles();

		int oldSheetCount = nb.getSheetCount();

		// delete a file from the notebook
		for (File file : filenames) {
			assertTrue(String.format("File %s should exist.", file.getAbsolutePath()), file.exists());
		}
		filenames[3].delete();
		assertFalse(filenames[3].exists());

		NoteBook reloaded = new NoteBook(nb.getName());

		assertEquals(oldSheetCount - 1, reloaded.getSheetCount());

		NoteBookProgram.log(getClass().getName(), "Trying to delete test files.");
		for (File file : filenames) {
			file.delete();
		}
	}

	/**
	 * Tests whether an newly created NoteBook has zero sheets in it.
	 */
	public void testEmptySheetCount() {
		NoteBook nb = createTempNoteBook();
		assertEquals(0, nb.getSheetCount());
	}

	/**
	 * Tests the forward and backward functions in the NoteBook.
	 */
	public void testGoBackwards() {
		NoteBook nb = createTempNoteBook();

		// Try going back although the notebook is already at the first page.
		assertEquals(1, nb.getCurrentSheet().getPagenumber());
		nb.goBackwards();
		assertEquals(1, nb.getCurrentSheet().getPagenumber());

		// Go one page forward.
		nb.drawLine(0, 0, 0, 0);
		nb.goForward();
		assertEquals(2, nb.getCurrentSheet().getPagenumber());

		// Go back to the start.
		nb.goBackwards();
		assertEquals(1, nb.getCurrentSheet().getPagenumber());

		// Advance several pages.
		for (int i = 0; i < 15; i++) {
			nb.goForward();
			nb.drawLine(0, 0, 0, 0);
			assertEquals(i + 2, nb.getCurrentSheet().getPagenumber());
		}

		// Go back again
		for (int i = 15; i > 0; i--) {
			nb.goBackwards();
			nb.drawLine(0, 0, 0, 0);
			assertEquals(Math.max(1, i), nb.getCurrentSheet().getPagenumber());
		}
	}

	/**
	 * Tests whether a reloaded NoteBook is the same as the saved one.
	 */
	public void testLoadFromConfig() {
		NoteBook nb = createTempNoteBook();
		nb.drawLine(0, 0, 0, 0);
		nb.saveToFiles();

		NoteBook reloaded = new NoteBook(nb.getName());

		assertEquals(nb.getName(), reloaded.getName());

		nb.gotoFirst();
		reloaded.gotoFirst();

		assertEquals(nb.getCurrentSheet().getFilename().getAbsolutePath(), reloaded.getCurrentSheet().getFilename().getAbsolutePath());
		assertEquals(nb.getSheetCount(), reloaded.getSheetCount());
	}

	/**
	 * Tests whether the page number advances when you go a page forward.
	 */
	public void testPageNumber() {
		NoteBook nb = createTempNoteBook();
		assertEquals(1, nb.getCurrentSheet().getPagenumber());
		nb.drawLine(0, 0, 0, 0);
		assertEquals(1, nb.getCurrentSheet().getPagenumber());
		nb.goForward();
		assertEquals(2, nb.getCurrentSheet().getPagenumber());
	}

	/**
	 * Tests whether saving the NoteBook actually creates image files. This
	 * test draws a line onto the first page.
	 */
	public void testSavingAfterDrawing() {
		NoteBook nb = createTempNoteBook();
		NoteSheet current = nb.getCurrentSheet();
		nb.drawLine(0, 0, 0, 0);
		nb.saveToFiles();
		assertTrue(current.getFilename().exists());
		assertNotSame(0, current.getFilename().length());
	}

	/**
	 * Tests whether saving the NoteBook actually creates image files. This
	 * test draws a line and advances one page. It should only save one file.
	 */
	public void testSavingAfterDrawingAndAdvance() {
		NoteBook nb = createTempNoteBook();
		NoteSheet current = nb.getCurrentSheet();
		nb.drawLine(0, 0, 0, 0);
		nb.goForward();
		nb.saveToFiles();
		assertTrue(current.getFilename().exists());
		assertNotSame(0, current.getFilename().length());
		current = nb.getCurrentSheet();
		assertFalse(current.getFilename().exists());
	}

	/**
	 * Tests whether saving a new NoteBook without any lines creates no files.
	 */
	public void testSavingAfterNew() {
		NoteBook nb = createTempNoteBook();
		NoteSheet current = nb.getCurrentSheet();
		nb.saveToFiles();
		assertFalse(current.getFilename().exists());
		//assertSame(String.format("File %s should be empty.", current.getFilename().getAbsolutePath()), 0, current.getFilename().length());
	}

	/**
	 * Tests whether saving the NoteBook actually creates image files. This
	 * test draws a line on page one and two and checks whether two pages are
	 * created.
	 */
	public void testSavingAfterTwoDrawingAndAdvance() {
		NoteBook nb = createTempNoteBook();
		NoteSheet current = nb.getCurrentSheet();
		nb.drawLine(0, 0, 0, 0);
		nb.goForward();
		nb.drawLine(0, 0, 0, 0);
		nb.saveToFiles();
		assertTrue(current.getFilename().exists());
		assertNotSame(0, current.getFilename().length());
		current = nb.getCurrentSheet();
		assertTrue(current.getFilename().exists());
		assertNotSame(0, current.getFilename().length());
	}

	/**
	 * Checks whether a new NoteBook with a line on the first page has one
	 * page.
	 */
	public void testSheetCountAfterFirstLine() {
		NoteBook nb = createTempNoteBook();
		nb.drawLine(1, 1, 2, 2);
		assertEquals(1, nb.getSheetCount());
	}

	/**
	 * Tests whether a new NoteBook has an untouched first page.
	 */
	public void testUntouchedFirstPage() {
		NoteBook nb = createTempNoteBook();
		assertFalse(nb.getCurrentSheet().touched());
	}
}

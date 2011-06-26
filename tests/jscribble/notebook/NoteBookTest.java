// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package tests.jscribble.notebook;

import java.awt.Dimension;
import java.io.File;
import java.util.UUID;

import jscribble.NoteBookProgram;
import jscribble.notebook.NoteBook;
import jscribble.notebook.NoteSheet;
import junit.framework.TestCase;

public class NoteBookTest extends TestCase {

	private NoteBook createTempNoteBook() {
		return new NoteBook(new Dimension(100, 100), new File(System.getProperty("java.io.tmpdir")), UUID.randomUUID().toString());
	}

	public NoteBookTest() {
		super();
	}

	public void testEmptySheetCount() {
		NoteBook nb = createTempNoteBook();
		assertEquals(0, nb.getSheetCount());
	}

	public void testUntouchedFirstPage() {
		NoteBook nb = createTempNoteBook();
		assertFalse(nb.getCurrentSheet().touched());
	}

	public void testCurrentSheet() {
		NoteBook nb = createTempNoteBook();
		assertNotNull(nb.getCurrentSheet());
	}

	public void testSheetCountAfterFirstLine() {
		NoteBook nb = createTempNoteBook();
		nb.drawLine(1, 1, 2, 2);
		assertEquals(1, nb.getSheetCount());
	}

	public void testWritingToConfigFile() {
		NoteBook nb = createTempNoteBook();
		nb.saveToConfig(new File(System.getProperty("java.io.tmpdir")));
		File outfile = nb.getConfigFile();
		assertNotNull(outfile);
		assertTrue(outfile.exists());
		nb.deleteSure();
		assertFalse(outfile.exists());
	}

	public void testSavingAfterDrawing() {
		NoteBook nb = createTempNoteBook();
		NoteSheet current = nb.getCurrentSheet();
		nb.drawLine(0, 0, 0, 0);
		nb.saveToFiles();
		assertTrue(current.getFilename().exists());
		assertNotSame(0, current.getFilename().length());
	}

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

	public void testSavingAfterNew() {
		NoteBook nb = createTempNoteBook();
		NoteSheet current = nb.getCurrentSheet();
		nb.saveToFiles();
		assertFalse(current.getFilename().exists());
		//assertSame(String.format("File %s should be empty.", current.getFilename().getAbsolutePath()), 0, current.getFilename().length());
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

	public void testLoadFromConfig() {
		NoteBook nb = createTempNoteBook();
		nb.saveToConfig(new File(System.getProperty("java.io.tmpdir")));
		nb.drawLine(0, 0, 0, 0);
		nb.saveToFiles();

		File outfile = nb.getConfigFile();

		NoteBook reloaded = new NoteBook(outfile);

		assertEquals(nb.getName(), reloaded.getName());

		nb.gotoFirst();
		reloaded.gotoFirst();

		assertEquals(nb.getCurrentSheet().getFilename().getAbsolutePath(), reloaded.getCurrentSheet().getFilename().getAbsolutePath());
		assertEquals(nb.getSheetCount(), reloaded.getSheetCount());

		outfile.delete();
		assertFalse(outfile.exists());
	}

	public void testDeletionOfPictureFile() {
		NoteBook nb = new NoteBook(new Dimension(10, 10), new File(System.getProperty("java.io.tmpdir")), UUID.randomUUID().toString());
		nb.saveToConfig(new File(System.getProperty("java.io.tmpdir")));
		File outfile = nb.getConfigFile();

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

		NoteBook reloaded = new NoteBook(outfile);

		assertEquals(oldSheetCount - 1, reloaded.getSheetCount());

		NoteBookProgram.log(getClass().getName(), "Trying to delete test files.");
		for (File file : filenames) {
			file.delete();
		}
		nb.getConfigFile().delete();
	}

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
}

// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package tests.jscribble.notebook;

import java.awt.Dimension;
import java.io.File;

import jscribble.notebook.NoteBook;
import jscribble.notebook.NoteSheet;
import junit.framework.TestCase;

public class NoteBookTest extends TestCase {

	private NoteBook createTempNoteBook() {
		return new NoteBook(new Dimension(100, 100), new File(System.getProperty("java.io.tmpdir")), "JUnit_Test");
	}

	public NoteBookTest() {
		super();
	}

	public void testEmptySheetCount() {
		NoteBook nb = createTempNoteBook();
		assertEquals(nb.getSheetCount(), 0);
	}

	public void testName() {
		NoteBook nb = createTempNoteBook();
		assertEquals("JUnit_Test", nb.getName());
	}

	public void testCurrentSheet() {
		NoteBook nb = createTempNoteBook();
		assertNotNull(nb.getCurrentSheet());
	}

	public void testSheetCountAfterFirstLine() {
		NoteBook nb = createTempNoteBook();
		assertEquals(nb.getSheetCount(), 0);
		nb.drawLine(1, 1, 2, 2);
		assertEquals(nb.getSheetCount(), 1);
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

	public void testSaving() {
		NoteBook nb = createTempNoteBook();
		NoteSheet current = nb.getCurrentSheet();
		nb.drawLine(0, 0, 0, 0);
		nb.saveToFiles();
		assertNotSame(current.getFilename().length(), 0);
	}

	public void testPageNumber() {
		NoteBook nb = createTempNoteBook();
		assertEquals(nb.getCurrentSheet().getPagenumber(), 1);
		nb.drawLine(0, 0, 0, 0);
		assertEquals(nb.getCurrentSheet().getPagenumber(), 1);
		nb.goForward();
		assertEquals(nb.getCurrentSheet().getPagenumber(), 2);
	}
}

// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package tests.jscribble.notebook;

import java.awt.Dimension;
import java.io.File;

import jscribble.notebook.NoteBook;
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
}

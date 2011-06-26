package tests.jscribble.notebook;
import java.awt.Dimension;

import jscribble.notebook.NoteSheet;
import junit.framework.TestCase;


public class NoteSheetTest extends TestCase {

	public NoteSheetTest() {
		super();
	}

	/**
	 * Positive control.
	 */
	public void testPositive() {
		assertTrue(true);
	}

	/**
	 * Tests whether drawing a line causes a change in color in the image.
	 */
	public void testDrawing() {
		NoteSheet n = new NoteSheet(new Dimension(1024, 600), 0, null);
		assertNotNull(n);
		assertNotNull(n.getImg());
		int previousColor = n.getImg().getRGB(100, 100);
		n.drawLine(100, 100, 100, 200);
		int rgbarray[] = n.getImg().getRGB(100, 100, 1, 1, null, 0, 1);
		assertTrue(rgbarray.length > 0);
		assertFalse(rgbarray[0] == previousColor);
	}
}

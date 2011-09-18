// Copyright © 2011 Martin Ueding <dev@martin-ueding.de>

/*
 * This file is part of jscribble.
 *
 * jscribble is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 *
 * jscribble is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * jscribble.  If not, see <http://www.gnu.org/licenses/>.
 */

package tests.jscribble.notebook;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import jscribble.notebook.ImageSwapTask;
import jscribble.notebook.WriteoutThread;
import junit.framework.TestCase;

public class WriteoutThreadTest extends TestCase {
	public WriteoutThreadTest() {
		super();
	}


	/**
	 * Schedules a single task and tests whether it is written to disk.
	 */
	public void testSingleWriteout() {
		WriteoutThread wt = new WriteoutThread();

		File outfile = null;
		try {
			outfile = File.createTempFile("JUnit-testSingleWriteout-", ".png");
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}

		assertNotNull(outfile);

		assertTrue(outfile.exists());
		assertEquals(0L, outfile.length());


		wt.schedule(new ImageSwapTask(new BufferedImage(10, 10,
		            BufferedImage.TYPE_BYTE_GRAY), outfile));

		assertTrue(wt.isAlive());

		wt.stopAfterLast();
		try {
			wt.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertFalse(wt.isAlive());

		assertTrue(outfile.exists());
		assertTrue(outfile.length() > 0);


		outfile.delete();
		assertFalse(outfile.exists());
	}
}

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

package tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import tests.jscribble.VersionNameTest;
import tests.jscribble.drawPanel.HelpItemTest;
import tests.jscribble.notebook.NoteBookTest;
import tests.jscribble.notebook.NoteSheetFileFilterTest;
import tests.jscribble.notebook.NoteSheetTest;
import tests.jscribble.notebook.WriteoutThreadTest;

public class JscribbleTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(NoteBookTest.class);
		suite.addTestSuite(NoteSheetTest.class);
		suite.addTestSuite(WriteoutThreadTest.class);
		suite.addTestSuite(VersionNameTest.class);
		suite.addTestSuite(NoteSheetFileFilterTest.class);
		suite.addTestSuite(HelpItemTest.class);
		return suite;
	}
}

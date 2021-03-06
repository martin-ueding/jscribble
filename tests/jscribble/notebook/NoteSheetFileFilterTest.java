// Copyright © 2011 Martin Ueding <mu@martin-ueding.de>

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

package tests.jscribble.notebook;

import jscribble.notebook.NoteSheetFileFilter;
import junit.framework.TestCase;

public class NoteSheetFileFilterTest extends TestCase {
    private NoteSheetFileFilter filter = new NoteSheetFileFilter();

    public void testLetters() {
        assertFalse(filter.accept(null, "asdfgh.png"));
    }

    public void testZero() {
        assertFalse(filter.accept(null, "000000.png"));
    }

    public void testLongNumber() {
        assertTrue(filter.accept(null, "1234567890.png"));
    }

    public void testAlphanumeric() {
        assertFalse(filter.accept(null, "asd123.png"));
    }

    public void testNegativeNumber() {
        assertFalse(filter.accept(null, "-123456.png"));
    }

    public void testShort() {
        assertTrue(filter.accept(null, "1.png"));
    }

    public void testSimple() {
        assertTrue(filter.accept(null, "000001.png"));
    }

    public void testSpaces() {
        assertFalse(filter.accept(null, " 000001.png"));
        assertFalse(filter.accept(null, "000001.png "));
    }

    public void testWrongSuffix() {
        assertFalse(filter.accept(null, "00000.jpg"));
        assertFalse(filter.accept(null, "000001.jpg"));
    }

    public void testNoSuffix() {
        assertFalse(filter.accept(null, "00000"));
        assertFalse(filter.accept(null, "000001"));
    }
}

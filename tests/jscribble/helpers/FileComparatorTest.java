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

package tests.jscribble.helpers;

import java.io.File;

import jscribble.helpers.FileComparator;
import junit.framework.TestCase;

public class FileComparatorTest extends TestCase {
    private FileComparator fc = new FileComparator();

    public void testEqual() {
        File a = new File("test");
        File b = new File("test");
        assertEquals(0, fc.compare(a, b));
    }

    public void testGreater() {
        File a = new File("foobar");
        File b = new File("test");
        assertTrue(fc.compare(a, b) < 0);
    }

    public void testLess() {
        File a = new File("foobar");
        File b = new File("test");
        assertTrue(fc.compare(b, a) > 0);
    }
}

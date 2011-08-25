// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

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

package tests.jscribble;

import java.util.regex.Pattern;

import jscribble.VersionName;
import junit.framework.TestCase;

/**
 * Tests the VersionName class.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class VersionNameTest extends TestCase {
	public VersionNameTest() {
		super();
	}


	/**
	 * Checks whether the version name is in the correct format x.xx~xxx.
	 */
	public void testVersionName() {
		Pattern p = Pattern.compile("\\d+(\\.\\d+)+~\\d+");
		assertTrue(p.matcher(VersionName.version).matches());
	}

}

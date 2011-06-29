// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

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

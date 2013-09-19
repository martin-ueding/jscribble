// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package tests.jscribble;

import java.util.regex.Pattern;

import jscribble.VersionName;
import junit.framework.TestCase;

public class VersionNameTest extends TestCase {
    public VersionNameTest() {
        super();
    }

    public void testVersionName() {
        Pattern p = Pattern.compile("\\d+(\\.\\d+)+");
        assertTrue(p.matcher(VersionName.version).matches());
    }

}
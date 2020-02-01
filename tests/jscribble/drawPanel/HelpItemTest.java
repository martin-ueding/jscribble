// Copyright © 2011 Martin Ueding <martin-ueding.de>

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

package tests.jscribble.drawPanel;

import java.awt.event.KeyEvent;

import jscribble.drawPanel.HelpItem;
import junit.framework.TestCase;

public class HelpItemTest extends TestCase {

    public void testChar() {
        HelpItem hi = new HelpItem("a", "text");
        assertEquals("A", hi.key);
    }

    public void testCharArray() {
        HelpItem hi = new HelpItem("a,b", "text");
        assertEquals("A, B", hi.key);
    }

    public void testKeyCode() {
        HelpItem hi = new HelpItem(String.valueOf(KeyEvent.VK_ENTER), "text");
        assertEquals(KeyEvent.getKeyText(KeyEvent.VK_ENTER), hi.key);
    }

    public void testKeyCodeArray() {
        HelpItem hi = new HelpItem(String.valueOf(KeyEvent.VK_ENTER) + ",0" + String.valueOf(KeyEvent.VK_BACK_SPACE), "text");
        assertEquals(KeyEvent.getKeyText(KeyEvent.VK_ENTER) + ", " + KeyEvent.getKeyText(KeyEvent.VK_BACK_SPACE), hi.key);
    }

    public void testOneDigitKeyCodeWithoutPadding() {
        HelpItem hi = new HelpItem(String.valueOf(KeyEvent.VK_BACK_SPACE), "text");
        assertFalse(KeyEvent.getKeyText(KeyEvent.VK_BACK_SPACE).equals(hi.key));
    }

    public void testOneDigitKeyCodeWithPadding() {
        HelpItem hi = new HelpItem("0" + String.valueOf(KeyEvent.VK_BACK_SPACE), "text");
        assertEquals(KeyEvent.getKeyText(KeyEvent.VK_BACK_SPACE), hi.key);
    }

    public void testText() {
        HelpItem hi = new HelpItem("a", "text");
        assertEquals("text", hi.helptext);
    }

}

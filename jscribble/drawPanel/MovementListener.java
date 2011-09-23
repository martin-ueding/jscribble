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

package jscribble.drawPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class MovementListener implements KeyListener {
	private final DrawPanel drawPanel;

	public MovementListener(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
	}

	private boolean isKeyGoBack(KeyEvent event) {
		return event.getKeyChar() == 'k' ||
		       event.getKeyCode() == KeyEvent.VK_UP ||
		       event.getKeyCode() == KeyEvent.VK_LEFT ||
		       event.getKeyCode() == KeyEvent.VK_BACK_SPACE;
	}

	private boolean isKeyGoForward(KeyEvent event) {
		return event.getKeyChar() == 'j' ||
		       event.getKeyCode() == KeyEvent.VK_DOWN ||
		       event.getKeyCode() == KeyEvent.VK_RIGHT ||
		       event.getKeyCode() == KeyEvent.VK_SPACE ||
		       event.getKeyCode() == KeyEvent.VK_ENTER;
	}

	private boolean isKeyGotoFirst(KeyEvent event) {
		return event.getKeyChar() == 'f' ||
		       event.getKeyCode() == KeyEvent.VK_HOME;
	}

	private boolean isKeyGotoLast(KeyEvent event) {
		return event.getKeyChar() == 'l' ||
		       event.getKeyCode() == KeyEvent.VK_END;
	}

	public void keyPressed(KeyEvent event) {}

	public void keyReleased(KeyEvent event) {
		// TODO externalize keys

		if (isKeyGoForward(event)) {
			drawPanel.goForward();
		}

		if (isKeyGoBack(event)) {
			drawPanel.goBackwards();
		}

		if (isKeyGotoFirst(event)) {
			drawPanel.gotoFirst();
		}

		if (isKeyGotoLast(event)) {
			drawPanel.gotoLast();
		}
	}

	public void keyTyped(KeyEvent event) {}
}

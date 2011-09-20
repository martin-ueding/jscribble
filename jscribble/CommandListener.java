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

package jscribble;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Listens to the keyboard for the command key and then polls the user for a
 * command. The command is then executed.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class CommandListener implements KeyListener {
	/**
	 * The DrawPanel that is displaying the NoteBook.
	 */
	private DrawPanel panel;

	/**
	 * The listener that is to be notified when something is changed.
	 */
	private Redrawer redrawer;

	/**
	 * Creates a new listener that listens to the given panel
	 *
	 * @param malPanel panel to listen to
	 */
	public CommandListener(DrawPanel malPanel) {
		panel = malPanel;
	}

	/**
	 * A listener to be called when a setting causes the panel to need
	 * refreshing.
	 *
	 * @param redrawer listener to call after setting change
	 */
	public void addChangeListener(Redrawer redrawer) {
		this.redrawer = redrawer;
	}

	/**
	 * Ignored.
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	/**
	 * Ignored.
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if (arg0.getKeyChar() == 'r') {
			panel.toggleRuling();
			redrawer.actionPerformed(null);
		}

		if (arg0.getKeyChar() == 'g') {
			panel.toggleGraphRuling();
			redrawer.actionPerformed(null);
		}

		if (arg0.getKeyChar() == 'h' || arg0.getKeyCode() == KeyEvent.VK_F1) {
			panel.toggleHelp();
			redrawer.actionPerformed(null);
		}

		if (arg0.getKeyChar() == '+') {
			panel.onionLayersIncrease();
		}

		if (arg0.getKeyChar() == '-') {
			panel.onionLayersDecrease();
		}

		// Close the help when the <ESC> key is pressed.
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			panel.setShowHelp(false);
			redrawer.actionPerformed(null);
		}
	}
}

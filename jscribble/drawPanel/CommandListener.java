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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import jscribble.helpers.SettingsWrapper;

/**
 * Listens to the keyboard for the command key and then polls the user for a
 * command. The command is then executed.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class CommandListener implements KeyListener, MouseListener {
	/**
	 * The DrawPanel that is displaying the NoteBook.
	 */
	private DrawPanel drawPanel;

	/**
	 * The listener that is to be notified when something is changed.
	 */
	private Redrawer redrawer;

	/**
	 * Creates a new listener that listens to the given panel
	 *
	 * @param drawPanel panel to listen to
	 */
	public CommandListener(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		drawPanel.addMouseListener(this);
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
	public void keyPressed(KeyEvent ignored) {
	}

	/**
	 * Handles various keyboard commands.
	 */
	@Override
	public void keyReleased(KeyEvent event) {
		if (SettingsWrapper.isKeyForCommand(event, "help_screen_close_key")) {
			drawPanel.setShowHelp(false);
			redrawer.actionPerformed(null);
		}
		else if (SettingsWrapper.isKeyForCommand(event, "help_screen_toggle_key")) {
			drawPanel.toggleHelp();
			redrawer.actionPerformed(null);
		}
		else if (SettingsWrapper.isKeyForCommand(event, "ruling_toggle_key")) {
			drawPanel.toggleRuling();
			redrawer.actionPerformed(null);
		}
		else if (SettingsWrapper.isKeyForCommand(event, "ruling_graph_toggle_key")) {
			drawPanel.toggleGraphRuling();
			redrawer.actionPerformed(null);
		}
		else if (SettingsWrapper.isKeyForCommand(event, "onion_layer_increase_key")) {
			drawPanel.onionLayersIncrease();
		}
		else if (SettingsWrapper.isKeyForCommand(event, "onion_layer_decrease_key")) {
			drawPanel.onionLayersDecrease();
		}

		// TODO add extra exit button, maybe "q"
	}

	/**
	 * Ignored.
	 */
	@Override
	public void keyTyped(KeyEvent ignored) {
	}

	/**
	 * Checks for third and fourth mouse button.
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		if (SettingsWrapper.isButtonForCommand(event, "notebook_go_forward_mouse_button")) {
			drawPanel.goForward();
		}
		else if (SettingsWrapper.isButtonForCommand(event, "notebook_go_back_mouse_button")) {
			drawPanel.goBackwards();
		}
	}

	/**
	 * Ignored.
	 */
	@Override
	public void mouseEntered(MouseEvent ignored) {

	}

	/**
	 * Ignored.
	 */
	@Override
	public void mouseExited(MouseEvent ignored) {

	}

	/**
	 * Ignored.
	 */
	@Override
	public void mousePressed(MouseEvent ignored) {

	}

	/**
	 * Ignored.
	 */
	@Override
	public void mouseReleased(MouseEvent ignored) {

	}
}

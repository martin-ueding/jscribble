// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

/*
 * This file is part of jscribble.
 *
 * Foobar is free software: you can redistribute it and/or modify it under the
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

package jscribble;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;


/**
 * Listens to changes from the NoteBook and updates the panel.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class Redrawer implements ActionListener {
	/**
	 * Panel which is to be redrawn.
	 */
	private JPanel panel;


	/**
	 * Creates a new Redrawer that tells the given Panel to redraw.
	 *
	 * @param panel panel to redraw when notified
	 */
	public Redrawer(JPanel panel) {
		this.panel = panel;
	}


	/**
	 * Redraws the previously set panel.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		panel.repaint();
	}
}

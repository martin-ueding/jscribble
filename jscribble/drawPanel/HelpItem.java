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

/**
 * A container for a key and help text.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
class HelpItem {
	/**
	 * The buttons(s) that cause some action.
	 */
	public String key;

	/**
	 * The action the buttons cause.
	 */
	public String helptext;

	/**
	 * Generates a new HelpItem.
	 */
	public HelpItem(String key, String helptext) {
		this.key = key;
		this.helptext = helptext;
	}
}
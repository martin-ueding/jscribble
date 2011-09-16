// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Localizes strings.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class Localizer {

	static ResourceBundle bundle;

	public static String get(String ident) {
		// Try to load the ResourceBundle.
		if (bundle == null) {
			try {
				bundle = ResourceBundle.getBundle("jscribble");
			}
			catch (ExceptionInInitializerError e) {
				NoteBookProgram.log(Localizer.class.getClass().getName(),
				        "Error: " + e.getMessage());
				bundle = null;
			}
			catch (MissingResourceException e) {
				NoteBookProgram.log(Localizer.class.getClass().getName(),
				        "Error: " + e.getMessage());
				bundle = null;
			}
		}

		// Do not translate if there is no translation around.
		if (bundle == null) {
			return ident;
		}
		else {
			try {
				return bundle.getString(ident);
			}
			catch (MissingResourceException e) {
				NoteBookProgram.log(Localizer.class.getClass().getName(),
				        e.getMessage());
				return ident;
			}
		}
	}
}

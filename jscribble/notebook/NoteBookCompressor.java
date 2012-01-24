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

package jscribble.notebook;

import java.io.File;
import java.util.Arrays;

import jscribble.helpers.FileComparator;
import jscribble.helpers.Localizer;
import jscribble.helpers.Logger;

/**
 * Takes a folder which contains a notebook and compresses the file names so
 * that there are no gaps in the numbering.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class NoteBookCompressor {
	File folder;

	public NoteBookCompressor(File folder) {
		this.folder = folder;
	}

	public void compress() {
		// Gather all the file names from the folder.
		File[] allImages = folder.listFiles(new NoteSheetFileFilter());

		// If there are no images in this folder, there is nothing to do.
		if (allImages == null || allImages.length == 0) {
			return;
		}

		// Sort the file names.
		Arrays.sort(allImages, new FileComparator());

		// Stop if it is already in the correct order.
		if (allImages[allImages.length - 1].getName().equalsIgnoreCase(String.format("%06d.png", allImages.length))) {
			Logger.log(getClass().getName(),
			           String.format(Localizer.get("Stop processing %s"), folder.getName()));
			return;
		}

		// Rename the files according to their place in the array.
		for (int i = 0; i < allImages.length; i++) {
			File image = allImages[i];

			image.getParent();

			File newName = new File(
			    image.getParent()
			    + File.separator
			    + String.format("%06d.png", i + 1)
			);

			Logger.log(getClass().getName(), String.format(Localizer.get("Rename %s to %s."), image.getName(), newName.getName()));
			image.renameTo(newName);
		}
	}
}

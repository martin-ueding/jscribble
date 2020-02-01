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

package jscribble.helpers;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;

/**
 * Compares two files by their file names.
 */
public class FileComparator implements Comparator<File> {
    /**
     * Collator to compare strings.
     */
    private Collator c = Collator.getInstance();

    /**
     * Compares two files by their filenames.
     */
    public int compare(File o1, File o2) {
        if (o1 == o2) {
            return 0;
        }

        File f1 = (File) o1;
        File f2 = (File) o2;

        return c.compare(f1.getName(), f2.getName());
    }
}

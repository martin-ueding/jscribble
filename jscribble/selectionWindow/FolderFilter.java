// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package jscribble.selectionWindow;

import java.io.File;
import java.io.FilenameFilter;

class FolderFilter implements FilenameFilter {
	@Override
	public boolean accept(File arg0, String arg1) {
		return arg0.isDirectory();
	}
}
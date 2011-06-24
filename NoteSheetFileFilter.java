// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;


public class NoteSheetFileFilter implements FilenameFilter {

	private String basename;
	
	private Pattern p = Pattern.compile("^[\\d-]+-(\\d)+\\.png");

	public NoteSheetFileFilter(String name) {
		basename = name;
	}

	@Override
	public boolean accept(File arg0, String arg1) {
		if (p.matcher(arg1).matches() && arg1.startsWith(basename)) {
			return false;
		}

		return true;
	}

}

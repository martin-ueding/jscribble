// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;


public class NoteSheetFileFilter implements FilenameFilter {

	private String basename;

	private Pattern p = Pattern.compile("\\D+-(\\d+)\\.png");

	public NoteSheetFileFilter(String name) {
		basename = name;
	}

	@Override
	public boolean accept(File arg0, String arg1) {
		String[] nameparts = arg1.split(Pattern.quote(File.separator));
		String fileBasename = nameparts[nameparts.length-1];
		return p.matcher(fileBasename).matches() && fileBasename.startsWith(basename);
	}

}

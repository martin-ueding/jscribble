package jscribble.notebook;
// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Filters filenames that belong to NoteSheet from a specific NoteBook.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class NoteSheetFileFilter implements FilenameFilter {

	/**
	 * The prefix of the filenames, the name of the NoteBook.
	 */
	private String basename;


	/**
	 * The pattern that every filename is matched against.
	 */
	private Pattern p = Pattern.compile("\\D+-(\\d+)\\.png");


	/**
	 * Creates a new FileFilter that filters files starting with the given name
	 * -- the name of the NoteBook.
	 *
	 * @param name prefix for the file name
	 */
	public NoteSheetFileFilter(String name) {
		basename = name;
	}

	/**
	 * Determines whether a file is accepted or not. A file is accepted if it
	 * starts with the correct prefix and matches the regular expression.
	 */
	@Override
	public boolean accept(File arg0, String arg1) {
		String[] nameparts = arg1.split(Pattern.quote(File.separator));
		String fileBasename = nameparts[nameparts.length-1];
		return p.matcher(fileBasename).matches() && fileBasename.startsWith(basename);
	}
}


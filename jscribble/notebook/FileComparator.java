package jscribble.notebook;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {
	private Collator c = Collator.getInstance();

	public int compare(File o1, File o2) {
		if (o1 == o2) {
			return 0;
		}

		File f1 = (File) o1;
		File f2 = (File) o2;

		return c.compare(f1.getName(), f2.getName());
	}
}

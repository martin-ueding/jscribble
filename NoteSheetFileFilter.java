import java.io.File;
import java.io.FilenameFilter;


public class NoteSheetFileFilter implements FilenameFilter {
	
	private String basename;

	public NoteSheetFileFilter(String name) {
		basename = name;
	}

	@Override
	public boolean accept(File arg0, String arg1) {
		if (!arg1.contains(basename)) {
			return false;
		}
		
		return true;
	}

}

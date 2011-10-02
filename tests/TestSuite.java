package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.jscribble.notebook.NoteBookTest;
import tests.jscribble.notebook.NoteSheetTest;
import tests.jscribble.notebook.WriteoutThreadTest;


@RunWith(Suite.class)
@SuiteClasses(
		{
			NoteBookTest.class,
			NoteSheetTest.class,
			WriteoutThreadTest.class,
		}
		)
public class TestSuite {

}

// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package jscribble.selectionWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jscribble.ColonListener;
import jscribble.DrawPanel;
import jscribble.NoteBookProgram;
import jscribble.Redrawer;
import jscribble.notebook.NoteBook;

/**
 * A window and launcher for individual NoteBook. It searches the
 * configuration directory for NoteBook entries and provides a list to open
 * them. A NoteBook can then be opened in a DrawPanel.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class NotebookSelectionWindow {
	/**
	 * The size of newly created NoteBook instances.
	 */
	private Dimension noteSize = new Dimension(1024, 600);


	/**
	 * Button to handle creation of a new NoteBook.
	 */
	private JButton buttonNew;


	/**
	 * Button to handle opening of a NoteBook.
	 */
	private JButton buttonOpen;


	/**
	 * Button to handle deletion of a NoteBook.
	 */
	private JButton buttonDelete;


	/**
	 * Button to enter the scribble mode.
	 */
	private JButton buttonScribble;


	/**
	 * Frame to display everything in.
	 */
	private JFrame frame;


	/**
	 * List that holds all the found NoteBook from the user's configuration
	 * directory.
	 */
	private LinkedList<NoteBook> notebooks;


	/**
	 * List GUI Element to display the NoteBook items in.
	 */
	private JList myList;


	/**
	 * Panel to display the selected NoteBook.
	 */
	private DrawPanel panel;


	private LinkedList<NoteBook> openedNotebooks;


	private DefaultListModel listModel = new DefaultListModel();


	/**
	 * Creates a new window to select NoteBook from. It automatically searches
	 * the user's configuration directory for NoteBook configuration files.
	 */
	public NotebookSelectionWindow() {
		notebooks = findNotebooks();
		openedNotebooks = new LinkedList<NoteBook>();

		myList = new JList(listModel);
		myList.addMouseListener(new ListListener(this));

		buttonNew = new JButton("New");
		buttonNew.addActionListener(new NewActionListener(this));
		buttonOpen = new JButton("Open");
		buttonOpen.addActionListener(new OpenActionListener(this));
		buttonDelete = new JButton("Delete");
		buttonDelete.addActionListener(new DeleteActionListener(this));
		buttonScribble = new JButton("Scribble");
		buttonScribble.addActionListener(new ScribbleActionListener(this));


		GridLayout gl = new GridLayout(1, 4);
		JPanel buttonPanel = new JPanel(gl);
		buttonPanel.add(buttonNew);
		buttonPanel.add(buttonOpen);
		buttonPanel.add(buttonDelete);
		buttonPanel.add(buttonScribble);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(new JScrollPane(myList), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		frame = new JFrame("Select your Notebook");
		frame.setSize(new Dimension(400, 300));
		frame.setLocationRelativeTo(null);
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				for (NoteBook notebook : openedNotebooks) {


					notebook.saveToFiles();
					NoteBookProgram.log(getClass().getName(), String.format("Closing NoteBook \"%s\".", notebook.getName()));
				}
				frame.setVisible(false);
			}
		});


		updateOpenButton();
	}


	/**
	 * Creates a new NoteBook and prompts the user for a name and folder to
	 * save the images in. A config file is automatically created in the config
	 * directory.
	 *
	 * @return the new NoteBook
	 */
	private NoteBook createNewNotebook() {
		String nickname = null;
		Pattern p = Pattern.compile("[A-Za-z0-9-_]+");

		do {
			nickname = JOptionPane.showInputDialog("Nickname of your Notebook: (please only use characters, numbers, _ and -)");

			Matcher m = p.matcher(nickname);
			if (m != null && m.matches()) {
				break;
			}
		}
		while (nickname != null);

		NoteBook nb = new NoteBook(nickname);
		return nb;
	}


	/**
	 * Deletes the currently selected NoteBook in the list.
	 */
	protected void deleteEvent() {
		int selection = myList.getSelectedIndex();

		if (selection >= 0) {
			notebooks.get(selection).delete();

			listModel.remove(selection);
			updateOpenButton();
		}
	}


	/**
	 * Tries to find a configuration directory for this program.
	 *
	 * @return list of NoteBook
	 */
	private LinkedList<NoteBook> findNotebooks() {
		LinkedList<NoteBook> notebooks = new LinkedList<NoteBook>();

		if (NoteBookProgram.getDotDir().exists()) {
			File[] folders = NoteBookProgram.getDotDir().listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File arg0, String arg1) {
					return arg0.isDirectory();
				}

			});

			for (File folder : folders) {
				NoteBook justfound = new NoteBook(folder.getName());
				notebooks.add(justfound);
				listModel.addElement(justfound);
			}
		}
		else {
			NoteBookProgram.getDotDir().mkdirs();
		}

		return notebooks;
	}


	/**
	 * Creates a new NoteBook.
	 */
	protected void newEvent() {
		NoteBook newNoteBook = createNewNotebook();
		if (newNoteBook != null) {
			notebooks.add(newNoteBook);
			listModel.addElement(newNoteBook);
			updateOpenButton();
			openNotebook(newNoteBook);
		}
	}


	/**
	 * Opens the currently selected NoteBook in the list.
	 */
	protected void openEvent() {
		int selection = myList.getSelectedIndex();

		if (selection >= 0) {
			openNotebook(notebooks.get(selection));
		}
	}


	/**
	 * Opens the given NoteBook in a DrawPanel.
	 *
	 * @param notebook NoteBook to open
	 */
	private void openNotebook(final NoteBook notebook) {
		openedNotebooks.add(notebook);

		final JFrame f = new JFrame(String.format("Notebook \"%s\"", notebook.getName()));
		f.setSize(noteSize);
		f.setLocationRelativeTo(null);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				notebook.saveToFiles();
				NoteBookProgram.log(getClass().getName(), String.format("Closing NoteBook \"%s\".", notebook.getName()));
				f.setVisible(false);
			}
		});


		panel = new DrawPanel(notebook);
		f.add(panel);


		f.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {}

			public void keyReleased(KeyEvent ev) {
				if (ev.getKeyChar() == 'j' ||
				        ev.getKeyCode() == KeyEvent.VK_DOWN ||
				        ev.getKeyCode() == KeyEvent.VK_RIGHT ||
				        ev.getKeyCode() == KeyEvent.VK_SPACE ||
				ev.getKeyCode() == KeyEvent.VK_ENTER) {
					notebook.goForward();
				}

				if (ev.getKeyChar() == 'k' ||
				        ev.getKeyCode() == KeyEvent.VK_UP ||
				        ev.getKeyCode() == KeyEvent.VK_LEFT ||
				ev.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					notebook.goBackwards();
				}

				if (ev.getKeyChar() == 'f' ||
				ev.getKeyCode() == KeyEvent.VK_HOME) {
					notebook.gotoFirst();
				}

				if (ev.getKeyChar() == 'l' ||
				ev.getKeyCode() == KeyEvent.VK_END) {
					notebook.gotoLast();
				}
			}

			public void keyTyped(KeyEvent arg0) {}
		});

		notebook.gotoLast();


		ColonListener cl = new ColonListener(panel);
		f.addKeyListener(cl);
		cl.addChangeListener(new Redrawer(panel));


		if (Toolkit.getDefaultToolkit().getScreenSize().equals(noteSize)) {
			GraphicsDevice myDevice = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

			if (myDevice.isFullScreenSupported()) {
				f.setUndecorated(true);
				f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
				myDevice.setFullScreenWindow(f);

				f.setLocation(0, 0);
			}
		}
		f.setVisible(true);
	}


	/**
	 * Starts a scribble mode NoteBook.
	 */
	protected void scribbleEvent() {
		openNotebook(new NoteBook(null));
	}


	/**
	 * Displays the dialogue.
	 */
	public void showDialog() {
		frame.setVisible(true);
	}


	private void updateOpenButton() {
		buttonOpen.setEnabled(notebooks.size() > 0);
	}
}


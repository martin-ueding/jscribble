// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

package jscribble;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	 * Button to delete a NoteBook.
	 *
	 * @author Martin Ueding <dev@martin-ueding.de>
	 */
	@SuppressWarnings("serial")
	private class ButtonDelete extends JButton implements ActionListener {
		ButtonDelete() {
			setText("Delete");

			addActionListener(this);
		}


		/**
		 * Triggers the deletion of a NoteBook.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int selection = myList.getSelectedIndex();

			if (selection >= 0) {
				notebooks.get(selection).delete();
				updateList();
			}
		}
	}


	/**
	 * Button to create a new NoteBook.
	 *
	 * @author Martin Ueding <dev@martin-ueding.de>
	 */
	@SuppressWarnings("serial")
	private class ButtonNew extends JButton implements ActionListener {
		ButtonNew() {
			setText("New");

			addActionListener(this);
		}


		/**
		 * Triggers the creation of a new NoteBook.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			NoteBook newNoteBook = createNewNotebook();
			if (newNoteBook != null) {
				notebooks.add(newNoteBook);
				updateList();
				openNotebook(newNoteBook);
			}
		}
	}


	/**
	 * Button to open a NoteBook.
	 *
	 * @author Martin Ueding <dev@martin-ueding.de>
	 */
	@SuppressWarnings("serial")
	private class ButtonOpen extends JButton implements ActionListener {
		ButtonOpen() {
			setText("Open");
			setEnabled(false);

			addActionListener(this);
		}


		/**
		 * Triggers the opening of a NoteBook, if one was selected.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int selection = myList.getSelectedIndex();

			if (selection >= 0) {
				openNotebook(notebooks.get(selection));
			}
		}
	}


	/**
	 * Button to enter the Scribble mode.
	 *
	 * @author Martin Ueding <dev@martin-ueding.de>
	 */
	@SuppressWarnings("serial")
	private class ButtonScribble extends JButton implements ActionListener {
		public ButtonScribble() {
			setText("Scribble");

			addActionListener(this);
		}


		/**
		 * Triggers the entering into scribble mode with a blank new temporary
		 * NoteBook.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			openNotebook(new NoteBook(null));
		}
	}


	/**
	 * The size of newly created NoteBook instances.
	 */
	private Dimension noteSize = new Dimension(1024, 600);


	/**
	 * Button to handle creation of a new NoteBook.
	 */
	private ButtonNew buttonNew = new ButtonNew();


	/**
	 * Button to handle opening of a NoteBook.
	 */
	private ButtonOpen buttonOpen = new ButtonOpen();


	/**
	 * Button to handle deletion of a NoteBook.
	 */
	private ButtonDelete buttonDelete = new ButtonDelete();


	/**
	 * Button to enter the scribble mode.
	 */
	private ButtonScribble buttonScribble = new ButtonScribble();


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
	 * String representations of the NoteBook items in the LinkedList.
	 */
	private String[] listData;


	/**
	 * List GUI Element to display the NoteBook items in.
	 */
	private JList myList;


	/**
	 * Panel to display the selected NoteBook.
	 */
	private DrawPanel panel;


	/**
	 * Creates a new window to select NoteBook from. It automatically searches
	 * the user's configuration directory for NoteBook configuration files.
	 */
	public NotebookSelectionWindow() {
		notebooks = findNotebooks();

		// TODO open NoteBook when double clicking on the list
		updateList();

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
	}


	/**
	 * Creates a new NoteBook and prompts the user for a name and folder to
	 * save the images in. A config file is automatically created in the config
	 * directory.
	 *
	 * @return the new NoteBook
	 */
	private NoteBook createNewNotebook() {
		String nickname = JOptionPane.showInputDialog("Nickname of your Notebook:");

		if (nickname == null) {
			return null;
		}

		// TODO clean up name for use as file name

		NoteBook nb = new NoteBook(nickname);

		return nb;
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
				notebooks.add(new NoteBook(folder.getName()));
			}
		}
		else {
			NoteBookProgram.getDotDir().mkdirs();
		}

		return notebooks;
	}


	/**
	 * Opens the given NoteBook in a DrawPanel.
	 *
	 * @param notebook NoteBook to open
	 */
	private void openNotebook(final NoteBook notebook) {
		final JFrame f = new JFrame(String.format("Notebook \"%s\"", notebook.getName()));
		f.setSize(noteSize);
		f.setLocationRelativeTo(null);

		f.addWindowListener(new java.awt.event.WindowAdapter() {
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
	 * Displays the dialogue.
	 */
	public void showDialog() {
		frame.setVisible(true);
	}


	/**
	 * Updates the list with NoteBook.
	 */
	private void updateList() {
		// FIXME really update the list if it is changed
		listData = new String[notebooks.size()];
		int j = 0;
		for (Iterator<NoteBook> iterator = notebooks.iterator(); iterator.hasNext(); j++) {
			NoteBook noteBook = (NoteBook) iterator.next();
			listData[j] = noteBook.toString();
		}


		myList = new JList(listData);
		if (myList != null && myList.isShowing()) {
			myList.repaint();
		}

		buttonOpen.setEnabled(notebooks.size() > 0);
	}
}


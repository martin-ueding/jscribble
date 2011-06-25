// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A window and launcher for individual NoteBook. It searches the
 * configuration directory for NoteBook entries and provides a list to open
 * them. A NoteBook can then be opened in a DrawPanel.
 * 
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class NotebookSelectionWindow {
	private static final Dimension noteSize = new Dimension(1024, 600);

	private ButtonNew buttonNew = new ButtonNew();
	private ButtonOpen buttonOpen = new ButtonOpen();
	private ButtonScribble buttonScribble = new ButtonScribble();


	private JFrame frame;

	private LinkedList<NoteBook> notebooks;
	private String[] listData;

	private JList myList;

	private File configdir = new File(System.getProperty("user.home") + File.separator + ".jscribble");

	private String configFileSuffix = ".jscribble-notebook";

	private DrawPanel panel;

	public NotebookSelectionWindow() {
		notebooks = findNotebooks();

		updateList();

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		buttonPanel.add(buttonNew);
		buttonPanel.add(buttonOpen);
		buttonPanel.add(buttonScribble);
		// TODO add delete button

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(new JScrollPane(myList), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		frame = new JFrame("Select your Notebook");
		frame.setSize(new Dimension(300, 400));
		frame.setLocationRelativeTo(null);
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	/**
	 * Displays the dialogue.
	 */
	public void showDialogue() {
		frame.setVisible(true);
	}


	/**
	 * Tries to find a configuration directory for this program.
	 * 
	 * @return list of NoteBook
	 */
	private LinkedList<NoteBook> findNotebooks() {
		LinkedList<NoteBook> notebooks = new LinkedList<NoteBook>();

		if (configdir.exists()) {
			File[] configfiles = configdir.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File arg0, String arg1) {
					return arg1.contains(configFileSuffix);
				}

			});

			for (File configfile : configfiles) {
				Properties p = new Properties();
				try {
					p.loadFromXML(new FileInputStream(configfile));

					Dimension noteSize = new Dimension(Integer.parseInt(p.getProperty("width")), Integer.parseInt(p.getProperty("height")));
					File folder = new File(p.getProperty("folder"));
					String name = p.getProperty("name");

					NoteBook nb = new NoteBook(noteSize, folder, name);
					notebooks.add(nb);
				}
				catch (InvalidPropertiesFormatException e) {
					NoteBookProgram.handleError("The NoteBook config file is malformed.");
					e.printStackTrace();
				}
				catch (FileNotFoundException e) {
					NoteBookProgram.handleError("The NoteBook config file could not be found.");
					e.printStackTrace();
				}
				catch (IOException e) {
					NoteBookProgram.handleError("IO during NoteBook config file loading.");
					e.printStackTrace();
				}
			}
		}
		else {
			configdir.mkdirs();
		}

		return notebooks;
	}


	/**
	 * Creates a new NoteBook and prompts the user for a name and folder to
	 * save the images in. A config file is automatically created in the config
	 * dir.
	 * 
	 * @return the new NoteBook
	 */
	private NoteBook createNewNotebook() {
		String nickname = JOptionPane.showInputDialog("Nickname of your Notebook:");

		if (nickname == null) {
			return null;
		}

		File in = null;

		JFileChooser loadChooser = new JFileChooser();
		loadChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);


		int result = loadChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			in = loadChooser.getSelectedFile();
		}

		// if there is no file selected, abort right here
		if (in == null) {
			return null;
		}

		// persist this NoteBook in the config file
		Properties p = new Properties();
		p.setProperty("width", String.valueOf(noteSize.width));
		p.setProperty("height", String.valueOf(noteSize.height));
		try {
			p.setProperty("folder", in.getCanonicalPath());
		}
		catch (IOException e) {
			NoteBookProgram.handleError("IO error while retrieving the path of the image folder.");
			e.printStackTrace();
		}
		p.setProperty("name", nickname);

		try {
			p.storeToXML(new FileOutputStream(new File(configdir.getCanonicalPath() + File.separator + nickname + configFileSuffix)), "generated by jscribble " + VersionName.version);
		}
		catch (FileNotFoundException e) {
			NoteBookProgram.handleError("Could not find NoteBook config file for writing.");
			e.printStackTrace();
		}
		catch (IOException e) {
			NoteBookProgram.handleError("IO error while writing NoteBook config file.");
			e.printStackTrace();
		}


		return new NoteBook(noteSize, in, nickname);
	}


	/**
	 * Updates the list with NoteBook.
	 */
	private void updateList() {
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


	/**
	 * Opens the given NoteBook in a DrawPanel.
	 * 
	 * @param notebook NoteBook to open
	 */
	public void openNotebook(final NoteBook notebook) {
		JFrame f = new JFrame(String.format("Notebook \"%s\"", notebook.getName()));
		f.setSize(noteSize);
		f.setLocationRelativeTo(null);

		f.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				notebook.saveToFiles();
				System.exit(0);
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

				if (ev.getKeyChar() == 'h' ||
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

		@Override
		public void actionPerformed(ActionEvent arg0) {
			openNotebook(new NoteBook(noteSize, null, null));
		}
	}
}


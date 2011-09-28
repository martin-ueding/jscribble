// Copyright © 2011 Martin Ueding <dev@martin-ueding.de>

/*
 * This file is part of jscribble.
 *
 * jscribble is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 2 of the License, or (at your option)
 * any later version.
 *
 * jscribble is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * jscribble.  If not, see <http://www.gnu.org/licenses/>.
 */

package jscribble.selectionWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jscribble.NoteBookProgram;
import jscribble.drawPanel.CommandListener;
import jscribble.drawPanel.DrawPanel;
import jscribble.drawPanel.MovementListener;
import jscribble.drawPanel.NoteBookClosingAdapter;
import jscribble.drawPanel.Redrawer;
import jscribble.helpers.Localizer;
import jscribble.helpers.SettingsWrapper;
import jscribble.notebook.NoteBook;

/**
 * A window and launcher for individual NoteBook. It searches the
 * configuration directory for NoteBook entries and provides a list to open
 * them. A NoteBook can then be opened in a DrawPanel.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class NoteBookSelectionWindow {
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

	/**
	 * Model for the NoteBook list.
	 */
	private DefaultListModel listModel = new DefaultListModel();

	private ShutdownHook shutdownHook;

	/**
	 * Creates a new window to select NoteBook from. It automatically searches
	 * the user's configuration directory for NoteBook configuration files.
	 */
	public NoteBookSelectionWindow() {
		notebooks = findNotebooks();

		myList = new JList(listModel);
		myList.addMouseListener(new ListListener(this));

		buttonNew = new JButton(Localizer.get("New"));
		buttonNew.addActionListener(new NewActionListener(this));
		buttonOpen = new JButton(Localizer.get("Open"));
		buttonOpen.addActionListener(new OpenActionListener(this));
		buttonDelete = new JButton(Localizer.get("Delete"));
		buttonDelete.addActionListener(new DeleteActionListener(this));
		buttonScribble = new JButton(Localizer.get("Scribble"));
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


		frame = new JFrame(Localizer.get("Select your Notebook"));
		frame.setSize(new Dimension(
		            SettingsWrapper.getInteger("notebook_selection_window_width"),
		            SettingsWrapper.getInteger("notebook_selection_window_height")
		        ));
		frame.setLocationRelativeTo(null);
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			frame.setIconImage(ImageIO.read(
			            getClass().getResourceAsStream(
			                    "/artwork/jscribble_gray.png")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}



		shutdownHook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);


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
		Pattern p = Pattern.compile(SettingsWrapper.getString("notebook_name_validation_pattern"));

		do {
			nickname = JOptionPane.showInputDialog(Localizer.get(
			        "Nickname of your Notebook:" + " " +
			        "(please only use characters, numbers, _ and -)"));

			if (nickname == null) {
				return null;
			}

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
			boolean wasdeleted = notebooks.get(selection).delete();

			if (wasdeleted) {
				listModel.remove(selection);
				updateOpenButton();
			}
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
			File[] folders = NoteBookProgram.getDotDir().listFiles();

			for (File folder : folders) {
				if (folder.isDirectory()) {
					NoteBook justfound = new NoteBook(folder.getName());
					notebooks.add(justfound);
				}
			}
			Collections.sort(notebooks);
			for (NoteBook notebook : notebooks) {
				listModel.addElement(notebook);

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
		shutdownHook.add(notebook);

		final JFrame f = new JFrame(String.format(Localizer.get(
		            "Notebook \"%s\""), notebook.getName()));
		f.setSize(notebook.getSize());
		f.setLocationRelativeTo(null);

		f.addWindowListener(new NoteBookClosingAdapter(notebook, f));
		f.setResizable(false);

		try {
			f.setIconImage(ImageIO.read(
			            getClass().getResourceAsStream(
			                    "/install_files/jscribble.png")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		panel = new DrawPanel(notebook, f);
		f.add(panel);

		f.addKeyListener(new MovementListener(panel));

		notebook.gotoLast();

		CommandListener cl = new CommandListener(panel);
		f.addKeyListener(cl);
		cl.addChangeListener(new Redrawer(panel));


		if (Toolkit.getDefaultToolkit().getScreenSize().equals(notebook.getSize())) {
			GraphicsDevice myDevice = java.awt.GraphicsEnvironment.
			        getLocalGraphicsEnvironment().getDefaultScreenDevice();

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

	/**
	 * Updates the state of the "open" button. This is needed when the last
	 * NoteBook is deleted or the first one created.
	 */
	private void updateOpenButton() {
		buttonOpen.setEnabled(notebooks.size() > 0);
	}
}

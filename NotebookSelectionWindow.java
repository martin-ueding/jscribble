// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;


public class NotebookSelectionWindow {

	@SuppressWarnings("serial")
	private class ButtonScribble extends JButton {
		public ButtonScribble() {
			setText("Scribble");
		}
	}

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
			
			if (selection > 0) {
				NoteBookProgram.openNotebook(notebooks.get(selection));
			}
			else {
				Logger.getLogger(this.getClass().getName()).info("nothing selected");
			}
		}
	}

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
			}

		}
	}

	private void updateList() {
		listData = new String[notebooks.size()];
		int j = 0;
		for (Iterator<NoteBook> iterator = notebooks.iterator(); iterator.hasNext(); j++) {
			NoteBook noteBook = (NoteBook) iterator.next();
			listData[j] = noteBook.toString();
			
			myList = new JList(listData);
			
			if (myList != null)
				myList.repaint();
		}
		
		buttonOpen.setEnabled(notebooks.size() > 0);
	}

	private ButtonNew buttonNew = new ButtonNew();
	private ButtonOpen buttonOpen = new ButtonOpen();
	private ButtonScribble buttonScribble = new ButtonScribble();
	

	private JFrame frame;

	private int width, height;

	private LinkedList<NoteBook> notebooks;
	private String[] listData;
	
	private JList myList;

	public NotebookSelectionWindow(int width, int height) {
		this.width = width;
		this.height = height;

		notebooks = findNotebooks();


		updateList();


		// The automatically created model is stored in JList's "model"
		// property, which you can retrieve
		ListModel model = myList.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			System.out.println(model.getElementAt(i));
		}


		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		buttonPanel.add(buttonNew);
		buttonPanel.add(buttonOpen);
		buttonPanel.add(buttonScribble);
		// TODO add delete button

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(myList, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		frame = new JFrame("Select your Notebook");
		frame.setSize(new Dimension(300, 400));
		frame.setVisible(true);
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	

	File configdir = new File(System.getProperty("user.home")+File.separator+".jscribble");
	
	String configFileSuffix = ".jscribble-notebook";

	/**
	 * Tries to find a configuration directory for this program.
	 * @return list of notebooks
	 */
	private LinkedList<NoteBook> findNotebooks() {
		LinkedList<NoteBook> notebooks = new LinkedList<NoteBook>();
		
		

		if (configdir.exists()) {
			System.out.println("found a config dir");
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

					int width = Integer.parseInt(p.getProperty("width"));
					int height = Integer.parseInt(p.getProperty("height"));
					File folder = new File(p.getProperty("folder"));
					String name = p.getProperty("name");

					NoteBook nb = new NoteBook(width, height, folder, name);
					notebooks.add(nb);
				}
				catch (InvalidPropertiesFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			try {
				System.out.println("creating config dir in "+configdir.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			configdir.mkdirs();
		}

		return notebooks;
	}

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
		
		// persist this notebook in the config file
		Properties p = new Properties();
		p.setProperty("width", String.valueOf(width));
		p.setProperty("height", String.valueOf(height));
		try {
			p.setProperty("folder", in.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.setProperty("name", nickname);
		
		try {
			p.storeToXML(new FileOutputStream(new File(configdir.getCanonicalPath()+File.separator+nickname+configFileSuffix)), "generated by jscribble "+VersionName.version);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return new NoteBook(width, height, in, nickname);
	}

}

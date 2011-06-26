package jscribble;
// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

/**
 * Listens to the keyboard for the command key and then polls the user for a
 * command. The command is then executed.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class ColonListener implements KeyListener {

	/**
	 * The DrawPanel that is displaying the NoteBook.
	 */
	private DrawPanel panel;

	/**
	 * The listener that is to be notified when something is changed.
	 */
	private Redrawer redrawer;

	/**
	 * Creates a new listener that listens to the given panel
	 *
	 * @param malPanel panel to listen to
	 */
	public ColonListener(DrawPanel malPanel) {
		panel = malPanel;
	}

	/**
	 * A listener to be called when a setting causes the panel to need
	 * refreshing.
	 *
	 * @param redrawer listener to call after setting change
	 */
	public void addChangeListener(Redrawer redrawer) {
		this.redrawer = redrawer;

	}


	/**
	 * Ignored.
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
	}


	/**
	 * Ignored.
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			panel.setShowHelp(false);
			redrawer.actionPerformed(null);
		}
	}


	/**
	 * Listens for the ':' character and polls the user for a command.
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		if (arg0.getKeyChar() == ':') {
			String command = JOptionPane.showInputDialog("Command:");

			if (command.equals("lines")) {
				panel.setLines(true);
			}
			if (command.equals("nolines")) {
				panel.setLines(false);
			}

			redrawer.actionPerformed(null);
		}

		if (arg0.getKeyChar() == 'h') {
			panel.toggleHelp();
			redrawer.actionPerformed(null);
		}
	}
}

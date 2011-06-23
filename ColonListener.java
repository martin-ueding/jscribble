// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

/**
 * Listens to the keyboard for the command key and then polls the user for a
 * command. The command is then executed.
 */
public class ColonListener implements KeyListener {

	private DrawPanel panel;
	private Redrawer redrawer;

	/**
	 * Creates a new listener that listens to the given panel
	 * @param malPanel panel to listen to
	 */
	public ColonListener(DrawPanel malPanel) {
		panel = malPanel;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

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

	}

	/**
	 * A listener to be called when a setting causes the panel to need
	 * refreshing.
	 * @param redrawer listener to call after setting change
	 */
	public void addChangeListener(Redrawer redrawer) {
		this.redrawer = redrawer;

	}

}

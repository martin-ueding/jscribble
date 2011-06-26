package jscribble;
// Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;


/**
 * Listens to changes from the NoteBook and updates the panel.
 *
 * @author Martin Ueding <dev@martin-ueding.de>
 */
public class Redrawer implements ActionListener {

	/**
	 * Panel which is to be redrawn.
	 */
	private JPanel panel;


	/**
	 * Creates a new Redrawer that tells the given Panel to redraw.
	 *
	 * @param panel panel to redraw when notified
	 */
	public Redrawer(JPanel panel) {
		this.panel = panel;
	}


	/**
	 * Redraws the previously set panel.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		panel.repaint();
	}
}

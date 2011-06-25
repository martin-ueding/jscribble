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

	private JPanel panel;

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

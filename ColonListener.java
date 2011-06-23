import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;


public class ColonListener implements KeyListener {

	private MalPanel panel;
	private Redrawer redrawer;

	public ColonListener(MalPanel malPanel) {
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

	public void addChangeListener(Redrawer redrawer) {
		this.redrawer = redrawer;

	}

}

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class MalPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageObserver io = this;
	protected void paintComponent (Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
		
		Graphics2D g2 = (Graphics2D)g;

		g.drawImage(Notizbuch.bg, 0, 0, io);
		
        int rule = AlphaComposite.SRC_OVER;
        AlphaComposite ac = AlphaComposite.getInstance(rule, 0.5f);
        g2.setComposite(ac);
        

		g2.drawImage(Notizbuch.img, 0, 0, io);
//		g.setColor(Color.BLUE);
//		g.drawString(Spr.get("speichern")+": [S]", 10, 25);
//		g.drawString(Spr.get("speichern")+" ("+Notizbuch.autoName+"): [A]", 10, 45);
		
	}
}

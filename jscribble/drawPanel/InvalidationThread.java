// Copyright © 2011 Martin Ueding <martin-ueding.de>

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

package jscribble.drawPanel;

import jscribble.helpers.Config;

/**
 * Waits until no more events come in and repaints the DrawPanel.
 *
 * @author Martin Ueding <martin-ueding.de>
 */
public class InvalidationThread extends Thread {
    /**
     * Whether the Thread should wait another turn.
     */
    boolean keepAlive = true;

    /**
     * The DrawPanel to repaint.
     */
    private DrawPanel drawPanel;

    /**
     * Creates a new Thread that repaints the DrawPanel after a no more events.
     *
     * @param drawPanel Panel to redraw.
     */
    public InvalidationThread(DrawPanel drawPanel) {
        super();
        this.drawPanel = drawPanel;
    }

    /**
     * Tells the Thread that another event came in and that the picture is not to be repainted yet.
     */
    public void keepAlive() {
        keepAlive = true;
    }

    /**
     * Waits until no more events come in and repaints the DrawPanel.
     */
    public void run() {
        while (keepAlive) {
            keepAlive = false;
            try {
                sleep(Config.getInteger("notebook_erase_timeout"));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        drawPanel.resetCachedImage();
        drawPanel.repaint();
    }
}

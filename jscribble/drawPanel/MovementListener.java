// Copyright © 2011 Martin Ueding <mu@martin-ueding.de>

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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import jscribble.helpers.Config;

/**
 * Handles movement commands from the keyboard.
 *
 * @author Martin Ueding <mu@martin-ueding.de>
 */
public class MovementListener implements KeyListener {
    /**
     * The DrawPanel that displays the current NoteBook.
     */
    private final DrawPanel drawPanel;

    /**
     * Creates a MovementListener that relays to the given DrawPanel.
     *
     * @param drawPanel Notify this.
     */
    public MovementListener(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
    }

    /**
     * Determines whether a given KeyEvent should fire going back a page.
     *
     * @param event KeyEvent to process
     * @return Whether this should fire going back a page.
     */
    private boolean isKeyGoBack(KeyEvent event) {
        return Config.isKeyForCommand(event, "notebook_go_back_key");
    }

    /**
     * Determines whether a given KeyEvent should fire going forward a page.
     *
     * @param event KeyEvent to process
     * @return Whether this should fire going forward a page.
     */
    private boolean isKeyGoForward(KeyEvent event) {
        return Config.isKeyForCommand(event, "notebook_go_forward_key");
    }

    /**
     * Determines whether a given KeyEvent should fire going to the first page.
     *
     * @param event KeyEvent to process
     * @return Whether this should fire going to the first page.
     */
    private boolean isKeyGotoFirst(KeyEvent event) {
        return Config.isKeyForCommand(event, "notebook_goto_first_key");
    }

    /**
     * Determines whether a given KeyEvent should fire going to the last page.
     *
     * @param event KeyEvent to process
     * @return Whether this should fire going to the last page.
     */
    private boolean isKeyGotoLast(KeyEvent event) {
        return Config.isKeyForCommand(event, "notebook_goto_last_key");
    }

    /**
     * Ignored.
     */
    public void keyPressed(KeyEvent event) {}

    /**
     * Handles various keys.
     */
    public void keyReleased(KeyEvent event) {
        if (isKeyGoForward(event)) {
            drawPanel.goForward();
        }

        if (isKeyGoBack(event)) {
            drawPanel.goBackwards();
        }

        if (isKeyGotoFirst(event)) {
            drawPanel.gotoFirst();
        }

        if (isKeyGotoLast(event)) {
            drawPanel.gotoLast();
        }
    }

    /**
     * Ignored.
     */
    public void keyTyped(KeyEvent event) {}
}

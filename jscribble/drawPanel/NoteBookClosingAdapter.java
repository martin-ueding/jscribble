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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import jscribble.helpers.Localizer;
import jscribble.helpers.Logger;
import jscribble.notebook.NoteBook;

/**
 * Closes a given NoteBook when its DrawPanel is closed.
 *
 * @author Martin Ueding <martin-ueding.de>
 */
public class NoteBookClosingAdapter extends WindowAdapter {
    private final NoteBook notebook;
    private final JFrame f;

    /**
     * Creates a NoteBookClosingAdapter that closes a given NoteBook and its window.
     *
     * @param notebook NoteBook to close.
     * @param f Window of the NoteBook to close.
     */
    public NoteBookClosingAdapter(NoteBook notebook, JFrame f) {
        this.notebook = notebook;
        this.f = f;
    }

    /**
     * Closes all the NoteBook instances.
     */
    public void windowClosing(WindowEvent winEvt) {
        notebook.saveToFiles();
        Logger.log(getClass().getName(),
                   String.format(Localizer.get("Closing NoteBook \"%s\"."),
                           notebook.getName()));
        f.setVisible(false);
    }
}

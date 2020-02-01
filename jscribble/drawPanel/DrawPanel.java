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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jscribble.VersionName;
import jscribble.helpers.Localizer;
import jscribble.helpers.Logger;
import jscribble.helpers.Config;
import jscribble.notebook.BufferedImageWrapper;
import jscribble.notebook.NoteBook;

/**
 * Displays the current page of a NoteBook. It also listens to the mouse and
 * relays the movements to the NoteBook as line drawing commands. It also
 * features a command listener for more user interaction.
 *
 * @author Martin Ueding <martin-ueding.de>
 */
@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
    /**
     * Color of the help lines.
     */
    private static final Color lineColor = Config.getColor("ruling_line_color");

    /**
     * The spacing between the help lines.
     */
    private static final int lineSpacing = Config.getInteger("ruling_line_spacing");

    /**
     * The NoteBook that is currently displayed.
     */
    private NoteBook notebook;

    /**
     * Handles the image output.
     */
    private ImageObserver io = this;

    /**
     * Whether to display the help panel.
     */
    private boolean showHelp = false;

    /**
     * A list with all the HelpItem to display.
     */
    private HelpItem[] helpItems = {
        new HelpItem(
            Config.getString("help_screen_toggle_key"),
            Localizer.get("show help")
        ),
        new HelpItem(
            Config.getString("help_screen_close_key"),
            Localizer.get("hide help")
        ),
        new HelpItem(
            Config.getString("notebook_go_forward_key"),
            Localizer.get("go forward")
        ),
        new HelpItem(
            Config.getString("notebook_go_back_key"),
            Localizer.get("go backward")),
        new HelpItem(
            Config.getString("notebook_goto_first_key"),
            Localizer.get("goto first")
        ),
        new HelpItem(
            Config.getString("notebook_goto_last_key"),
            Localizer.get("goto last")
        ),
        new HelpItem(
            Config.getString("notebook_close_key"),
            Localizer.get("save & exit")
        ),
        new HelpItem(
            Config.getString("onion_layer_increase_key"),
            Localizer.get("increase onion layers")
        ),
        new HelpItem(
            Config.getString("onion_layer_decrease_key"),
            Localizer.get("decrease onion layers")
        ),
        new HelpItem(
            Config.getString("ruling_toggle_key"),
            Localizer.get("toggle ruling")
        ),
        new HelpItem(
            Config.getString("ruling_graph_toggle_key"),
            Localizer.get("toggle graph paper")
        ),
    };

    /**
     * How many images should be composed in a see through way.
     */
    private int onionMode;

    /**
     * A cached image that is used instead of the original images in the onion
     * mode to conserve performance. Use it via the getter.
     */
    private BufferedImage cachedImage;

    /**
     * The wrapper for the current image. Use it via the getter.
     */
    private BufferedImageWrapper imageWrapper;

    /**
     * Whether the help splash screen is (still) displayed.
     */
    private boolean showHelpSplash = Config.getBoolean("help_splash_enable");

    /**
     * Which type of ruling is used.
     */
    private RulingType ruling = RulingType.NONE;

    /**
     * Runtime to obtain memory usage.
     */
    private Runtime r;

    /**
     * Thread that handles redrawing after the erasing stops.
     */
    private InvalidationThread invalidationThread;

    /**
     * Parent frame that needs to be closed when the user hits the close button.
     */
    private JFrame parent;

    /**
     * Creates a new display panel that will listen to changes from a specific
     * NoteBook.
     *
     * @param notebook The NoteBook to display.
     * @param parent Window to close when NoteBook is closed.
     */
    public DrawPanel(NoteBook notebook, JFrame parent) {
        this.notebook = notebook;
        this.parent = parent;

        // Notify this instance when the notebook was is done drawing.
        notebook.setDoneDrawing(new Redrawer(this));

        new PaintListener(this);

        r = Runtime.getRuntime();
    }

    /**
     * Draws the help screen if needed.
     *
     * @param g Graphics2D to draw in
     */
    private void drawHelp(Graphics2D g) {
        if (!showHelp) {
            return;
        }

        // Draw a dark rectangle to write the help text on.
        g.setColor(Config.getColor("help_screen_background_color"));
        int helpMargin = Config.getInteger("help_screen_margin");
        int helpBorderRadius = Config.getInteger("help_screen_border_radius");
        g.fillRoundRect(
            helpMargin,
            helpMargin,
            getWidth() - helpMargin * 2,
            getHeight() - helpMargin * 2,
            helpBorderRadius,
            helpBorderRadius);
        g.setColor(Color.WHITE);

        // Iterate through the help items and display them.
        int i = 0;
        int vspacing = Config.getInteger("help_screen_vspacing");
        int spacing = Config.getInteger("help_screen_spacing");
        int padding = Config.getInteger("help_screen_padding");
        for (HelpItem h : helpItems) {
            g.drawString(h.helptext, padding, i * vspacing + padding);
            g.drawString(h.key, spacing + padding, i * vspacing +
                    padding);
            i++;
        }

        // Print the version identifier.
        g.setColor(Color.GRAY);
        g.drawString(String.format(Localizer.get("Version %s"),
                VersionName.version), padding, getHeight() - padding);
    }

    /**
     * Draws a help splash screen at the beginning.
     *
     * @param g Graphics2D to draw in
     */
    private void drawHelpSplash(Graphics2D g) {
        if (!showHelpSplash) {
            return;
        }

        // Draw a dark rectangle to write the help text on.
        g.setColor(Config.getColor("help_splash_background_color"));
        Dimension splashSize = new Dimension(
            getWidth() - Config.getInteger("help_splash_margin") * 2,
            Config.getInteger("help_splash_height"));
        int helpSplashBorderRadius = Config.getInteger("help_splash_border_radius");
        g.fillRoundRect(
            (getWidth() - splashSize.width) / 2,
            (getHeight() - splashSize.height) / 2,
            splashSize.width, splashSize.height,
            helpSplashBorderRadius,
            helpSplashBorderRadius);
        g.setColor(Color.WHITE);

        g.drawString(Localizer.get("Press h or F1 to get help."),
                (getWidth() - splashSize.width) / 2 + Config.getInteger("help_splash_margin"),
                getHeight() / 2 + 5);
    }

    /**
     * Draws a line onto the current sheet. If onion mode is used, it will be
     * cached in another image. To the user, there will be no difference.
     *
     * @param line Line to draw.
     */
    public void drawLine(Line2D line) {
        if (hasCachedImage()) {
            getImageWrapper().drawLine(line);
        }
        notebook.drawLine(line);

        showHelpSplash = false;
    }

    /**
     * Draws the helping lines if needed.
     *
     * @param g Graphics2D to draw on
     */
    private void drawLines(Graphics2D g) {
        if (ruling != RulingType.NONE) {
            g.setColor(lineColor);

            // Vertical lines.
            if (ruling == RulingType.GRAPH) {
                for (int i = lineSpacing; i < getWidth(); i += lineSpacing) {
                    g.drawLine(i, 0, i, getHeight());
                }
            }

            // Horizontal lines.
            for (int i = lineSpacing; i < getHeight(); i += lineSpacing) {
                g.drawLine(0, i, getWidth(), i);
            }
        }
    }

    /**
     * Draws the memory usage onto the screen.
     *
     * @param g Graphics2D to draw on
     */
    private void drawMemoryUsage(Graphics2D g) {
        if (!Config.getBoolean("memory_usage_show")) {
            return;
        }

        g.drawString(
            String.format(
                Localizer.get("%d MiB used, %d MiB free, %d MiB total"),
                (r.totalMemory() - r.freeMemory()) / 1024 / 1024,
                r.freeMemory() / 1024 / 1024,
                r.totalMemory() / 1024 / 1024
            ),
            Config.getInteger("memory_usage_position_left"),
            getHeight() - Config.getInteger("memory_usage_position_bottom")
        );
    }

    /**
     * Draws the number of onion layers as a string.
     *
     * @param g Graphics2D to draw on
     */
    private void drawOnionInfo(Graphics2D g) {
        if (!isOnionMode()) {
            return;
        }

        g.drawString(
            String.format(Localizer.get("Onion Layers: %d"), onionMode),
            Config.getInteger("onion_info_position_left"),
            Config.getInteger("onion_info_position_top")
        );
    }

    /**
     * Draws the page number on top.
     *
     * @param g Graphics2D to draw on
     */
    private void drawPageNumber(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.drawString(String.format(Localizer.get("Page %d/%d"),
                notebook.getCurrentSheet().getPagenumber(),
                notebook.getSheetCount()),
                getWidth() / 2,
                Config.getInteger("page_number_position_top"));
    }

    /**
     * Draws the scroll panels at the side of the screen if enabled in the
     * config.
     *
     * @param g Context of the current DrawPanel
     */
    private void drawScrollPanels(Graphics2D g) {
        // Do nothing if the option is not set.
        if (!Config.getBoolean("scroll_panels_show")) {
            return;
        }

        try {
            // Read the dimension of the panel from the config file.
            int scrollPanelRadius = Config.getInteger("scroll_panel_width");
            int scrollPanelPadding = Config.getInteger("scroll_panel_padding");

            // Draw the panels on the sides.
            g.setColor(Config.getColor("scroll_panel_color"));
            g.fillRoundRect(-scrollPanelRadius, scrollPanelPadding,
                    2 * scrollPanelRadius,
                    getHeight() - 2 * scrollPanelPadding,
                    scrollPanelRadius, scrollPanelRadius);
            g.fillRoundRect(getWidth() - scrollPanelRadius,
                    scrollPanelPadding, 2 * scrollPanelRadius,
                    getHeight() - 2 * scrollPanelPadding,
                    scrollPanelRadius, scrollPanelRadius);
        }
        catch (NumberFormatException e) {
            Logger.handleError(Localizer.get(
                        "Malformed entry in config file."));
        }
    }

    /**
     * Erases a line on the NoteBook. If a cachedImage is used, the line is
     * erased on that image as well.
     *
     * @param line Line to erase.
     */
    public void eraseLine(Line2D line) {
        if (hasCachedImage()) {
            getImageWrapper().eraseLine(line);

            /*
             * The user might have erased some of the underlying ruling or
             * onion layers. Refreshing the picture with all its layers takes
             * too much time to do with each repaint. Schedule the picture for
             * expiration if that did not happen yet. If it is scheduled
             * already, keep it alive for a little longer.
             */
            getInvalidationThread().keepAlive();
        }

        notebook.eraseLine(line);

        showHelpSplash = false;
    }

    /**
     * Returns the cached image. This can be the original image if there is no
     * onion mode used, or the layered image if used. If there is no image yet,
     * it will be created and composed.
     *
     * @return Image which contains all the drawing information
     */
    private BufferedImage getCachedImage() {
        // If the onion mode is not enabled, the original image can be used.
        if (!isOnionMode() && ruling == RulingType.NONE) {
            return notebook.getCurrentSheet().getImg();
        }

        if (cachedImage == null) {
            // Create a new blank image.
            cachedImage = new BufferedImage(getWidth(), getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g2 = (Graphics2D) cachedImage.getGraphics();
            g2.setColor(Config.getColor("notebook_background_color"));
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Go back as many pages as there should be onion layers.
            int wentBack = 0;
            for (; wentBack < onionMode; wentBack++) {
                int prevPageNumber = notebook.getCurrentSheet()
                        .getPagenumber();
                notebook.goBackwards();
                if (prevPageNumber == notebook.getCurrentSheet()
                        .getPagenumber()) {
                    break;
                }
            }

            // Set the layers to a given opacity.
            g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_ATOP,
                        (float)(Config.getDouble("onion_mode_opacity") / Math.max(onionMode, 1))));

            // Iterate through from the bottom to the top layer and compose
            // the images onto the cache image.
            while (wentBack > 0) {
                g2.drawImage(notebook.getCurrentSheet().getImg(), 0, 0, io);

                // Move on to the next NoteSheet.
                wentBack--;
                notebook.goForward();
            }

            drawLines(g2);

            g2.drawImage(notebook.getCurrentSheet().getImg(), 0, 0, io);
        }

        return cachedImage;
    }

    /**
     * Retrieves and initializes the BufferedImageWrapper for the current cached
     * image.
     *
     * @return BufferedImageWrapper of cachedImage
     */
    private BufferedImageWrapper getImageWrapper() {
        if (imageWrapper == null) {
            imageWrapper = new BufferedImageWrapper(cachedImage);
        }

        return imageWrapper;
    }

    /**
     * Returns an initializes the InvalidationThread.
     * @return Initialized InvalidationThread.
     */
    private InvalidationThread getInvalidationThread() {
        if (invalidationThread == null || !invalidationThread.isAlive()) {
            invalidationThread = new InvalidationThread(this);
            invalidationThread.start();
        }
        return invalidationThread;
    }

    /**
     * Goes one page back.
     */
    public void goBackwards() {
        resetCachedImage();
        notebook.goBackwards();
    }

    /**
     * Goes one page forward.
     */
    public void goForward() {
        resetCachedImage();
        notebook.goForward();
    }

    /**
     * Goes to the first page.
     */
    public void gotoFirst() {
        resetCachedImage();
        notebook.gotoFirst();
    }

    /**
     * Goes to the last page.
     */
    public void gotoLast() {
        resetCachedImage();
        notebook.gotoLast();
    }

    /**
     * Determines whether a cached image is currently used.
     *
     * @return Whether a cached image is used.
     */
    private boolean hasCachedImage() {
        return cachedImage != null;
    }

    /**
     * Whether there are any onion layers displayed.
     *
     * @return Whether there are onion layers.
     */
    private boolean isOnionMode() {
        return onionMode > 0;
    }

    /**
     * Decreases the onion layers and does additional housekeeping.
     */
    public void onionLayersDecrease() {
        // Do nothing if there are no layers displayed currently.
        if (!isOnionMode()) {
            return;
        }

        resetCachedImage();
        onionMode--;
        repaint();
    }

    /**
     * Increases the onion layers and does additional housekeeping.
     */
    public void onionLayersIncrease() {
        resetCachedImage();

        onionMode++;
        repaint();
    }

    /**
     * Draws the NoteSheet and page number. If lines are on, they are drawn on
     * top of the image as well.
     *
     * @param graphics graphics context (usually given by Java itself).
     */
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;
        g.setRenderingHints(new
                RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON));


        // Draw the current image.
        g.drawImage(getCachedImage(), 0, 0, io);

        drawPageNumber(g);
        drawOnionInfo(g);
        drawScrollPanels(g);
        drawHelp(g);
        drawHelpSplash(g);
        drawMemoryUsage(g);
    }

    /**
     * Resets the cached image after changing pages for instance.
     */
    protected void resetCachedImage() {
        cachedImage = null;
        imageWrapper = null;
        showHelpSplash = false;
    }

    /**
     * Sets whether the help dialog is displayed.
     */
    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
    }

    /**
     * Saves the NoteBook and closes the window.
     */
    public void shutdown() {
        notebook.saveToFiles();
        parent.setVisible(false);
    }

    /**
     * Toggles the display of graph ruling.
     */
    public void toggleGraphRuling() {
        ruling = ruling == RulingType.GRAPH ? RulingType.NONE : RulingType.GRAPH;
        resetCachedImage();
    }

    /**
     * Whether to display the help panel.
     */
    public void toggleHelp() {
        showHelp = !showHelp;
        showHelpSplash = false;
    }

    /**
     * Toggles the display of (line) ruling.
     */
    public void toggleRuling() {
        ruling = ruling == RulingType.LINE ? RulingType.NONE : RulingType.LINE;
        resetCachedImage();
    }
}

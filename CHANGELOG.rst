.. Copyright © 2013 Martin Ueding <dev@martin-ueding.de>

#########
Changelog
#########

v1.7.7
    * Be more careful before using JOptionPane.

v1.7.6
    * Add pre-commit script.
    * Add readme file with installation instructions.
    * Convert man page to reST.
    * Prevent crash in headless mode when logging a text.
    * Use XDG standard directories, offer migration (.config, .local/share).
    * Use canonical path in test.

    - Remove Doxygen file and makefile target.
    - Rename finalize method, not deprecated any more.

v1.7.5
    * Clean up makefile, exit on errors.

    - Remove unused links from manual page.

v1.7.4
    * Use pandoc instead of ronn.

v1.7.3
    * Reoder manual page.

v1.7.2
    * Faster startup.

v1.7.1
    * Document file format.
    * Fix FileFilter not to match 000000.png.

    - Update tests for FileFilter.

v1.7
    * Filename numbering compression. (opt-in)

v1.6.6
    * High resolution icons.

    - License in auxilary files.

v1.6.5
    * Fix permissions of files via "make install".

v1.6.4
    * Do not depend on ronn at this time for "make install".

v1.6.3
    * Install manual page with "make install".

    - Cleaner jscribble.pot.

v1.6.2
    * Reorganisation of files in source tree (new artwork, config and l10n directory).

    - Fix spelling and grammar mistakes in manual page.

v1.6.1
    * Fix prefix of CHANGELOG in tarball

v1.6
    * Enable overriding of config keys on the command line.
    * Add bash completion for config keys.

v1.5.5
    * Automatic save on every page change to prevent data loss.

    - Gray logo for selection window.
    - Unified test suite.
    - Fix race condition in WriteoutThread queue checking.

v1.5.4
    * Save everything, even if program is interrupted.
    * Close notebook on "q" key.

    - Show assigned key from the (user) config file in help screen.

v1.5.3
    * Add Java Webstart.
    * Create extensive manual page.

    - Fix drawing dots (broken since v1.5.2).
    - Allow movements with extra mouse buttons (enable in config).
    - Add config option to disable eraser.

v1.5.2
    * Create more config options, e. g. mouse buttons and keyboard keys.
    * Repaint ruling and onion layers after erasing.

v1.5.1
    * Create config for most settings, overwriteable by user.
    * Optional memory usage display.

v1.5
    * Support for any screen size.

v1.4
    * Add ruling and graph paper.
    * Add splash screen for help.

v1.3
    * Add eraser on right click.

    - Sort notebooks in the selection window.

v1.2.2
    * Multiple layers in onion mode.

    - Add window icon in notebook view too.

v1.2.1
    * Use git describe version number.

v1.1.1
    * Add "make install" target.

v1.1
    * Add German localization.
    * Add scroll panels for touch only devices.
    * Use a config file.

    - Add -v flag to activate log messages.
    - Add logo to main window.
    - Add simple man page.
    - Delete the temporary files after the programs exists.
    - Disallow to resize the window.
    - Fix null pointer exception when clicking cancel on new dialog.
    - Fix temp file permissions.

v1.0.1
    * Add desktop entry.
    * Add launcher script
    * Add logo for the program.

v1.0
    * Do not close application after closing notebook.
    * Drastically simplify file structure, tests are still buggy.
    * Open notebook on double click.
    * Save all notebooks that are opened when closing the opening dialog.

    - Add version flag to CLI.
    - Bug fix blank page reloading error.
    - Fix case where going back and forth faster than WriteoutThread could handle writing.

v0.3
    * Add default directory option.
    * Add delete function.
    * Add help message.
    * Add more keys to control program.
    * Draw only with primary mouse button.

    - Ask user when the default directory is not valid any more.
    - Display page count in overview.

v0.2.1
    * Add option to write out loading and writing.

v0.1
    * Initial release.

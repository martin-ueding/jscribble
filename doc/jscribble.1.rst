#########
jscribble
#########

********************************
infinite notepad written in Java
********************************

:Author: Martin Ueding <dev@martin-ueding.de>
:Date: 2012-02-27
:Manual section: 1

SYNOPSIS
========
``jscribble [options]``

``java -jar jscribble.jar [options]``

``javaws http://martin-ueding.de/download/jscribble/jscribble.jnlp``


DESCRIPTION
===========
jscribble is a graphical notepad with as many pages as you want. You can
organize your notes within notepads. Best used with a graphics tablet or
touchscreen.

You can organize your notes into notebooks, which allow you to have several
collections of notesheets. At startup, you can select which notepad you want to
use. If you do not want to save your notes, you can go into the *scribble*
mode. All notes that you take there, will be thrown away when you close the
program---without any sheet of paper wasted.

Taking notes on paper allows you to draw and write whatever you want.  Some
people are fast enough in LaTeX to set complicated formulas while in a lecture.
Some prefer to draw these by hand but do not like carrying lots of paper with
them.

jscribble offers screen based note taking with as many pages as you want.
Xournal offers more features but is paper size oriented which does not make
sense on a 10" netbook screen where one wants to use the whole screen for
taking notes.

Another focus was set to avoid any mouse navigation. All you can do with your
mouse (or pen) is draw, therefore you cannot click anything accidentally.
Navigation is done with the keyboard. This stance is weakened a little now,
there is a mouse only mode which features panels at the left and right side of
the screen to flip pages. You can also advance to the next page using the
middle click. Both of these options have to be enabled in the configuration
file, though.

The eraser was implemented later too, it can be disabled in the config if it
causes trouble. It is activated by default since it is very handy.

You need the Java Runtime Environment (JRE) to run jscribble.


OPTIONS
=======
``-v``
    Verbose Mode.
``--key value``
    Allows one to overwrite any config key in the default config and the user
    config. See below for a list of all available config keys


DEFAULT CONTROLS
================
To flip between the pages, you can use the arrows keys, space bar, enter and
backspace.

When the primary mouse button is pressed, a line is drawn, the secondary mouse
button erases.

All controls are displayed in the online help screen activated by ``h`` or
``F1``.


FILES
=====
jscribble uses plain PNG images to store your drawings. They are, by default,
in the ``~/.jscribble`` directory. On Windows, that is somewhere in the
``C:\Documents & Settings`` or ``C:\Users`` directory.

The notebooks are plain folders, each containing a heap of images. The folder
name corresponds to the notebook name. To avoid strange names, the notebook
name is restricted to very few characters by default.

The images are numbered, zero padded to 6 digits. The program uses ``%06d.png``
to format the number. In case you have more images, the file name will get
longer. ::

    ~/.jscribble/test_notebook/
        000001.png
        000002.png
        ...
        000100.png
        ...
        999999.png
        1000000.png
        ...

You can delete images from the folder, the program will still find them.  You
will get something like ``Page 15/13`` if you delete two images in front of the
gast one.

There is a configuration option that will tell jscribble to automatically
compress the file names, that is to renumber them so that they are consecutive.


Config File
-----------
jscribble supports a user config file that overrides values from the default
config file.

Create a file called *config.properties* in the *.jscribble* directory in your
home directory. Then insert key-value pairs with an equal sign in each line.

The format is the standard Java Properties format.


Value Types
-----------
**String**
    A string which will get parsed in some way.

**Integer**
    A number, often sizes measured in pixels.

**Color**
    A hexadecimal color with optional alpha part in the front, RRGGBB or
    AARRGGBB.

**Float**
    A decimal number.

**Keyboard Char or Code**
    A comma separated list of key key codes and key chars. A single character
    will be interpreted as a literal character on the keyboard, multiple
    characters will be interpreted as a key code number.

**Mouse Mask**
    A comma separated list of mouse down masks.

**Boolean**
    ``true`` or ``false``.


Example Config
--------------
::

    # example config file for jscribble saved in .jscribble/config.properties
    memory_usage_show=true
    rule_line_spacing=20


Available Keys
--------------
The type is given in brackets, the default value in parentheses.

.. include:: keylist.rst


SECURITY CONSIDERATIONS
=======================
Named notebooks are saved in a folder in the user's home directory.  Notebooks
that are in the scribble mode are not saved to the user's home directory but in
the temporary directory of the system. On Linux, this is usually ``/tmp``. The
files are not readable to anyone but the user.

If the user's home directory is encrypted, but the temporary folder is not, an
attacker might be able to recover images drawn in the scribble mode.


BUGS
====
Please report bugs in English or German via email to me. Include the version
number which is displayed at the bottom of the help screen within the program.


SEE ALSO
========
`Xournal <http://xournal.sourceforge.net/>`_

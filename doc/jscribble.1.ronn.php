# jscribble(1) -- infinite notepad written in Java

## SYNOPSIS

`jscribble` [options]

`java -jar jscribble.jar` [options]

`javaws http://martin-ueding.de/download/jscribble/jscribble.jnlp`

## DESCRIPTION

jscribble is a graphical notepad with as many pages as you want. You can
organize your notes within notepads. Best used with a graphics tablet or
touchscreen.

Taking notes on paper allows you to draw and write whatever you want. Some
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

## OPTIONS

* `-v`:
  Verbose Mode.
* `--key value`:
  Allows one to overwrite any config key in the default config and the user config. See below for a list of all available config keys

## DEFAULT CONTROLS

To flip between the pages, you can use the arrows keys, space bar, enter and
backspace.

When the primary mouse button is pressed, a line is drawn, the secondary mouse
button erases.

All controls are displayed in the online help screen activated by `h` or `F1`.

## INSTALLING

Download the current `.jar` file from the [jscribble website][jsc]. Copy this file wherever you like, e. g. your program folder or your home directory.

To be always up to date, use the [webstart][web].

If you use Debian or Arch Linux, you can use the package.


## CONFIG FILE

jscribble supports a user config file that overrides values from the default
config file.

Create a file called *config.properties* in the *.jscribble* directory in your
home directory. Then insert key-value pairs with an equal sign in each line.

The format is the standard Java Properties format.

### Value Types

* **String**:
  A string which will get parsed in some way.

* **Integer**:
  A number, often sizes measured in pixels.

* **Color**:
  A hexadecimal color with optional alpha part in the front, RRGGBB or AARRGGBB.

* **Float**:
  A decimal number.

* **Keyboard Char or Code**:
  A comma separated list of key key codes and key chars. A single character will be interpreted as a literal character on the keyboard, multiple characters will be interpreted as a key code number.

* **Mouse Mask**:
  A comma separated list of mouse down masks.

* **Boolean**:
  `true` or `false`.

### Example Config

	# example config file for jscribble saved in .jscribble/config.properties
	memory_usage_show=true
	rule_line_spacing=20

### Available Keys

The type is given in brackets, the default value in parentheses.

	<?php
	# Parse the default config file and insert the comments and default value
	# here.
	echo "\n";

	$file = file_get_contents('config/config.js');
	$config = json_decode($file, true);

	foreach ($config as $item) {
		echo "* `".$item['key']."`:\n";
		if (!empty($item['comment']))
			echo "  ".$item['comment']."\n";
		echo " [".$item['type']."] (".$item['value'].")\n";
	}
	?>

## SECURITY CONSIDERATIONS

Named notebooks are saved in a folder in the user's home directory. Notebooks
that are in the scribble mode are not saved to the user's home directory but in
the temporary directory of the system. On Linux, this is usually `/tmp`. The
files are not readable to anyone but the user.

If the user's home directory is encrypted, but the temporary folder is not, an
attacker might be able to recover images drawn in the scribble mode.

## PACKAGES

This program is featured in [Debian Sid][sid], [Debian Wheezy][whe] and [Arch Linux][arc] so far.

## BUILDING FROM SOURCE

### Just The Program

You can obtain a tarball with the latest source code from the [jscribble
website][jsc].

The following software is needed to compile this program:

* **make**:
  Build system.
* **Java Development Kit** (JDK):
  Java compiler, version 1.6 works.
* **xgettext**, **msgfmt**:
  Parses source code for translation calls and converts translation file into the Java property format.
* **php5-cli**:
  Used for various file creations, lists all the config entries in the manual page for instance.
* **various Linux tools**:
  find, rm, touch, bash

To build the main program, simply invoke make:

	make

Then you can just launch the program with a `java -jar jscribble.jar`. In case
you use Linux and want to install it for all users, run this:

	make install	# as root

Now it can be launched with a simple `jscribble`.

### Developer Documentation

In case you want to build all the developer documentation, you might want to
install these as well:

* **doxygen**:
  A HTML and LaTeX documentation generator for various languages.
* **javadoc**:
  A HTML documentation generator for Java. This should be included with the Java Development Kit (JDK).

To build the documentation, call:

	build dev-doc

### Unit Tests

jscribble has a couple test cases which can be run with junit.

* **junit**:
  Unit test runner for Java.

To run the tests, call `make test`.

### User Manual

This manual is written in the ronn-format, the tarball already contains the
converted output to lighten the build dependencies. In case you want to convert
the ronn-format manual page, you need ronn as well.

* **ronn**:
  Converts markdown into html and manual page. Get it [here][ron].

To convert the ronn manual page into a roff and html manual page, invoke:

	make doc

## BUGS

Please report bugs in English or German via email to me. Include the version
number which is displayed at the bottom of the help screen within the program.

## CONTRIBUTING

If you like, you can send patches from the latest source checkout. Please
include which version you based on, so that I can merge. If you care for the
git repository, please let me know.

## AUTHOR

Martin Ueding <dev@martin-ueding.de>

## COPYRIGHT

Copyright © 2011 Martin Ueding <dev@martin-ueding.de>

## SEE ALSO

[Xournal][xou]

[xou]: http://xournal.sourceforge.net/
[jsc]: http://martin-ueding.de/jscribble/
[sid]: http://packages.debian.org/sid/jscribble
[whe]: http://packages.debian.org/wheezy/jscribble
[arc]: https://aur.archlinux.org/packages.php?ID=51337
[ron]: http://rtomayko.github.com/ronn/
[web]: http://martin-ueding.de/download/jscribble/jscribble.jnlp

	<?php /* vi: set filetype=markdown: */ ?>

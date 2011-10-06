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

The value in parentheses is the default value.

	<?php
	// Parse the default config file and insert the comments and default value
	// here.
	echo "\n";

	$config_file = 'jscribble/default_config.properties';
	if (!file_exists($config_file)) {
		die('Could not find default config.');
	}

	$lines = file($config_file);

	$i = 1;

	while ($i < count($lines)) {
		// Advance until there is a comment sign in the line.
		while ($lines[$i][0] != '#') {
			$i++;
		}

		// Assume that all the following lines are the comment for the key-value
		// pair that follows after that.

		while ($i < count($lines) && $lines[$i][0] == '#') {
			$comment_pieces[] = trim(substr($lines[$i], 2));
			$i++;
		}
		$comment = implode(' ', $comment_pieces);
		unset($comment_pieces);

		// Skip over any blank lines that might be between the comment and the
		// key-value pair.
		while ($i < count($lines) && strlen(trim($lines[$i])) == 0) {
			$i++;
		}

		// If the last comment was a finish commit at the end of the file, break
		// here.
		if ($i >= count($lines)) {
			break;
		}

		// There should be a non-comment, non-blank line which can only be a
		// key-value pair.
		$key_value = trim($lines[$i]);
		$key_value_array = explode('=', $key_value);
		$key = $key_value_array[0];
		$value = $key_value_array[1];

		echo "* `$key`:\n";
		echo "  $comment\n";
		echo "  ($value)\n";

		$i++;
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

This program is featured in [Debian][deb] and [Arch Linux][arc] so far.

## BUILDING FROM SOURCE

You can obtain a tarball with the latest source code from the [jscribble
website][jsc].

If the minimum set is installed, you should be able to compile and install with
this:

	make jscribble.jar
	make install	# as root

The following software is needed to compile this program:

* **make**:
  Build system.
* **Java Development Kit** (jdk):
  Java compiler.
* **xgettext**, **msgfmt**:
  Parses source code for translation calls and converts translation file into the Java property format.
* **php5-cli**:
  Reads the default config file and parses the options into the manual page. It is also needed to parse the config for the bash completion.
* **various linux tools**:
  find, rm, touch, bash

In case you want to build all the documentation, you might want to install these as well:

* **doxygen**:
  A HTML and LaTeX documentation generator for various languages.
* **javadoc**:
  A HTML documentation generator for Java. This should be included with the Java Development Kit.
* **junit**:
  Unit test runner for Java.

This manual is written in the ronn-format, the tarball already contains the converted output to lighten the build dependencies. In case you want to convert the ronn-format manual page, you need ronn as well.

* **ronn**:
  Converts markdown into html and manual page. Get it [here][ron].

Then you can call this to compile the binary and the manual page:

	make

To build the javadoc and doxygen output, use this:

	make all

To run all tests:

	make test

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

Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

## SEE ALSO

[Xournal][xou]

[xou]: http://xournal.sourceforge.net/
[jsc]: http://martin-ueding.de/jscribble/
[deb]: http://packages.debian.org/sid/jscribble
[arc]: https://aur.archlinux.org/packages.php?ID=51337
[ron]: http://rtomayko.github.com/ronn/
[web]: http://martin-ueding.de/download/jscribble/jscribble.jnlp

	<?php /* vi: set filetype=markdown: */ ?>

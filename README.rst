jscribble
=========

building from source
--------------------

just the program
^^^^^^^^^^^^^^^^
You can obtain a tarball with the latest source code from the [jscribble
website](http://martin-ueding.de/jscribble/)

The following software is needed to compile this program:

make
    Build system.
Java Development Kit
    Java compiler, version 1.6 works.
xgettext, msgfmt
    Parses source code for translation calls and converts translation file into
    the Java property format.
php5-cli
    Used for various file creations, lists all the config entries in the manual
    page for instance.
pandoc
    Converts markdown into html and manual page.
various Linux tools
    find, rm, touch, bash

To build the main program, simply invoke make::

	make

Then you can just launch the program with a `java -jar jscribble.jar`. In case
you use Linux and want to install it for all users, run this::

	make install    # as root

Now it can be launched with a simple `jscribble`.


developer documentation
^^^^^^^^^^^^^^^^^^^^^^^
In case you want to build all the developer documentation, you might want to
install these as well:

doxygen
    A HTML and LaTeX documentation generator for various languages.
javadoc
    A HTML documentation generator for Java. This should be included with the
    Java Development Kit (JDK).

To build the documentation, call::

	build dev-doc


unit tests
^^^^^^^^^^
jscribble has a couple test cases which can be run with junit.

junit
    Unit test runner for Java.

To run the tests, call ``make test``.


contributing
------------
If you like, you can send patches from the latest source checkout. Please
include which version you based on, so that I can merge. If you care for the
git repository, please let me know.

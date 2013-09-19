# Copyright © Martin Ueding <dev@martin-ueding.de>

# This file is part of jscribble.
#
# jscribble is free software: you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by the Free Software
# Foundation, either version 2 of the License, or (at your option) any later
# version.
#
# jscribble is distributed in the hope that it will be useful, but WITHOUT ANY
# WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
# A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along with
# jscribble.  If not, see <http://www.gnu.org/licenses/>.

###########################################################################
#                          Variables, File Lists                          #
###########################################################################

# Current version of the program.
name=jscribble
version:=1.7.7

# Find all the Java files in the source tree and generate a matching list of
# class files (targets) from that.
alljavafiles:=$(shell find . -name "*.java")
allclassfiles:=$(alljavafiles:.java=.class)
javafiles:=$(shell find jscribble -name "*.java")
classfiles:=$(javafiles:.java=.class)

# Java compiler to use.
javac:=javac -encoding UTF-8

# Output file names.
foldername:=$(name)-$(version)
signedjar:=$(name)_$(version).jar
tarball:=$(name)_$(version).tar.gz

.DELETE_ON_ERROR:

###########################################################################
#                              Named Targets                              #
###########################################################################

# Builds the main program.
all: program install_files/completion/jscribble doc

# Removes all build files. The changelog and the manual page are not included
# since they are hard to come by.
clean:
	$(RM) $(allclassfiles)
	$(RM) *.jar
	$(RM) *.mo
	$(RM) -r javadoc
	$(RM) .testrun
	$(RM) doc/jscribble.1
	$(RM) doc/keylist.rst
	$(RM) install_files/completion/jscribble
	$(RM) jscribble/VersionName.java
	$(RM) jscribble/default_config.properties
	$(RM) jscribble_*.*.tar.gz
	$(RM) l10n/de.po~
	$(RM) l10n/jscribble.pot
	$(RM) l10n/jscribble_*.properties

doc: doc/jscribble.1

# Extracts all javadoc comments from the source codes and generates HTML pages
# with that information.
doc-dev: javadoc/index.html

# Creates the template for translators.
i18n: l10n/jscribble.pot

# Installs the jar file and the launcher script into DESTDIR.
install: jscribble.jar install_files/completion/jscribble
	mkdir -p "$(DESTDIR)/usr/share/jscribble"
	install -m 644 jscribble.jar "$(DESTDIR)/usr/share/jscribble/"
	mkdir -p "$(DESTDIR)/usr/bin"
	install -m 755 install_files/jscribble "$(DESTDIR)/usr/bin/"
	mkdir -p "$(DESTDIR)/etc/bash_completion.d/"
	install -m 644 install_files/completion/jscribble "$(DESTDIR)/etc/bash_completion.d/"
	mkdir -p "$(DESTDIR)/usr/share/man/man1/"
	install -m 644 doc/jscribble.1 "$(DESTDIR)/usr/share/man/man1/"
	gzip "$(DESTDIR)/usr/share/man/man1/jscribble.1"

program: jscribble/default_config.properties jscribble.jar

# Creates a jar file which contains the version name, ready for Java Web Start
# deployment.
signed-jar: $(signedjar)

# Creates a tarball which contains all the source code. The changelog and the
# manual page are included, although they are build files, since the tools
# needed for that are not ubiquitous.
tarball: $(tarball)

# Runs all available unit tests.
test: program $(allclassfiles)
	junit -text tests.JscribbleTestSuite

###########################################################################
#                             Explicit Rules                              #
###########################################################################

# Signs the jar file with Martin's key.
$(signedjar): jscribble.jar
	jarsigner $< mu
	cp $< $@

# Put all the files that are tracked in git in a tarball and amend the hard to
# come by build files.
$(tarball): .git/HEAD
	$(RM) $@
	git archive --prefix=$(foldername)/ HEAD > $@

# Creates a changelog file with the information in the git tags.
CHANGELOG: .git/HEAD
	git-changelog > CHANGELOG

# Creates the roff manual page.
doc/jscribble.1: doc/jscribble.1.rst doc/keylist.rst
	rst2man $< $@

# Inserts the values and comments from the default config into the manual page.
doc/keylist.rst: doc/keylist.php jscribble/default_config.properties
	php $< > $@

install_files/completion/jscribble: install_files/completion/generate_completion config/config.js
	$< > $@

# Create standard javadoc.
javadoc/index.html: $(alljavafiles)
	javadoc -d javadoc $^

jscribble/default_config.properties: config/generate_properties config/config.js
	$< > $@

# Generate a Java file that tells the program its version number.
jscribble/VersionName.java: makefile
	./generate_version_class $(version) > $@

# Put all the class files into the jar.
jscribble.jar: jscribble/VersionName.class $(classfiles) l10n/jscribble_de.properties jscribble/default_config.properties artwork/jscribble.png artwork/jscribble_gray.png
	jar -cfm $@ manifest.txt $^

# Convert the object into a Java compatible file.
l10n/jscribble_de.properties: l10n/de.po
	msgcat --properties-output -o $@ $<

# Extract the strings from all Java source files.
l10n/jscribble.pot: $(alljavafiles)
	xgettext -o $@ -k -k"Localizer.get" $^ --from-code=utf-8

###########################################################################
#                             Implicit Rules                              #
###########################################################################

# How to compile regular Java files.
jscribble/%.class: jscribble/%.java
	$(javac) $<

# How to compile unit tests.
tests/%.class: tests/%.java
	$(javac) -classpath /usr/share/java/junit.jar -sourcepath .:jscribble $<

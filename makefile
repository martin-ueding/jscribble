# Copyright © Martin Ueding <dev@martin-ueding.de>

###########################################################################
#                          Variables, File Lists                          #
###########################################################################

# Find all the Java files in the source tree and generate a matching list of
# class files (targets) from that.
alljavafiles:=$(shell find . -name "*.java")
allclassfiles:=$(alljavafiles:.java=.class)
javafiles:=$(shell find jscribble -name "*.java")
classfiles:=$(javafiles:.java=.class)

# Java compiler to use.
javac:=javac -encoding UTF-8

# Current version of the program.
name=jscribble
version:=1.5.4

# Output file names.
tarball:=$(name)_$(version).tar.gz
foldername:=$(name)-$(version)
signedjar:=$(name)_$(version).jar

###########################################################################
#                              Named Targets                              #
###########################################################################

# Builds the main program.
all: jscribble.jar

# Creates the template for translators.
i18n: jscribble.pot

# Assembles the documentation for the users.
doc: doc/jscribble.1

# Extracts all javadoc comments from the source codes and generates HTML pages
# with that information.
doc-dev: javadoc/.javadoc html/.doxygen

# Creates a tarball which contains all the source code. The changelog and the
# manual page are included, although they are build files, since the tools
# needed for that are not ubiquitous.
tarball: $(tarball)

# Runs all available unit tests.
test: $(allclassfiles)
	junit -text tests.JscribbleTestSuite

# Removes all build files. The changelog and the manual page are not included
# since they are hard to come by.
clean:
	$(RM) *.jar jscribble_*.*.tar.gz
	$(RM) *.mo jscribble_*.properties de.po~
	$(RM) -r html javadoc
	$(RM) .testrun
	$(RM) doc/jscribble.1.html
	$(RM) jscribble.pot
	$(RM) jscribble/VersionName.java
	$(RM) $(allclassfiles)

# Installs the jar file and the launcher script into DESTDIR.
install: jscribble.jar
	mkdir -p "$(DESTDIR)/usr/share/jscribble"
	install jscribble.jar "$(DESTDIR)/usr/share/jscribble/"
	mkdir -p "$(DESTDIR)/usr/bin"
	install install_files/jscribble "$(DESTDIR)/usr/bin/"

# Creates a jar file which contains the version name, ready for Java Web Start
# deployment.
signed-jar: $(signedjar)

###########################################################################
#                             Explicit Rules                              #
###########################################################################

# Signs the jar file with Martin's key.
$(signedjar): jscribble.jar
	jarsigner $< mu
	cp $< $@

# Put all the files that are tracked in git in a tarball and amend the hard to
# come by build files.
$(tarball): doc/jscribble.1 CHANGELOG .git/HEAD
	$(RM) $@
	git archive --prefix=$(foldername)/ HEAD > $(basename $@ .gz)
	tar -cf surrogates.tar doc/jscribble.1 CHANGELOG
	tar -Af $(basename $@ .gz) surrogates.tar
	$(RM) surrogates.tar
	gzip $(basename $@ .gz)

# Creates a changelog file with the information in the git tags.
CHANGELOG: .git/HEAD
	git-changelog > CHANGELOG

# Creates the roff manual page.
doc/jscribble.1: doc/jscribble.1.ronn
	ronn $^ --style=toc --manual=jscribble

# Inserts the values and comments from the default config into the manual page.
doc/jscribble.1.ronn: doc/jscribble.1.ronn.php jscribble/default_config.properties
	php $^ > $@

# Put all the class files into the jar.
jscribble.jar: jscribble/VersionName.class $(classfiles) jscribble_de.properties jscribble/default_config.properties artwork/jscribble.png artwork/jscribble_gray.png
	jar -cfm $@ manifest.txt $^

# Convert the object into a Java compatible file.
jscribble_de.properties: de.po
	msgcat --properties-output -o $@ $^

# Extract the strings from all Java source files.
jscribble.pot: $(alljavafiles)
	xgettext -o $@ -k"Localizer.get" $^ --from-code=utf-8

# Create standard javadoc.
javadoc/.javadoc: $(alljavafiles)
	javadoc -d javadoc $^
	touch $@

# Create doxygen doc.
html/.doxygen: $(alljavafiles)
	doxygen
	touch $@

# Generate a Java file that tells the program its version number.
jscribble/VersionName.java: makefile
	./generate_version_class $(version)

###########################################################################
#                             Implicit Rules                              #
###########################################################################

# How to compile regular Java files.
jscribble/%.class: jscribble/%.java
	$(javac) $^

# How to compile unit tests.
tests/%.class: tests/%.java
	$(javac) -classpath /usr/share/java/junit.jar -sourcepath .:jscribble $^

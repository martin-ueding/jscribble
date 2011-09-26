# Copyright © Martin Ueding <dev@martin-ueding.de>

javafiles=$(shell find . -name "*.java")
classfiles=$(javafiles:.java=.class)

javac=javac -encoding UTF-8

version=1.5.3

jscribble.jar: jscribble/VersionName.java jscribble/NoteBookProgram.class jscribble_de.properties jscribble/default_config.properties
	find jscribble -name "*.class" -print > classlist
	jar -cfm $@ manifest.txt @classlist install_files/jscribble.png *.properties jscribble/default_config.properties
	rm classlist

all: jscribble.jar javadoc/.javadoc html/.doxygen jscribble.pot doc/jscribble.1

jscribble_de.properties: de.po
	msgcat --properties-output -o $@ $^

jscribble.pot: $(javafiles)
	xgettext -o $@ -k"Localizer.get" $^ --from-code=utf-8

jscribble/NoteBookProgram.class: $(javafiles)
	$(javac) jscribble/NoteBookProgram.java

javadoc/.javadoc: $(javafiles)
	javadoc -d javadoc $^
	touch $@

doxygen: html/.doxygen
html/.doxygen: $(javafiles)
	doxygen
	touch $@

jscribble/VersionName.java: makefile
	./generate_version_class $(version)

test:
	$(javac) -classpath /usr/share/java/junit.jar -sourcepath .:jscribble $(shell find tests -type f -name "*Test.java")
	bash run_tests.sh

tarball=jscribble_$(version).tar.gz
fullname=jscribble-$(version)

tarball: $(tarball)
$(tarball): doc/jscribble.1 CHANGELOG .git/HEAD
	$(RM) -f $@
	git archive --prefix=$(fullname)/ HEAD > $(basename $@ .gz)
	tar -cf surrogates.tar doc/jscribble.1 CHANGELOG
	tar -Af $(basename $@ .gz) surrogates.tar
	$(RM) surrogates.tar
	gzip $(basename $@ .gz)

CHANGELOG: .git/HEAD
	git-changelog > CHANGELOG

install: jscribble.jar
	mkdir -p "$(DESTDIR)/usr/share/jscribble"
	install jscribble.jar "$(DESTDIR)/usr/share/jscribble/"
	mkdir -p "$(DESTDIR)/usr/bin"
	install install_files/jscribble "$(DESTDIR)/usr/bin/"

doc/jscribble.1: doc/jscribble.1.ronn
	$(ronn) $^ --style=toc --manual=jscribble

clean:
	$(RM) *.mo
	$(RM) -r html
	$(RM) -r javadoc
	$(RM) .testrun
	$(RM) classlist
	$(RM) de.po~
	$(RM) doc/jscribble.1.html
	$(RM) jscribble.jar.asc
	$(RM) jscribble.pot
	$(RM) jscribble/VersionName.java
	$(RM) jscribble_*.*.tar.gz
	$(RM) jscribble_*.properties
	find . -name "*.class" -delete
	find . -name "*.jar" -delete
	find . -name "*.orig" -delete

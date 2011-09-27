# Copyright © Martin Ueding <dev@martin-ueding.de>

###########################################################################
#                          Variables, File Lists                          #
###########################################################################

javafiles:=$(shell find . -name "*.java")
classfiles:=$(javafiles:.java=.class)

javac:=javac -encoding UTF-8

version:=1.5.3

tarball:=jscribble_$(version).tar.gz
fullname:=jscribble-$(version)

# TODO add target for signed jar

###########################################################################
#                              Named Targets                              #
###########################################################################

all: jscribble.jar

i18n: jscribble.pot

doc: doc/jscribble.1

doc-dev: javadoc/.javadoc html/.doxygen

tarball: $(tarball)

test: $(classfiles)
	bash run_tests.sh

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

install: jscribble.jar
	mkdir -p "$(DESTDIR)/usr/share/jscribble"
	install jscribble.jar "$(DESTDIR)/usr/share/jscribble/"
	mkdir -p "$(DESTDIR)/usr/bin"
	install install_files/jscribble "$(DESTDIR)/usr/bin/"

###########################################################################
#                             Explicit Rules                              #
###########################################################################

$(tarball): doc/jscribble.1 CHANGELOG .git/HEAD
	$(RM) $@
	git archive --prefix=$(fullname)/ HEAD > $(basename $@ .gz)
	tar -cf surrogates.tar doc/jscribble.1 CHANGELOG
	tar -Af $(basename $@ .gz) surrogates.tar
	$(RM) surrogates.tar
	gzip $(basename $@ .gz)

CHANGELOG: .git/HEAD
	git-changelog > CHANGELOG

doc/jscribble.1: doc/jscribble.1.ronn
	ronn $^ --style=toc --manual=jscribble

doc/jscribble.1.ronn: doc/jscribble.1.ronn.php jscribble/default_config.properties
	php $^ > $@

jscribble.jar: jscribble/VersionName.class $(classfiles) jscribble_de.properties jscribble/default_config.properties install_files/jscribble.png
	jar -cfm $@ manifest.txt $^

jscribble_de.properties: de.po
	msgcat --properties-output -o $@ $^

jscribble.pot: $(javafiles)
	xgettext -o $@ -k"Localizer.get" $^ --from-code=utf-8

javadoc/.javadoc: $(javafiles)
	javadoc -d javadoc $^
	touch $@

html/.doxygen: $(javafiles)
	doxygen
	touch $@

jscribble/VersionName.java: makefile
	./generate_version_class $(version)

###########################################################################
#                             Implicit Rules                              #
###########################################################################

jscribble/%.class: jscribble/%.java
	$(javac) $^

tests/%.class: tests/%.java
	$(javac) -classpath /usr/share/java/junit.jar -sourcepath .:jscribble $^

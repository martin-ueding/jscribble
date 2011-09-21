# Copyright © Martin Ueding <dev@martin-ueding.de>

javafiles=$(shell find . -name "*.java")
classfiles=$(javafiles:.java=.class)

javac=javac -encoding UTF-8

version=1.5

jscribble.jar: jscribble/VersionName.java jscribble/NoteBookProgram.class classlist jscribble_de.properties
	jar -cfm $@ manifest.txt @classlist install_files/jscribble.png *.properties

classlist: $(shell find jscribble -name "*.class")
	find jscribble -name "*.class" -print > classlist

all: jscribble.jar javadoc/.javadoc html/.doxygen jscribble.pot

jscribble_de.properties: de.po
	msgcat --properties-output -o $@ $^

jscribble.pot: $(javafiles)
	xgettext -o $@ -k"Localizer.get" $^ --from-code=utf-8

jscribble/NoteBookProgram.class: $(javafiles)
	$(javac) jscribble/NoteBookProgram.java

javadoc/.javadoc: $(javafiles)
	javadoc -d javadoc $^
	touch $@

html/.doxygen: $(javafiles)
	doxygen
	touch $@

jscribble/VersionName.java: makefile
	./generate_version_class $(version)

test: .testrun

.testrun:
	$(javac) -classpath /usr/share/java/junit.jar -sourcepath .:jscribble $(shell find tests -type f -name "*Test.java")
	bash run_tests.sh
	touch .testrun

tarball=jscribble_$(version).tar.gz
fullname=jscribble-$(version)

tarball: $(tarball)
$(tarball):
	git archive --prefix=$(fullname)/ HEAD | gzip > $@

install: jscribble.jar
	mkdir -p "$(DESTDIR)/usr/share/jscribble"
	install jscribble.jar "$(DESTDIR)/usr/share/jscribble/"
	mkdir -p "$(DESTDIR)/usr/bin"
	install install_files/jscribble "$(DESTDIR)/usr/bin/"

clean:
	$(RM) *.mo
	$(RM) -r html
	$(RM) -r javadoc
	$(RM) .testrun
	$(RM) classlist
	$(RM) de.po~
	$(RM) jscribble.jar.asc
	$(RM) jscribble.pot
	$(RM) jscribble/VersionName.java
	$(RM) jscribble_*.*.tar.gz
	$(RM) jscribble_*.properties
	find . -name "*.class" -delete
	find . -name "*.jar" -delete
	find . -name "*.orig" -delete

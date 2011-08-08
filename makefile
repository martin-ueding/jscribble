# Copyright (c) Martin Ueding <dev@martin-ueding.de>

javafiles=$(shell find . -name "*.java")
classfiles=$(javafiles:.java=.class)

version=1.1

jscribble.jar: jscribble/VersionName.java jscribble/NoteBookProgram.class classlist jscribble_de.properties
	jar -cfm $@ manifest.txt @classlist install_files/jscribble.png jscribble/config.txt *.properties

classlist: $(shell find jscribble -name "*.class")
	find jscribble -name "*.class" -print > classlist

all: jscribble.jar javadoc/.javadoc html/.doxygen jscribble.pot

jscribble_de.properties: de.po
	msgcat --properties-output -o $@ $^

jscribble.pot: $(javafiles)
	xgettext -o $@ -k"Localizer.get" $^

jscribble/NoteBookProgram.class: $(javafiles)
	javac jscribble/NoteBookProgram.java

javadoc/.javadoc: $(javafiles)
	javadoc -d javadoc $^
	touch $@

html/.doxygen: $(javafiles)
	doxygen
	touch $@

jscribble/VersionName.java:
	./generate_version_class

test: .testrun

.testrun:
	javac -classpath /usr/share/java/junit.jar -sourcepath .:jscribble $(shell find tests -type f -name "*Test.java")
	bash run_tests.sh
	touch .testrun

fullname=jscribble_$(version).orig.tar.gz
fullnamedash=jscribble-$(version)
tarball: ../$(fullname)

../$(fullname): clean
	mkdir $(fullnamedash)
	cp -r generate_version_class jscribble license.txt manifest.txt run_tests.sh install_files jscribble.1 makefile README.markdown tests *.po $(fullnamedash)
	tar -czf $@ $(fullnamedash) 
	$(RM) -r jscribble-*.*

install: jscribble.jar
	mkdir -p "$(DESTDIR)/usr/share/jscribble"
	cp jscribble.jar "$(DESTDIR)/usr/share/jscribble/"
	mkdir -p "$(DESTDIR)/usr/bin"
	cp install_files/jscribble "$(DESTDIR)/usr/bin/"

clean:
	$(RM) *.mo
	$(RM) -r html
	$(RM) -r javadoc
	$(RM) .testrun
	$(RM) classlist
	$(RM) jscribble.jar.asc
	$(RM) jscribble.pot
	$(RM) jscribble_*.properties
	find . -name "*.class" -delete
	find . -name "*.jar" -delete
	find . -name "*.orig" -delete


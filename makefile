# Copyright (c) Martin Ueding <dev@martin-ueding.de>

javafiles=$(shell find . -name "*.java")
classfiles=$(javafiles:.java=.class)

# TODO use automatic variables here

jscribble.jar: jscribble/VersionName.java jscribble/NoteBookProgram.class classlist jscribble.1.gz
	jar -cfm jscribble.jar manifest.txt @classlist install_files/jscribble.png

classlist: $(shell find jscribble -name "*.class")
	find jscribble -name "*.class" -print > classlist

all: jscribble.jar javadoc/.javadoc html/.doxygen jscribble.pot

jscribble.pot: $(javafiles)
	xgettext -o jscribble.pot -k"Localizer.get" $(javafiles)

jscribble/NoteBookProgram.class: $(javafiles)
	javac jscribble/NoteBookProgram.java

javadoc/.javadoc: $(javafiles)
	javadoc -d javadoc $(javafiles)
	touch javadoc/.javadoc

html/.doxygen: $(javafiles)
	doxygen
	touch html/.doxygen

jscribble/VersionName.java:
	./generate_version_class

jscribble.1.gz: jscribble.manpage
	$(RM) jscribble.1.gz
	cp jscribble.manpage jscribble.1
	gzip jscribble.1

clean:
	$(RM) -r html
	$(RM) -r javadoc
	$(RM) classlist
	$(RM) jscribble.1.gz
	$(RM) jscribble.jar.asc
	$(RM) jscribble.pot
	find . -name "*.class" -delete
	find . -name "*.jar" -delete
	find . -name "*.orig" -delete


# Copyright (c) Martin Ueding <dev@martin-ueding.de>

javafiles=$(shell find . -name "*.java")
classfiles=$(javafiles:.java=.class)

jscribble.jar: jscribble/VersionName.java jscribble/NoteBookProgram.class
	jar -cfm jscribble.jar manifest.txt $(shell find . -name "*.class")

all: jscribble.jar javadoc/.javadoc html/.doxygen

jscribble/NoteBookProgram.class: $(javafiles)
	javac jscribble/NoteBookProgram.java

javadoc/.javadoc: $(javafiles)
	javadoc -d javadoc $(javafiles)
	touch javadoc/.javadoc

html/.doxygen: $(javafiles)
	doxygen
	touch html/.doxygen

jscribble/VersionName.java: .bzr/branch/last-revision
	./generate_version_class

clean:
	$(RM) -r javadoc
	$(RM) -r html
	find . -name "*.orig" -delete
	find . -name "*.class" -delete
	find . -name "*.jar" -delete

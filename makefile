# Copyright (c) Martin Ueding <dev@martin-ueding.de>

javafiles=$(shell find . -name "*.java")
classfiles=$(javafiles:.java=.class)

jscribble.jar: jscribble/VersionName.java jscribble/NoteBookProgram.class classlist
	jar -cfm $@ manifest.txt @classlist install_files/jscribble.png

classlist: $(shell find jscribble -name "*.class")
	find jscribble -name "*.class" -print > classlist

all: jscribble.jar javadoc/.javadoc html/.doxygen jscribble.pot

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

clean:
	$(RM) -r html
	$(RM) -r javadoc
	$(RM) .testrun
	$(RM) classlist
	$(RM) jscribble.1.gz
	$(RM) jscribble.jar.asc
	$(RM) jscribble.pot
	find . -name "*.class" -delete
	find . -name "*.jar" -delete
	find . -name "*.orig" -delete


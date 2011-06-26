# Copyright (c) Martin Ueding <dev@martin-ueding.de>

javafiles=$(wildcard *.java */*.java */*/*.java)
classfiles=$(javafiles:.java=.class)

jscribble.jar: jscribble/VersionName.java jscribble/NoteBookProgram.class
	jar -cfm jscribble.jar manifest.txt */*.class */*/*.class

all: jscribble.jar javadoc/.javadoc html/.doxygen

test: $(classfiles)
	testcasesJava=$(wildcard tests/*Test.java tests/*/*Test.java)
	testcases=$(testcasesJava:.java=)
	echo $(testcases)
	junit -text $(testcases)
	touch test

jscribble/NoteBookProgram.class: $(javafiles)
	javac jscribble/NoteBookProgram.java

javadoc/.javadoc: *.java
	javadoc -d javadoc $(javafiles)
	touch javadoc/.javadoc

html/.doxygen: *.java
	doxygen
	touch html/.doxygen

jscribble/VersionName.java: .bzr/branch/last-revision
	./generate_version_class

.PHONY: commit
commit:
	astyle *.java
	bzr commit

clean:
	$(RM) -r *.jar *.class
	$(RM) -r javadoc
	$(RM) -r html
	$(RM) -r *.orig

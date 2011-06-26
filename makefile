# Copyright (c) Martin Ueding <dev@martin-ueding.de>

jscribble.jar: VersionName.java NoteBookProgram.class
	jar -cfm jscribble.jar manifest.txt *.class

all: jscribble.jar javadoc/.javadoc html/.doxygen

.PHONY: test
test:
	testcasesJava=$(wildcard *Test.java)
	testcases=$(testcasesJava:.java=)
	echo $(testcases)
	junit -text $(testcases)

NoteBookProgram.class: *.java
	javac NoteBookProgram.java

javadoc/.javadoc: *.java
	javadoc -d javadoc *.java
	touch javadoc/.javadoc

html/.doxygen: *.java
	doxygen
	touch html/.doxygen

.PHONY: VersionName.java
VersionName.java:
	./generate_version_class

clean:
	$(RM) *.jar *.class
	$(RM) -r javadoc
	$(RM) -r html
	$(RM) *.orig

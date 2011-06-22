# Copyright (c) Martin Ueding <dev@martin-ueding.de>

jscribble.jar: Notizbuch.class
	jar -cfm jscribble.jar manifest.txt *.class

all: jscribble.jar javadoc/.javadoc

Notizbuch.class: *.java
	javac Notizbuch.java

javadoc/.javadoc: *.java
	javadoc -d javadoc *.java
	touch javadoc/.javadoc

clean:
	$(RM) *.jar *.class
	$(RM) -r javadoc

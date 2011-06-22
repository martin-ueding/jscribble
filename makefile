# Copyright (c) Martin Ueding <dev@martin-ueding.de>

all: jscribble.jar javadoc/.javadoc

jscribble.jar: Notizbuch.class
	jar -cfm jscribble.jar manifest.txt *.class

Notizbuch.class: *.java
	javac Notizbuch.java

javadoc/.javadoc: *.java
	javadoc -d javadoc *.java
	touch javadoc/.javadoc

clean:
	rm -rf *.jar *.class

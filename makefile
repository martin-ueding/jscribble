# Copyright (c) Martin Ueding <dev@martin-ueding.de>

jscribble.jar: Notizbuch.class
	jar -cfm jscribble.jar manifest.txt *.class

Notizbuch.class: *.java
	javac Notizbuch.java

clean:
	rm -rf *.jar *.class

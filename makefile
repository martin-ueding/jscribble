jscribble.jar: Notizbuch.class
	jar -cfm jscribble.jar manifest.txt *.class *.jpg

Notizbuch.class: *.java
	javac Notizbuch.java

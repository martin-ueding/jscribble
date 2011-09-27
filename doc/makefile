# Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

jscribble.1: jscribble.1.ronn
	ronn $^

jscribble.1.ronn: jscribble.1.ronn.php ../jscribble/default_config.properties
	php $^ > $@

.PHONY: clean
clean:
	$(RM) jscribble.1.ronn
	$(RM) jscribble.1.html

#!/bin/bash
# Copyright © 2011 Martin Ueding <dev@martin-ueding.de>

# exist if any of the tests fail
set -e

set -u

tests=$(find tests -type f -name "*.java" | sort -u | sed "s/\.java//" | sed "s#/#\.#g" | tr  '\n' ' ') 

for t in $tests
do
	junit -text $t
done

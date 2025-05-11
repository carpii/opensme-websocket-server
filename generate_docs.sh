#!/bin/sh
mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt >/dev/null
javadoc -d docs \
	-sourcepath src/main/java \
	-classpath "$(cat classpath.txt)" \
	-subpackages handlers -subpackages util
rm -f classpath.txt

#!/bin/sh
mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt >/dev/null
java -cp "target/classes:jar/h2-1.3.175.jar:$(cat classpath.txt)" WebSocketServer
rm -f classpath.txt

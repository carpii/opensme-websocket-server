#!/bin/bash

# Remove contents of docs directory if it exists
if [ -d "docs" ]; then
    rm -rf docs/*
fi

# Create docs directory
mkdir -p docs

# Get classpath from Maven
CLASSPATH=$(mvn -q exec:exec -Dexec.executable=echo -Dexec.args="%classpath")

# Generate Javadoc for all modules
javadoc -d docs \
    -sourcepath backend/src/main/java:frontend/src/main/java \
    -classpath "${CLASSPATH}" \
    -subpackages com.opensme \
    -quiet \
    -windowtitle "OpenSME WebSocket Server API" \
    -doctitle "OpenSME WebSocket Server API Documentation"

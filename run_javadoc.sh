#!/bin/bash

# Create docs directory if it doesn't exist
mkdir -p docs

# Get classpath from Maven
mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt >/dev/null

# Find existing packages with .java files
PACKAGES=$(find src/backend -type f -name "*.java" -exec dirname {} \; | \
    sort -u | \
    sed 's/.*src\///' | \
    tr '/' '.' | \
    tr '\n' ':' | \
    sed 's/:$//')

echo "Generating documentation for packages: ${PACKAGES}"

# Generate Javadoc documentation
javadoc -d docs \
    -sourcepath src \
    -classpath "target/classes:jar/h2-1.3.175.jar:$(cat classpath.txt)" \
    -subpackages backend:backend.handlers:backend.models \
    -private \
    -author \
    -version \
    -windowtitle "OpenSME WebSocket Server API" \
    -header "<h1>OpenSME WebSocket Server</h1>" \
    -bottom "<i>Copyright © 2025. All Rights Reserved.</i>"

# Clean up
rm -f classpath.txt

# Check if documentation was generated successfully
if [ $? -eq 0 ]; then
    echo "Documentation generated successfully in docs/"
else
    echo "Error generating documentation"
    exit 1
fi

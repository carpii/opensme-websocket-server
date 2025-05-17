#!/bin/bash
APP_HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_FILE="frontend/target/websocket-server-frontend-1.0.jar"
LIB_DIR="frontend/target/lib"

if [ ! -f "$JAR_FILE" ]; then
    mvn clean install -DskipTests
fi

if [ ! -f "$JAR_FILE" ]; then
    echo "Error: $JAR_FILE not found!"
    exit 1
fi

java -cp "${JAR_FILE}:${LIB_DIR}/*" com.opensme.frontend.PortfolioUI

#!/bin/bash
APP_HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_FILE="backend/target/websocket-server-backend-1.0.jar"
LIB_DIR="backend/target/lib"

if [ ! -f "$JAR_FILE" ]; then
    echo "Building project..."
    mvn clean install -DskipTests
fi

if [ ! -f "$JAR_FILE" ]; then
    echo "Error: $JAR_FILE not found!"
    exit 1
fi

echo "Starting WebSocket server..."
java -cp "${JAR_FILE}:${LIB_DIR}/*" com.opensme.backend.WebSocketServer
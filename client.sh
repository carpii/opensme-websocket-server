#!/bin/bash
# filepath: /home/carpii/dev/opensme-websocket-server/client.sh

# Check if no arguments or help flag is provided
if [ "${#}" -eq 0 ] || [ "${1}" == "--help" ]; then
    echo "Usage: ${0} <action> [id]"
    echo ""
    echo "Available actions:"
    echo "  portfolio.get <id>    - Fetch a specific portfolio by ID"
    echo "  portfolio.list        - List all portfolios"
    echo "  portfolioGroup.list   - List all portfolio groups"
    echo "  table.list            - List all database tables"
    echo ""
    exit 0
fi

ACTION=${1}
ID=${2}

# List of valid actions
VALID_ACTIONS=("portfolio.list" "portfolio.get" "portfolioGroup.list" "table.list")

# Check if the action is valid
if [[ ! " ${VALID_ACTIONS[@]} " =~ " ${ACTION} " ]]; then
    echo "Invalid action: ${ACTION}"
    echo ""
    echo "Usage: ${0} <action> [id]"
    echo ""
    echo "Available actions:"
    echo "  portfolio.get <id>    - Fetch a specific portfolio by ID"
    echo "  portfolio.list        - List all portfolios"
    echo "  portfolioGroup.list   - List all portfolio groups"
    echo "  table.list            - List all database tables"
    echo ""
    exit 1
fi

mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt >/dev/null

if [ "${ACTION}" = "portfolio.get" ]; then
    if [ -z "${ID}" ]; then
        echo '{"error": "portfolio.get requires a second argument: id"}' >&2
        exit 1
    fi
    java -cp "target/classes:jar/h2-1.3.175.jar:$(cat classpath.txt)" WebSocketClient "${ACTION}" "{\"id\":\"${ID}\"}"
elif [ "${ACTION}" = "portfolio.list" ] || [ "${ACTION}" = "table.list" ]; then
    java -cp "target/classes:jar/h2-1.3.175.jar:$(cat classpath.txt)" WebSocketClient "${ACTION}"
else
    java -cp "target/classes:jar/h2-1.3.175.jar:$(cat classpath.txt)" WebSocketClient "${ACTION}"
fi

rm -f classpath.txt

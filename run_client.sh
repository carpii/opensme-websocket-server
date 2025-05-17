#!/bin/bash
show_usage() {
    echo "Usage: ${0} <action> [id]"
    echo ""
    echo "Available CLI actions:"
    echo "  portfolio.get <id>      - Fetch a specific portfolio by ID"
    echo "  portfolio.get_items <id> - Fetch all items in a portfolio"
    echo "  portfolio.list          - List all portfolios"
    echo "  portfolio_group.list    - List all portfolio groups"
    echo "  table.list             - List all database tables"
    echo ""
}

# Check if no arguments or help flag is provided
if [ "${#}" -eq 0 ] || [ "${1}" == "--help" ]; then
    show_usage
    exit 0
fi

# CLI mode
ACTION=${1}
ID=${2}

# Get classpath from Maven
CLASSPATH=$(mvn -q exec:exec -Dexec.executable=echo -Dexec.args="%classpath")

# List of valid actions
VALID_ACTIONS=("portfolio.list" "portfolio.get" "portfolio.get_items" "portfolio_group.list" "table.list")

# Check if the action is valid
if [[ ! " ${VALID_ACTIONS[@]} " =~ " ${ACTION} " ]]; then
    echo "Invalid action: ${ACTION}"
    echo ""
    show_usage
    exit 1
fi

# Run the appropriate command
if [ "${ACTION}" = "portfolio.get" ] || [ "${ACTION}" = "portfolio.get_items" ]; then
    if [ -z "${ID}" ]; then
        echo "{\"error\": \"${ACTION} requires a second argument: id\"}" >&2
        exit 1
    fi
    java -cp "${CLASSPATH}" com.opensme.backend.WebSocketClient "${ACTION}" "{\"id\":\"${ID}\"}"
else
    java -cp "${CLASSPATH}" com.opensme.backend.WebSocketClient "${ACTION}"
fi

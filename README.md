# OpenSME WebSocket Server

A proof of concept websocket server and client for an OpenSME rewrite
Ultimately everything is subject to change, including the backend database and DB schema

## Prerequisites

- Java 17 or higher
- Maven 3.x
- H2 Database - drop a sme.h2.db into ./db

## Provides

- A WebSocketServer which can interrogate database and return JSON
- A WebSocketClient which provides a client API 
- bash scripts to interact with the API via CLI
- Reads from ./db/sme.h2.db

## Compiling

```bash
mvn install 
./compile.sh
```

## Running (server)

Launch the WebSocket server:
```bash
./server.sh
```

## Running (client)

All JSON output returned by client.sh can be piped into `jq` for readability

```bash
# Show available commands
./client.sh --help

# Get all portfolio groups
./client.sh portfolio_group.list

# Get all portfolios
./client.sh portfolio.list

# Get portfolio by id
./client.sh portfolio.get 1

# Get holdings by portfolio id
./client.sh portfolio.get_items 1

# List all database tables
./client.sh table.list
```

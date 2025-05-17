# OpenSME WebSocket Server

A proof of concept websocket server and client for an OpenSME rewrite.
Ultimately everything is subject to change, including the backend database and DB schema.

### Prerequisites
- Java 17+
- Maven 3.x
- H2 Database (embedded)

## Scripts
Several shell scripts are provided to help build and run the application:

- `compile.sh` - Builds all modules using Maven
- `run_server.sh` - Starts the WebSocket server on port 8025
- `run_client.sh` - Command line interface to interact with the server
- `frontend_run.sh` - Launches the Java Swing GUI

### Command Line Client
List all portfolios:
```bash
./run_client.sh portfolio.list
```

Get specific portfolio:
```bash
./run_client.sh portfolio.get <id>
```

Get portfolio items:
```bash
./run_client.sh portfolio.get_items <id>
```

List portfolio groups:
```bash
./run_client.sh portfolio_group.list
```

List database tables:
```bash
./run_client.sh table.list
```

## Features
- WebSocket server for real-time portfolio data
- Java Swing frontend for portfolio management
- Command line interface for quick access
- H2 database for persistent storage

## API Endpoints
- `portfolio.list` - List all portfolios
- `portfolio.get` - Get portfolio by ID
- `portfolio.get_items` - Get items in portfolio
- `portfolio_group.list` - List all portfolio groups
- `table.list` - List database tables

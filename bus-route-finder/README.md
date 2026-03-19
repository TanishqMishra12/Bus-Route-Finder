# Bhopal Bus Route Finder 🚌

A command-line Java application that models Bhopal's public bus network as a weighted directed graph and helps commuters find optimal routes between any two stops.

## Features

- **Shortest Route (BFS)** — Find the path with the fewest stops
- **Fastest Route (Dijkstra)** — Find the path with minimum travel time
- **Interchange Detection** — Automatically identifies where you need to switch buses
- **Stop Search** — Find stops using partial name matching
- **Route Lookup** — List all stops on a route or find all routes through a stop
- **Query Logging** — All searches are logged with timestamps

## Prerequisites

- Java 17 or higher
- Apache Maven 3.6+

## Build

```bash
cd bus-route-finder
mvn clean package
```

## Run

```bash
java -jar target/bus-route-finder-1.0-SNAPSHOT.jar
```

> **Note:** Run from the project root directory so the app can find `data/stops.csv` and `data/routes.csv`.

## Sample CLI Session

```
Loading bus network data...
Loaded 30 stops and 10 routes.

╔══════════════════════════════════════════╗
║      BHOPAL BUS ROUTE FINDER v1.0       ║
║   Navigate the city's bus network with   ║
║        ease — powered by graphs!         ║
╚══════════════════════════════════════════╝

┌──────────────────────────────────────────┐
│               MAIN MENU                  │
├──────────────────────────────────────────┤
│  1. Find route (fewest stops)     [BFS]  │
│  2. Find route (fastest time) [Dijkstra] │
│  3. List all stops on a route            │
│  4. Find routes through a stop           │
│  5. Search stop by name                  │
│  6. Exit                                 │
└──────────────────────────────────────────┘
Enter your choice: 1

── Find Route (Fewest Stops — BFS) ──

Enter source stop (name or ID): MP Nagar
Enter destination stop (name or ID): Arera Colony

✔ Route found! (3 stops)

  1. MP Nagar  [Route1]
  2. New Market  [Route1]
  3. Arera Colony  [Destination]
```

## Data Files

### `data/stops.csv`

| Column | Description | Example |
|--------|-------------|---------|
| stopId | Unique ID (S + 3 digits) | S001 |
| name | Stop display name | MP Nagar |
| zone | City zone | Central |

### `data/routes.csv`

| Column | Description | Example |
|--------|-------------|---------|
| routeNumber | Route identifier | Route1 |
| stopId | References stops.csv | S001 |
| sequence | Position on route (1-indexed) | 3 |
| travelTimeToNextStop | Minutes to next stop (0 for last) | 5 |

## Project Structure

```
bus-route-finder/
├── pom.xml
├── README.md
├── data/
│   ├── stops.csv
│   └── routes.csv
├── logs/                          (git-ignored)
│   └── search_history.log
└── src/main/java/com/busroute/
    ├── Main.java
    ├── model/     Stop, Edge, Route
    ├── graph/     BusGraph, GraphBuilder
    ├── algorithm/ BFSSolver, DijkstraSolver
    ├── service/   RouteService, SearchService
    ├── util/      CSVLoader, Logger
    └── cli/       MenuHandler
```

## Technology Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 17 LTS |
| Graph | Adjacency List (HashMap) |
| BFS | java.util.LinkedList (Queue) |
| Dijkstra | java.util.PriorityQueue |
| Data | CSV (BufferedReader) |
| Build | Apache Maven |
| CLI | java.util.Scanner |

Zero external dependencies — built entirely with the Java Standard Library.

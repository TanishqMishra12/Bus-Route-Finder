# Bhopal Bus Route Finder 🚌

A command-line Java application that models Bhopal's public bus network as a weighted directed graph and helps commuters find optimal routes between any two stops.

This project solves the real-world problem of navigating local transit **without internet connectivity** by pre-loading real BCLL routes and finding paths using **Breadth-First Search (BFS)** and **Dijkstra's algorithm**.

---

## 🌟 What This Project Does

| Feature | Description |
|---|---|
| **Fewest-Stop Routes (BFS)** | Path with the minimum number of bus stops between source and destination |
| **Fastest Routes (Dijkstra)** | Path with the minimum total travel time in minutes |
| **Interchange Detection** | Automatically flags stops where you must switch buses |
| **Stop Search** | Partial, case-insensitive name matching — type `"nar"` to find `"MP Nagar"` (min 3 characters) |
| **Route Lookup** | List all stops on a given route, or find all routes passing through a specific stop |
| **Query Logging** | Every search is saved with a timestamp to `logs/search_history.log` |

---

## 🛠️ Prerequisites

- **Java JDK 17 or newer** installed on your system
- The `data/` folder must be present with both `stops.csv` and `routes.csv` (included in this repository)
- No Maven, Gradle, or external libraries required — only the Java Standard Library

> **Check your Java version:** Run `java -version` in your terminal. You should see `17` or higher. If not, download JDK 17+ from [https://adoptium.net](https://adoptium.net).

---

## 🚀 How to Run

### Option 1 — Windows (One Click)

A `run.bat` script is included that compiles and runs everything automatically.

1. Open File Explorer and navigate to the project folder (`bus-route-finder/`)
2. Double-click `run.bat`
3. A terminal window will open, compile the code, and launch the application

### Option 2 — Command Line (Mac / Linux / Windows Terminal)

Open a terminal in the root of the project folder and run these two commands in order:

**Step 1 — Compile:**

```bash
# Mac / Linux
find src -name "*.java" | xargs javac -encoding UTF-8 -d target/classes

# Windows Command Prompt
for /r src %f in (*.java) do javac -encoding UTF-8 -d target\classes "%f"

# Windows PowerShell
Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { javac -encoding UTF-8 -d target/classes $_.FullName }
```

**Step 2 — Run:**

```bash
java -cp target/classes com.busroute.Main
```

> **Note:** The `target/classes` directory is created automatically during compilation. If you see a `cannot find or load main class` error, make sure you ran the compile step first from the project root.

---

## ❗ Troubleshooting

| Problem | Fix |
|---|---|
| `javac: command not found` or `'javac' is not recognized` | JDK is not installed or not added to your system PATH. Install JDK 17+ from [https://adoptium.net](https://adoptium.net) and restart your terminal. |
| `Error: Could not find or load main class com.busroute.Main` | You are not running the command from the project root, or compilation failed. Re-run the compile step from the `bus-route-finder/` folder. |
| `FileNotFoundException` for stops.csv or routes.csv | Run the application from the project root directory, not from inside `src/` or `target/`. The app expects `data/` to be a sibling of where you run the command. |
| Route not found between two valid stops | The dataset covers the main Bhopal corridors. Not all routes are included yet. See Dataset Coverage below. |

---

## 📖 Sample Usage

Once the application starts, you will see the main menu:

```
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
Enter your choice:
```

**Example — Finding a route with fewest stops (Option 1):**

```
── Find Route (Fewest Stops — BFS) ──

Enter source stop (name or ID): MP Nagar
Enter destination stop (name or ID): Arera Colony

✔ Route found! (3 stops)

  1. MP Nagar        [Route 1]
  2. New Market      [Route 1]  ← Interchange: switch to Route 3 here
  3. Arera Colony    [Destination]
```

**Example — Searching for a stop by partial name (Option 5):**

```
Enter at least 3 characters: hab

Matching stops:
  S009 — Habibganj
  S010 — Habibganj Railway Station
```

---

## 📂 Project Structure

```
bus-route-finder/
├── run.bat                         ← Windows one-click compile + run
├── README.md
├── data/
│   ├── stops.csv                   ← 25 Bhopal bus stops (ID, name, zone)
│   └── routes.csv                  ← Route definitions (route no., stop sequence, travel times)
├── logs/                           ← Created automatically on first search
│   └── search_history.log          ← Timestamped record of all queries
└── src/main/java/com/busroute/
    ├── Main.java                   ← Entry point
    ├── model/                      ← Stop, Edge, Route (data classes)
    ├── graph/                      ← BusGraph (adjacency list), GraphBuilder
    ├── algorithm/                  ← BFSSolver, DijkstraSolver (implement RouteSolver)
    ├── service/                    ← RouteService (orchestration), SearchService (name lookup)
    ├── util/                       ← CSVLoader, Logger
    └── cli/                        ← MenuHandler (all user interaction)
```

---

## 🗺️ Dataset Coverage

The dataset covers **25 stops across 10 routes**, focusing on the main BCLL corridors:

- MP Nagar ↔ New Market ↔ Arera Colony
- Karond ↔ Hamidia Road ↔ Nadra Bus Stand
- Habibganj ↔ AIIMS ↔ Kolar Road
- Board Office ↔ Bittan Market ↔ TT Nagar

Route and stop data was manually collected from physical boards at Nadra Bus Stand and Hamidia Road. This covers approximately 40% of Bhopal's active BCLL network and is sufficient for route-finding demonstrations across all major zones.

To add new stops or routes, edit `data/stops.csv` and `data/routes.csv` directly — no recompilation needed.

---

## ⚙️ How It Works (Technical Summary)

The bus network is modelled as a **weighted directed graph**:

- Each **stop** is a vertex with a unique ID (e.g., `S001`)
- Each **direct connection** between consecutive stops on a route is a directed edge
- Each edge carries a **travel time in minutes** as its weight
- The graph is stored as an **adjacency list** (`HashMap<String, List<Edge>>`) — chosen over a matrix because the network is sparse (each stop connects to a small subset of others)

**BFS** finds the path with the fewest stops by exploring the graph level by level on an unweighted version.

**Dijkstra's algorithm** finds the fastest path using a `PriorityQueue` (min-heap) ordered by cumulative travel time. Bellman-Ford was considered and rejected — travel times are always non-negative, and Dijkstra's O((V+E) log V) is significantly faster than Bellman-Ford's O(V×E).

Both solvers implement a common `RouteSolver` interface, allowing `RouteService` to call either interchangeably.

---

## 📝 Extending the Dataset

The application separates data from code entirely. To add a new stop or route:

**Adding a stop** — append a row to `data/stops.csv`:
```
S026,New Stop Name,Zone Name
```

**Adding a route connection** — append rows to `data/routes.csv`:
```
RouteNumber,StopID,SequencePosition,TravelTimeToNextStopInMinutes
```

Restart the application to load the updated data.
---

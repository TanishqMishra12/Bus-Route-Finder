# Bhopal Bus Route Finder 🚌

A command-line Java application that models Bhopal's public bus network as a weighted directed graph and helps commuters find optimal routes between any two stops. 

This project solves the real-world problem of navigating local transit without internet connectivity by pre-loading real routes and finding paths using the **Breadth-First Search (BFS)** and **Dijkstra's** algorithms.

---

## 🌟 What This Project Does

- **Find Least-Stop Routes (BFS)** — Discover the path with the fewest number of bus stops between your current location and destination.
- **Find Fastest Routes (Dijkstra)** — Discover the path with the absolute minimum travel time.
- **Interchange Detection** — The application automatically detects when you need to switch buses and prints out the exact interchange stop.
- **Stop Search** — Find stops effortlessly using partial name matching (e.g., typing "nar" will find "MP Nagar").
- **Route Lookup** — List all stops on a specific route or find all routes passing through a specific stop.
- **Query Logging** — All searches you make are saved with timestamps into a `logs/search_history.log` file.

---

## 🛠️ How to Set It Up

This project takes zero configuration and uses no external databases or libraries! Everything you need is built right into the **Java Standard Library**.

### Prerequisites
1. **Java JDK 17** (or newer) installed on your system.
2. Ensure your `data/stops.csv` and `data/routes.csv` files are present in the `data/` folder (these come included).

*(Note: There is no need to install Maven, Gradle, or any complex build tools to run this application!)*

---

## 🚀 How to Run and Use It

### On Windows (Easiest Method)
I have included a convenient batch file to compile and run the application for you in one single click.

1. Open your File Explorer and navigate to the project folder (`bus-route-finder`).
2. Double-click the **`run.bat`** file.
3. A terminal window will open, automatically compile the code using your installed Java compiler, and start the application menu.

### Via Command Line / Terminal (Mac / Linux / Windows)
If you prefer using the terminal directly, open your terminal in the root folder of this project and run the following commands sequentially:

**1. Compile the code:**
```bash
javac -encoding UTF-8 -d target/classes -sourcepath src/main/java src/main/java/com/busroute/Main.java src/main/java/com/busroute/**/*.java
```

**2. Run the application:**
```bash
java -cp target/classes com.busroute.Main
```

---

## 📖 Sample Usage Guide

Once the application starts, you'll be greeted with the Main Menu.

```text
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
```

From here, simply press **`1`** and hit **Enter** to find a route.

**Example interaction:**
```text
── Find Route (Fewest Stops — BFS) ──

Enter source stop (name or ID): MP Nagar
Enter destination stop (name or ID): Arera Colony

✔ Route found! (3 stops)

  1. MP Nagar  [Route1]
  2. New Market  [Route1]
  3. Arera Colony  [Destination]
```
*Notice how it prints out the exact route (`Route1`) you need to be on!*

---

## 📂 Project Structure

```
bus-route-finder/
├── run.bat                        (Windows 1-click execution script)
├── README.md                      (This documentation file)
├── data/
│   ├── stops.csv                  (Dataset of 30+ bus stops)
│   └── routes.csv                 (Dataset of connected bus routes)
├── logs/                          (Generated automatically at runtime)
│   └── search_history.log         (Timestamped search history)
└── src/main/java/com/busroute/    (Java Source Code)
    ├── Main.java                  (Entry point)
    ├── model/                     (Stop, Edge, Route classes)
    ├── graph/                     (BusGraph, GraphBuilder)
    ├── algorithm/                 (BFSSolver, DijkstraSolver)
    ├── service/                   (RouteService, SearchService)
    ├── util/                      (CSVLoader, Logger)
    └── cli/                       (Interactive MenuHandler interface)
```

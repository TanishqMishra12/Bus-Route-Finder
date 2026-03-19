package com.busroute.cli;

import com.busroute.algorithm.DijkstraSolver;
import com.busroute.model.Route;
import com.busroute.model.Stop;
import com.busroute.service.RouteService;
import com.busroute.service.SearchService;
import com.busroute.util.Logger;

import java.util.List;
import java.util.Scanner;

public class MenuHandler {
    private RouteService routeService;
    private SearchService searchService;
    private Scanner scanner;

    public MenuHandler(RouteService routeService, SearchService searchService) {
        this.routeService = routeService;
        this.searchService = searchService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║      BHOPAL BUS ROUTE FINDER v1.0       ║");
        System.out.println("║   Navigate the city's bus network with   ║");
        System.out.println("║        ease — powered by graphs!         ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleBFS();
                case "2" -> handleDijkstra();
                case "3" -> handleListStopsOnRoute();
                case "4" -> handleRoutesThrough();
                case "5" -> handleSearch();
                case "6" -> {
                    System.out.println("\nThank you for using Bhopal Bus Route Finder. Safe travels!");
                    running = false;
                }
                default -> System.out.println("\n⚠ Invalid choice. Please enter a number between 1 and 6.\n");
            }
        }
    }

    private void printMenu() {
        System.out.println("┌──────────────────────────────────────────┐");
        System.out.println("│               MAIN MENU                  │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  1. Find route (fewest stops)     [BFS]  │");
        System.out.println("│  2. Find route (fastest time) [Dijkstra] │");
        System.out.println("│  3. List all stops on a route            │");
        System.out.println("│  4. Find routes through a stop           │");
        System.out.println("│  5. Search stop by name                  │");
        System.out.println("│  6. Exit                                 │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print("Enter your choice: ");
    }

    private Stop promptAndResolveStop(String label) {
        System.out.print("Enter " + label + " (name or ID): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("⚠ Input cannot be empty.");
            return null;
        }

        Stop stop = searchService.resolveStop(input);
        if (stop != null) return stop;

        List<Stop> matches = searchService.searchByName(input);
        if (matches.isEmpty()) {
            System.out.println("⚠ No stop found matching: " + input);
            return null;
        }

        System.out.println("\nMultiple matches found — please select:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + matches.get(i));
        }
        System.out.print("Enter number: ");

        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < matches.size()) {
                return matches.get(idx);
            }
        } catch (NumberFormatException ignored) {}

        System.out.println("⚠ Invalid selection.");
        return null;
    }

    private void handleBFS() {
        System.out.println("\n── Find Route (Fewest Stops — BFS) ──\n");

        Stop source = promptAndResolveStop("source stop");
        if (source == null) { System.out.println(); return; }

        Stop dest = promptAndResolveStop("destination stop");
        if (dest == null) { System.out.println(); return; }

        List<Stop> path = routeService.findShortestPath(source.getStopId(), dest.getStopId());

        if (path.isEmpty()) {
            System.out.println("\n✘ No route found from " + source.getName() + " to " + dest.getName() + ".\n");
            Logger.log("BFS | " + source.getName() + " -> " + dest.getName() + " | No route found");
            return;
        }

        System.out.println("\n✔ Route found! (" + path.size() + " stops)\n");
        printPathWithRoutes(path);

        Logger.log("BFS | " + source.getName() + " -> " + dest.getName() + " | " + path.size() + " stops");
        System.out.println();
    }

    private void handleDijkstra() {
        System.out.println("\n── Find Route (Fastest Time — Dijkstra) ──\n");

        Stop source = promptAndResolveStop("source stop");
        if (source == null) { System.out.println(); return; }

        Stop dest = promptAndResolveStop("destination stop");
        if (dest == null) { System.out.println(); return; }

        DijkstraSolver.PathResult result = routeService.findFastestPath(source.getStopId(), dest.getStopId());

        if (result.getPath().isEmpty()) {
            System.out.println("\n✘ No route found from " + source.getName() + " to " + dest.getName() + ".\n");
            Logger.log("Dijkstra | " + source.getName() + " -> " + dest.getName() + " | No route found");
            return;
        }

        List<Stop> path = result.getPath();
        System.out.println("\n✔ Fastest route found! (Total time: " + result.getTotalTime() + " minutes)\n");

        String prevRoute = null;
        for (int i = 0; i < path.size(); i++) {
            Stop stop = path.get(i);
            if (i < path.size() - 1) {
                String route = result.getRouteForEdge(stop.getStopId(), path.get(i + 1).getStopId());
                if (prevRoute != null && !route.equals(prevRoute)) {
                    System.out.println("     🔄 INTERCHANGE at " + stop.getName() + " — switch to " + route);
                }
                System.out.println("  " + (i + 1) + ". " + stop.getName() + "  [" + route + "]");
                prevRoute = route;
            } else {
                System.out.println("  " + (i + 1) + ". " + stop.getName() + "  [Destination]");
            }
        }

        Logger.log("Dijkstra | " + source.getName() + " -> " + dest.getName()
                + " | " + path.size() + " stops, " + result.getTotalTime() + " min");
        System.out.println();
    }

    private void printPathWithRoutes(List<Stop> path) {
        String prevRoute = null;
        for (int i = 0; i < path.size(); i++) {
            Stop stop = path.get(i);
            if (i < path.size() - 1) {
                String route = routeService.getRouteOnEdge(stop.getStopId(), path.get(i + 1).getStopId());
                if (prevRoute != null && !route.equals(prevRoute)) {
                    System.out.println("     🔄 INTERCHANGE at " + stop.getName() + " — switch to " + route);
                }
                System.out.println("  " + (i + 1) + ". " + stop.getName() + "  [" + route + "]");
                prevRoute = route;
            } else {
                System.out.println("  " + (i + 1) + ". " + stop.getName() + "  [Destination]");
            }
        }
    }

    private void handleListStopsOnRoute() {
        System.out.println("\n── List Stops on a Route ──\n");
        System.out.println("Available routes:");
        for (Route route : routeService.getAllRoutes()) {
            System.out.println("  • " + route);
        }
        System.out.print("\nEnter route number (e.g., Route1): ");
        String routeNum = scanner.nextLine().trim();

        Route route = routeService.getRouteByNumber(routeNum);
        if (route == null) {
            System.out.println("⚠ Route not found: " + routeNum + "\n");
            return;
        }

        System.out.println("\n" + route.getRouteNumber() + ": " + route.getStartTerminus() + " → " + route.getEndTerminus() + "\n");
        List<Stop> stops = route.getStopSequence();
        for (int i = 0; i < stops.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + stops.get(i).getName() + " (" + stops.get(i).getZone() + ")");
        }
        System.out.println();
    }

    private void handleRoutesThrough() {
        System.out.println("\n── Find Routes Through a Stop ──\n");

        Stop stop = promptAndResolveStop("stop");
        if (stop == null) { System.out.println(); return; }

        List<String> routes = routeService.findRoutesThrough(stop.getStopId());
        if (routes.isEmpty()) {
            System.out.println("⚠ No routes serve this stop.\n");
            return;
        }

        System.out.println("\nRoutes through " + stop.getName() + ":");
        for (String r : routes) {
            System.out.println("  • " + r);
        }
        System.out.println();
    }

    private void handleSearch() {
        System.out.println("\n── Search Stop by Name ──\n");
        System.out.print("Enter search term (min 3 characters): ");
        String query = scanner.nextLine().trim();

        if (query.length() < 3) {
            System.out.println("⚠ Please enter at least 3 characters.\n");
            return;
        }

        List<Stop> results = searchService.searchByName(query);
        if (results.isEmpty()) {
            System.out.println("No stops found matching: " + query + "\n");
            return;
        }

        System.out.println("\nMatching stops:");
        for (Stop stop : results) {
            System.out.println("  • " + stop);
        }
        if (results.size() == 10) {
            System.out.println("  (showing first 10 results — try a more specific query)");
        }
        System.out.println();
    }
}

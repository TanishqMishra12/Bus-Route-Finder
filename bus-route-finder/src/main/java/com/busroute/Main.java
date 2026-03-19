package com.busroute;

import com.busroute.cli.MenuHandler;
import com.busroute.graph.BusGraph;
import com.busroute.graph.GraphBuilder;
import com.busroute.model.Route;
import com.busroute.model.Stop;
import com.busroute.service.RouteService;
import com.busroute.service.SearchService;
import com.busroute.util.CSVLoader;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String stopsFile = "data/stops.csv";
        String routesFile = "data/routes.csv";

        System.out.println("Loading bus network data...");
        List<Stop> stops = CSVLoader.loadStops(stopsFile);
        List<GraphBuilder.RouteRow> routeRows = CSVLoader.loadRouteData(routesFile);

        if (stops.isEmpty() || routeRows.isEmpty()) {
            System.err.println("Failed to load data. Ensure data/stops.csv and data/routes.csv exist.");
            return;
        }

        BusGraph graph = new BusGraph();
        List<Route> routes = GraphBuilder.buildGraph(graph, stops, routeRows);

        System.out.println("Loaded " + stops.size() + " stops and " + routes.size() + " routes.\n");

        RouteService routeService = new RouteService(graph, routes);
        SearchService searchService = new SearchService(graph);
        MenuHandler menu = new MenuHandler(routeService, searchService);
        menu.start();
    }
}

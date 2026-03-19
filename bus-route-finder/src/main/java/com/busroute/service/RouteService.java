package com.busroute.service;

import com.busroute.algorithm.BFSSolver;
import com.busroute.algorithm.DijkstraSolver;
import com.busroute.graph.BusGraph;
import com.busroute.model.Edge;
import com.busroute.model.Route;
import com.busroute.model.Stop;

import java.util.*;

public class RouteService {
    private BusGraph graph;
    private List<Route> routes;
    private BFSSolver bfsSolver;
    private DijkstraSolver dijkstraSolver;

    public RouteService(BusGraph graph, List<Route> routes) {
        this.graph = graph;
        this.routes = routes;
        this.bfsSolver = new BFSSolver();
        this.dijkstraSolver = new DijkstraSolver();
    }

    public List<Stop> findShortestPath(String sourceId, String destId) {
        return bfsSolver.findShortestPath(graph, sourceId, destId);
    }

    public DijkstraSolver.PathResult findFastestPath(String sourceId, String destId) {
        return dijkstraSolver.findFastestPath(graph, sourceId, destId);
    }

    public Route getRouteByNumber(String routeNumber) {
        for (Route route : routes) {
            if (route.getRouteNumber().equalsIgnoreCase(routeNumber)) {
                return route;
            }
        }
        return null;
    }

    public List<String> findRoutesThrough(String stopId) {
        List<String> result = new ArrayList<>();
        for (Route route : routes) {
            for (Stop stop : route.getStopSequence()) {
                if (stop.getStopId().equals(stopId)) {
                    result.add(route.getRouteNumber());
                    break;
                }
            }
        }
        return result;
    }

    public String getRouteOnEdge(String fromId, String toId) {
        for (Edge edge : graph.getNeighbors(fromId)) {
            if (edge.getDestination().getStopId().equals(toId)) {
                return edge.getRouteNumber();
            }
        }
        return "Unknown";
    }

    public List<Route> getAllRoutes() {
        return routes;
    }
}

package com.busroute.graph;

import com.busroute.model.Edge;
import com.busroute.model.Stop;

import java.util.*;

public class BusGraph {
    private Map<String, List<Edge>> adjacencyList = new HashMap<>();
    private Map<String, Stop> stopRegistry = new HashMap<>();

    public void addStop(Stop stop) {
        stopRegistry.put(stop.getStopId(), stop);
        adjacencyList.putIfAbsent(stop.getStopId(), new ArrayList<>());
    }

    public void addEdge(String fromId, Edge edge) {
        adjacencyList.computeIfAbsent(fromId, k -> new ArrayList<>()).add(edge);
    }

    public List<Edge> getNeighbors(String stopId) {
        return adjacencyList.getOrDefault(stopId, Collections.emptyList());
    }

    public Stop getStop(String stopId) {
        return stopRegistry.get(stopId);
    }

    public Set<String> getAllStopIds() {
        return stopRegistry.keySet();
    }

    public boolean stopExists(String stopId) {
        return stopRegistry.containsKey(stopId);
    }

    public Collection<Stop> getAllStops() {
        return stopRegistry.values();
    }
}

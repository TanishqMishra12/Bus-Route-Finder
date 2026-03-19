package com.busroute.service;

import com.busroute.graph.BusGraph;
import com.busroute.model.Stop;

import java.util.ArrayList;
import java.util.List;

public class SearchService {
    private BusGraph graph;

    public SearchService(BusGraph graph) {
        this.graph = graph;
    }

    public List<Stop> searchByName(String query) {
        if (query == null || query.trim().length() < 3) {
            return new ArrayList<>();
        }

        String lowerQuery = query.trim().toLowerCase();
        List<Stop> results = new ArrayList<>();

        for (Stop stop : graph.getAllStops()) {
            if (stop.getName().toLowerCase().contains(lowerQuery)) {
                results.add(stop);
                if (results.size() >= 10) break;
            }
        }

        return results;
    }

    public Stop resolveStop(String input) {
        for (Stop stop : graph.getAllStops()) {
            if (stop.getStopId().equalsIgnoreCase(input.trim())) {
                return stop;
            }
        }
        List<Stop> matches = searchByName(input);
        if (matches.size() == 1) {
            return matches.get(0);
        }
        return null;
    }
}

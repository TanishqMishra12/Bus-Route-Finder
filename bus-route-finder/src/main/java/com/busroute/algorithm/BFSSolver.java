package com.busroute.algorithm;

import com.busroute.graph.BusGraph;
import com.busroute.model.Edge;
import com.busroute.model.Stop;

import java.util.*;

public class BFSSolver {

    public List<Stop> findShortestPath(BusGraph graph, String sourceId, String destId) {
        if (!graph.stopExists(sourceId) || !graph.stopExists(destId)) {
            return Collections.emptyList();
        }
        if (sourceId.equals(destId)) {
            return List.of(graph.getStop(sourceId));
        }

        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        visited.add(sourceId);
        queue.add(sourceId);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            for (Edge edge : graph.getNeighbors(current)) {
                String neighborId = edge.getDestination().getStopId();
                if (!visited.contains(neighborId)) {
                    visited.add(neighborId);
                    parentMap.put(neighborId, current);

                    if (neighborId.equals(destId)) {
                        return reconstructPath(graph, parentMap, sourceId, destId);
                    }

                    queue.add(neighborId);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Stop> reconstructPath(BusGraph graph, Map<String, String> parentMap, String sourceId, String destId) {
        LinkedList<Stop> path = new LinkedList<>();
        String current = destId;

        while (current != null) {
            path.addFirst(graph.getStop(current));
            if (current.equals(sourceId)) break;
            current = parentMap.get(current);
        }

        return path;
    }
}

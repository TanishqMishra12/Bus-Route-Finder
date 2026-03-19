package com.busroute.algorithm;

import com.busroute.graph.BusGraph;
import com.busroute.model.Edge;
import com.busroute.model.Stop;

import java.util.*;

public class DijkstraSolver {

    public static class PathResult {
        private List<Stop> path;
        private int totalTime;
        private Map<String, String> edgeRouteMap;

        public PathResult(List<Stop> path, int totalTime, Map<String, String> edgeRouteMap) {
            this.path = path;
            this.totalTime = totalTime;
            this.edgeRouteMap = edgeRouteMap;
        }

        public List<Stop> getPath() { return path; }
        public int getTotalTime() { return totalTime; }

        public String getRouteForEdge(String fromId, String toId) {
            return edgeRouteMap.getOrDefault(fromId + "->" + toId, "Unknown");
        }
    }

    public PathResult findFastestPath(BusGraph graph, String sourceId, String destId) {
        if (!graph.stopExists(sourceId) || !graph.stopExists(destId)) {
            return new PathResult(Collections.emptyList(), 0, Collections.emptyMap());
        }
        if (sourceId.equals(destId)) {
            return new PathResult(List.of(graph.getStop(sourceId)), 0, Collections.emptyMap());
        }

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parentMap = new HashMap<>();
        Map<String, String> edgeRouteMap = new HashMap<>();
        PriorityQueue<String[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> Integer.parseInt(a[1])));

        for (String stopId : graph.getAllStopIds()) {
            dist.put(stopId, Integer.MAX_VALUE);
        }
        dist.put(sourceId, 0);
        pq.offer(new String[]{sourceId, "0"});

        Set<String> settled = new HashSet<>();

        while (!pq.isEmpty()) {
            String[] top = pq.poll();
            String currentId = top[0];
            int currentDist = Integer.parseInt(top[1]);

            if (settled.contains(currentId)) continue;
            settled.add(currentId);

            if (currentId.equals(destId)) break;

            for (Edge edge : graph.getNeighbors(currentId)) {
                String neighborId = edge.getDestination().getStopId();
                int newDist = currentDist + edge.getTravelTimeMinutes();

                if (newDist < dist.get(neighborId)) {
                    dist.put(neighborId, newDist);
                    parentMap.put(neighborId, currentId);
                    edgeRouteMap.put(currentId + "->" + neighborId, edge.getRouteNumber());
                    pq.offer(new String[]{neighborId, String.valueOf(newDist)});
                }
            }
        }

        if (!parentMap.containsKey(destId) && !sourceId.equals(destId)) {
            return new PathResult(Collections.emptyList(), 0, Collections.emptyMap());
        }

        LinkedList<Stop> path = new LinkedList<>();
        String current = destId;
        while (current != null) {
            path.addFirst(graph.getStop(current));
            if (current.equals(sourceId)) break;
            current = parentMap.get(current);
        }

        return new PathResult(path, dist.get(destId), edgeRouteMap);
    }
}

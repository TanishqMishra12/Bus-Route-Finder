package com.busroute.graph;

import com.busroute.model.Edge;
import com.busroute.model.Route;
import com.busroute.model.Stop;

import java.util.*;

public class GraphBuilder {

    public static class RouteRow {
        public String routeNumber;
        public String stopId;
        public int sequence;
        public int travelTimeToNextStop;

        public RouteRow(String routeNumber, String stopId, int sequence, int travelTimeToNextStop) {
            this.routeNumber = routeNumber;
            this.stopId = stopId;
            this.sequence = sequence;
            this.travelTimeToNextStop = travelTimeToNextStop;
        }
    }

    public static List<Route> buildGraph(BusGraph graph, List<Stop> stops, List<RouteRow> routeRows) {
        for (Stop stop : stops) {
            graph.addStop(stop);
        }

        Map<String, List<RouteRow>> grouped = new LinkedHashMap<>();
        for (RouteRow row : routeRows) {
            grouped.computeIfAbsent(row.routeNumber, k -> new ArrayList<>()).add(row);
        }

        List<Route> routes = new ArrayList<>();

        for (Map.Entry<String, List<RouteRow>> entry : grouped.entrySet()) {
            String routeNumber = entry.getKey();
            List<RouteRow> rows = entry.getValue();
            rows.sort(Comparator.comparingInt(r -> r.sequence));

            List<Stop> stopSequence = new ArrayList<>();
            for (RouteRow row : rows) {
                Stop stop = graph.getStop(row.stopId);
                if (stop != null) {
                    stopSequence.add(stop);
                }
            }

            for (int i = 0; i < rows.size() - 1; i++) {
                RouteRow current = rows.get(i);
                RouteRow next = rows.get(i + 1);
                Stop destStop = graph.getStop(next.stopId);
                if (destStop != null && current.travelTimeToNextStop > 0) {
                    Edge edge = new Edge(destStop, routeNumber, current.travelTimeToNextStop);
                    graph.addEdge(current.stopId, edge);
                }
            }

            if (!stopSequence.isEmpty()) {
                String start = stopSequence.get(0).getName();
                String end = stopSequence.get(stopSequence.size() - 1).getName();
                routes.add(new Route(routeNumber, stopSequence, start, end));
            }
        }

        return routes;
    }
}

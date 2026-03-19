package com.busroute.util;

import com.busroute.graph.GraphBuilder;
import com.busroute.model.Stop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {

    public static List<Stop> loadStops(String filePath) {
        List<Stop> stops = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 3) {
                        stops.add(new Stop(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                    }
                } catch (Exception e) {
                    System.err.println("Skipping malformed stop row: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading stops file: " + e.getMessage());
        }
        return stops;
    }

    public static List<GraphBuilder.RouteRow> loadRouteData(String filePath) {
        List<GraphBuilder.RouteRow> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 4) {
                        String routeNumber = parts[0].trim();
                        String stopId = parts[1].trim();
                        int sequence = Integer.parseInt(parts[2].trim());
                        int travelTime = Integer.parseInt(parts[3].trim());
                        rows.add(new GraphBuilder.RouteRow(routeNumber, stopId, sequence, travelTime));
                    }
                } catch (Exception e) {
                    System.err.println("Skipping malformed route row: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading routes file: " + e.getMessage());
        }
        return rows;
    }
}

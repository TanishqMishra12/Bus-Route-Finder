package com.busroute.model;

public class Edge {
    private Stop destination;
    private String routeNumber;
    private int travelTimeMinutes;

    public Edge(Stop destination, String routeNumber, int travelTimeMinutes) {
        this.destination = destination;
        this.routeNumber = routeNumber;
        this.travelTimeMinutes = travelTimeMinutes;
    }

    public Stop getDestination() { return destination; }
    public String getRouteNumber() { return routeNumber; }
    public int getTravelTimeMinutes() { return travelTimeMinutes; }

    @Override
    public String toString() {
        return "-> " + destination.getName() + " via " + routeNumber + " (" + travelTimeMinutes + " min)";
    }
}

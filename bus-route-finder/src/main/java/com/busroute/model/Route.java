package com.busroute.model;

import java.util.List;

public class Route {
    private String routeNumber;
    private List<Stop> stopSequence;
    private String startTerminus;
    private String endTerminus;

    public Route(String routeNumber, List<Stop> stopSequence, String startTerminus, String endTerminus) {
        this.routeNumber = routeNumber;
        this.stopSequence = stopSequence;
        this.startTerminus = startTerminus;
        this.endTerminus = endTerminus;
    }

    public String getRouteNumber() { return routeNumber; }
    public List<Stop> getStopSequence() { return stopSequence; }
    public String getStartTerminus() { return startTerminus; }
    public String getEndTerminus() { return endTerminus; }

    @Override
    public String toString() {
        return routeNumber + " (" + startTerminus + " → " + endTerminus + ")";
    }
}

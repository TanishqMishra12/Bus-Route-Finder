package com.busroute.model;

public class Stop {
    private String stopId;
    private String name;
    private String zone;

    public Stop(String stopId, String name, String zone) {
        this.stopId = stopId;
        this.name = name;
        this.zone = zone;
    }

    public String getStopId() { return stopId; }
    public String getName() { return name; }
    public String getZone() { return zone; }

    @Override
    public String toString() {
        return name + " (" + stopId + ", " + zone + ")";
    }
}

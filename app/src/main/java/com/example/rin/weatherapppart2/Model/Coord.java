package com.example.rin.weatherapppart2.Model;

public class Coord {
    private double last;
    private double lon;

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Coord(double last, double lon) {
        this.last = last;
        this.lon = lon;
    }
}
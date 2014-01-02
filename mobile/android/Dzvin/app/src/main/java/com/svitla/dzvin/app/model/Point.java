package com.svitla.dzvin.app.model;

/**
 * Created by slelyuk on 12/23/13.
 */
public class Point {
    private Float lat;
    private Float lon;
    private String title;

    public Point() {
    }

    public Point(Float lat, Float lon, String title) {
        this.lat = lat;
        this.lon = lon;
        this.title = title;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        String s = "lat=" + lat + ", lon=" + lon + ",title=" + title;
        return s;
    }
}

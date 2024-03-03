package com.example.hw2.Model;


import com.google.android.gms.maps.model.Marker;

public class User implements Comparable<User> {
    private int score;
    private double lat;
    private double lon;
    private Marker marker;

    public User(){
        this.score = 0;
        this.lat = 0.0d;
        this.lon = 0.0d;
        this.marker=null;
    }

    public int getScore() {
        return score;
    }

    public User addToScore(int score) {
        this.score += score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public User setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public User setLon(double lon) {
        this.lon = lon;
        return this;
    }
    public User setMarker(Marker marker){
        this.marker = marker;
        return this;
    }

    @Override
    public int compareTo(User o) {
        if(this.score > o.score)
            return -1;
        if(this.score < o.score)
            return 1;
        return 0;
    }
}

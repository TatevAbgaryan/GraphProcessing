package com.example.tatevabgaryan.graphprocessing.model;

import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 2/10/2018.
 */

public class Island {
    TreeSet<Point> points;

    public Island(TreeSet<Point> points) {
        this.points = points;
    }

    public TreeSet<Point> getPoints() {
        return points;
    }

    public void setPoints(TreeSet<Point> points) {
        this.points = points;
    }
}
package com.example.tatevabgaryan.graphprocessing.model;

import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;

import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 2/10/2018.
 */

public class Island {
    private TreeSet<Point> points;
    private boolean isGraph;
    private int value;

    public Island(){
        points = new TreeSet<>(new PointComparator());
    }

    public Island(TreeSet<Point> points) {
        this.points = points;
    }

    public TreeSet<Point> getPoints() {
        return points;
    }

    public void setPoints(TreeSet<Point> points) {
        this.points = points;
    }

    public boolean isGraph() {
        return isGraph;
    }

    public void setGraph(boolean graph) {
        isGraph = graph;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
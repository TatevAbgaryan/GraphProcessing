package com.example.tatevabgaryan.graphprocessing.model;

import android.graphics.Color;

import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 2/10/2018.
 */

public class Island {
    private TreeSet<Point> points;
    private boolean isGraph;
    private int value;
    private int[] pixels;
    private int minX = Integer.MAX_VALUE, maxX = 0, minY = Integer.MAX_VALUE, maxY = 0;

    public Island(){
        points = new TreeSet<>(new PointComparator());
        pixels = new int[BitmapContext.getWidth() * BitmapContext.getHeight()];
        Arrays.fill(pixels, Color.rgb(255, 255, 255));
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

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }
}
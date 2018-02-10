package com.example.tatevabgaryan.graphprocessing.model;

import android.graphics.Color;

/**
 * Created by Tatev.Abgaryan on 1/28/2018.
 */

public class Point {
    private int x;
    private int y;
    private Color color;

    public Point() {

    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDistanceFromOrigin(){
       return  (this.getX() * this.getX() + this.getY() * this.getY());
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point point = (Point) o;

        if (getX() != point.getX()) return false;
        if (getY() != point.getY()) return false;
        return getColor() != null ? getColor().equals(point.getColor()) : point.getColor() == null;
    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        result = 31 * result + (getColor() != null ? getColor().hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                '}';
    }
}

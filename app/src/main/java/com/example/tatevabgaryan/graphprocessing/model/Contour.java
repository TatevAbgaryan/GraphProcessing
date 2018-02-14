package com.example.tatevabgaryan.graphprocessing.model;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 2/11/2018.
 */

public class Contour {
    private TreeSet<Point> points;
    private int[][] matrix;
    private int rowSize;
    private int columnSize;

    public int getRowSize() {
        return rowSize;
    }

    public void setRowSize(int rowSize) {
        this.rowSize = rowSize;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public TreeSet<Point> getPoints() {
        return points;
    }

    public void setPoints(TreeSet<Point> points) {
        this.points = points;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }
}

package com.example.tatevabgaryan.graphprocessing.comparator;

import com.example.tatevabgaryan.graphprocessing.helper.BitmapHelper;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.Comparator;

/**
 * Created by Tatev.Abgaryan on 2/11/2018.
 */

public class PointComparator implements Comparator<Point> {
    @Override
    public int compare(Point p1, Point p2) {
        int diff = p1.getDistanceFromOrigin() - p2.getDistanceFromOrigin();
        if (diff == 0) {
            return p1.getX() - p2.getX();
        }
        return diff;
    }
}

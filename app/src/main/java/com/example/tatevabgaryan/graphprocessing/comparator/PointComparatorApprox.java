package com.example.tatevabgaryan.graphprocessing.comparator;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.Comparator;

public class PointComparatorApprox implements Comparator<Point> {
        @Override
        public int compare(Point p1, Point p2) {
            int diff = p1.getDistanceFromOrigin() - p2.getDistanceFromOrigin();
            if (Math.abs(diff) < MainActivity.APPROXIMATION_RADIUS) {
               return 0;
            }
            return diff;
        }
    }
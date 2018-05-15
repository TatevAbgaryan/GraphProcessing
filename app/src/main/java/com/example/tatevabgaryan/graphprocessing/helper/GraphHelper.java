package com.example.tatevabgaryan.graphprocessing.helper;

import android.content.Context;
import android.util.Log;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.model.Edge;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 2/25/2018.
 */

public class GraphHelper {

    public boolean isEdge(final List<Point> n1, final List<Point> n2, final TreeSet<Point> contour) {
        for (Point p1 : n1) {
            for (Point p2 : n2) {
                boolean smallerX = p1.getX() < p2.getX();
                final int x1 = smallerX ? p1.getX() : p2.getX(), x2 = smallerX ? p2.getX() : p1.getX(),
                        y1 = smallerX ? p1.getY() : p2.getY(),
                        y2 = smallerX ? p2.getY() : p1.getY();
                boolean foundY = false;
                for (int i = x1 + 1; i <= x2 - 1; i++) {
                    double y = y2 - (double) ((x2 - i) * (y2 - y1)) / (x2 - x1);
                    if (isInteger(y)) {
                        foundY = true;
                        if (!containsApprox(contour, new Point(i, (int) y)))
                            return false;
                    }
                    if (foundY && i == x2 - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean containsApprox(TreeSet<Point> contour, Point linePoint) {
        for (Point p : contour) {
            if (getDistanceOfPoints(p, linePoint) < 10) {
                return true;
            }
        }
        return false;
    }

    private boolean isInteger(double y) {
        if ((y == Math.floor(y)) && !Double.isInfinite(y)) {
            return true;
        }
        return false;
    }

    public List<TreeSet<Point>> groupPointsOfEachNode(TreeSet<Point> nodes, List<Island> islands) {
        List<TreeSet<Point>> grouped = new ArrayList<>();
        TreeSet<Point> pointsOfEachNode = new TreeSet<>(new PointComparator());
        Point previousPoint = null;
        for (Point p : nodes) {
            if (previousPoint == null) {
                pointsOfEachNode.add(p);
            } else {
                if (getDistanceOfPoints(previousPoint, p) > MainActivity.NODE_POINT_DISTANCE) {
                    grouped.add(pointsOfEachNode);
                    pointsOfEachNode = new TreeSet<>(new PointComparator());
                    pointsOfEachNode.add(p);
                } else {
                    pointsOfEachNode.add(p);
                }
            }
            previousPoint = p;
        }
        grouped.add(pointsOfEachNode);

        List<TreeSet<Point>> toRemove = new ArrayList<>();
        for (int i = 0; i < grouped.size(); i++) {
            for (int j = i + 1; j < grouped.size(); j++) {
                if (getDistanceOfPoints(grouped.get(i).first(), grouped.get(j).first()) < MainActivity.NODE_POINT_DISTANCE) {
                    grouped.get(i).addAll(grouped.get(j));
                    toRemove.add(grouped.get(j));
                }
            }
        }

        grouped.removeAll(toRemove);
        return grouped;
    }

    public int getDistanceOfPoints(Point p1, Point p2) {
        return (int) (Math.sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY())));
    }

    public boolean isNode(Point p, TreeSet<Point> contour) {
        int rowNbr[] = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
        int colNbr[] = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 1; i <= MainActivity.NODE_RADIUS; i++) {
            for (int j = 0; j < rowNbr.length; j++) {
                Point checkP = new Point(p.getX() + i * rowNbr[j], p.getY() + i * colNbr[j]);
                if (!contour.contains(checkP)) {
                    return false;
                }
            }
        }
        return true;
    }

    public double getDistanceOfNumberFromEdge(Island island, Edge edge, Graph graph) {

        int midPX = (graph.getNodes().get(edge.getStartNode()).first().getX() + graph.getNodes().get(edge.getEndNode()).first().getX()) / 2;
        int midPY = (graph.getNodes().get(edge.getStartNode()).first().getY() + graph.getNodes().get(edge.getEndNode()).first().getY()) / 2;
        Point edgeMidPoint = new Point(midPX, midPY);
        double minDistance = Double.MAX_VALUE;
        double currentDistance;
        for (Point p : island.getPoints()) {
            if ((currentDistance = getDistanceOfPoints(p, edgeMidPoint)) < minDistance) {
                minDistance = currentDistance;
            }
        }
        return minDistance;
    }

    public void numerateIslands(List<Island> islands) {
        final OCRHelper ocrHelper = new OCRHelper();
        final BitmapHelper bitmapHelper = new BitmapHelper();

        for (final Island island : islands) {
            if (!island.isGraph()) {
                try {
                    int number = ocrHelper.numberFromBitmap(bitmapHelper.createNumberBitmapFromIsland(island));
                    island.setValue(number != 0 ? number : 2);
                    Log.d("islandValue", island.getValue() + "");
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public int findNearestNode(Graph graph, Point touchPoint) {
        double minDistance = Double.MAX_VALUE;
        double current;
        int nearestNodeIndex = -1;
        for (int i = 0; i < graph.getNodes().size(); i++) {
            if (graph.getNodes().get(i).isEmpty()) continue;
            if ((current = getDistanceOfPoints(graph.getNodes().get(i).first(), touchPoint)) < minDistance) {
                minDistance = current;
                nearestNodeIndex = i;
            }
        }
        return nearestNodeIndex;
    }
}

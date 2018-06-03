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
            if (getDistanceOfPoints(p, linePoint) < 3) {
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
        double minDistance = Double.MAX_VALUE;
        double currentDistance;
        ArrayList<Point> nodePointes = new ArrayList();
        nodePointes.addAll(graph.getNodes().get(edge.getStartNode()));
        Point P1 = nodePointes.get(nodePointes.size() / 2);
        nodePointes = new ArrayList<>();
        nodePointes.addAll(graph.getNodes().get(edge.getEndNode()));
        Point P2 = nodePointes.get(nodePointes.size() / 2);

        for (Point p : island.getPoints()) {
            if ((currentDistance = getPointDistanceFromLine(P1, P2, p)) <= minDistance) {
                minDistance = currentDistance;
            }
        }

        return minDistance;
    }

    private double getPointDistanceFromLine(Point p1, Point p2, Point islandPoint) {
        int x0 = islandPoint.getX();
        int y0 = islandPoint.getY();
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();
        double h = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1) / Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));

        //check if height is out og line return max
        double k1 = getDistanceOfPoints(p1, islandPoint);
        double k2 = getDistanceOfPoints(p2, islandPoint);
        double k = getDistanceOfPoints(p1, p2);
        if(Math.sqrt(k1*k1 - h*h) + Math.sqrt(k2*k2 - h*h) > k)
            return Double.MAX_VALUE;
        return h;
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

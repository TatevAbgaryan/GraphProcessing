package com.example.tatevabgaryan.graphprocessing.helper;

import android.content.Context;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.model.Edge;
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

    public List<TreeSet<Point>> groupPointsOfEachNode(TreeSet<Point> nodes) {
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

    public List<Island> getNumberIslands(List<Island> islands) {
        int graphIslandIndex = 0;
        int maxCountOFPoints = 0;
        for (int i = 0; i < islands.size(); i++) {
            if (islands.get(i).getPoints().size() > maxCountOFPoints) {
                maxCountOFPoints = islands.get(i).getPoints().size();
                graphIslandIndex = i;
            }
        }
        islands.remove(graphIslandIndex);
        return islands;
    }

    public double getDistanceOfNumberFromEdge(Island island, Edge edge) {
        int midPX = (edge.getStartNode().first().getX() + edge.getEndNode().first().getX()) / 2;
        int midPY = (edge.getStartNode().first().getY() + edge.getEndNode().first().getY()) / 2;
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

    public void numerateIslands(List<Island> islands, Context context) {
        OCRHelper ocrHelper = new OCRHelper();
        BitmapHelper bitmapHelper = new BitmapHelper();
        for (Island island : islands) {
            if (!island.isGraph())
                island.setValue(ocrHelper.numberFromBitmap(bitmapHelper.createNumberBitmapFromIsland(island), context));
        }
    }
}

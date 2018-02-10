package com.example.tatevabgaryan.graphprocessing.helper;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.model.Edge;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 1/27/2018.
 */
public class BitmapHelper {

    public TreeSet<Point> separateContour(Bitmap bmpOriginal) {
        TreeSet<Point> contour = new TreeSet<>(new PointComparator());

        bmpOriginal = Bitmap.createScaledBitmap(bmpOriginal, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        Bitmap grayScaled = bmpOriginal.copy(Bitmap.Config.ARGB_8888, true);
        for (int x = 0; x < grayScaled.getWidth(); x++) {
            for (int y = 0; y < grayScaled.getHeight(); y++) {
                int r = Color.red(grayScaled.getPixel(x, y));
                int g = Color.green(grayScaled.getPixel(x, y));
                int b = Color.blue(grayScaled.getPixel(x, y));
                int mid = (r + g + b) / 3;
                //grayScaled.setPixel(x, y, Color.rgb(mid, mid, mid));
                if (mid < 100) {
                    contour.add(new Point(x, y));
                }
            }
        }
        return contour;
    }

    public Bitmap createBitmapFromPoint(TreeSet<Point> contour) {
//        contour = new TreeSet<>(new PointComparator());
//        contour.add(new Point(1115, 741));
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        image = Bitmap.createScaledBitmap(image, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Point p = new Point(x, y);
                if (contour.contains(p)) {
                    image.setPixel(x, y, Color.rgb(0, 0, 0));
                } else {
                    image.setPixel(x, y, Color.rgb(255, 255, 255));
                }
            }
        }
        return image;
    }

    public TreeSet<Point> findGraphNodes(TreeSet<Point> contour) {
        TreeSet<Point> nodes = new TreeSet<>(new PointComparator());
        for (Point p : contour) {
            if (isNode(p, contour)) {
                nodes.add(p);
            }
        }
        return filterOnePixelFromEach(nodes);
    }

    public List<Edge> findEdges(Graph graph, TreeSet<Point> contour) {
        graph.setNodes(findGraphNodes(contour));
        List<Point> nodes = new ArrayList<>(graph.getNodes());
        List<Edge> edges = new ArrayList<>();
        TreeSet<Point> contourApprox = new TreeSet<>(new PointComparatorApprox());
        contourApprox.addAll(contour);
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                if (isEdge(nodes.get(i), nodes.get(j), contourApprox)) {
                    edges.add(new Edge(nodes.get(i), nodes.get(j)));
                }
            }
        }
        return edges;
    }

    private boolean isEdge(final Point p1, final Point p2, final TreeSet<Point> contour) {
        boolean smallerX = p1.getX() < p2.getX();
        final int x1 = smallerX ? p1.getX() : p2.getX(), x2 = smallerX ? p2.getX() : p1.getX(),
                y1 = smallerX ? p1.getY() : p2.getY(),
                y2 = smallerX ? p2.getY() : p1.getY();
        for (int i = x1+1; i < x2; i++) {
            if (((x2 - i) * (y2 - y1)) % (x2 - x1) == 0) {
                int y = y2 - ((x2 - i) * (y2 - y1)) / (x2 - x1);
                    if (!contour.contains(new Point(i, y)))
                        return false;
            }
        }
        return true;
    }

    private TreeSet<Point> filterOnePixelFromEach(TreeSet<Point> nodes) {
        TreeSet<Point> filtered = new TreeSet<>(new PointComparator());
        List<Point> pointsOfEachNode = new ArrayList<>();
        int nearDistance = 500;
        Point previousPoint = null;
        for (Point p : nodes) {
            if (previousPoint == null) {
                pointsOfEachNode.add(p);
            } else {
                if (getDistanceOfPoints(previousPoint, p) > nearDistance) {
                    if (pointsOfEachNode.size() != 0) {
                        filtered.add(pointsOfEachNode.get(pointsOfEachNode.size() / 2));
                        pointsOfEachNode = new ArrayList<>();
                        pointsOfEachNode.add(p);
                    }
                } else {
                    pointsOfEachNode.add(p);
                }
            }
            previousPoint = p;
        }
        filtered.add(pointsOfEachNode.get(pointsOfEachNode.size() / 2));

        // TODO There are points from different nodes that have "same" distance from origin and mix the order in  nodes treeSet
        // as a result -> 2 points from same node.
        TreeSet<Point> toRemove = new TreeSet<>(new PointComparator());
        for (Point p1 : filtered) {
            for (Point p2 : filtered) {
                if (!toRemove.contains(p1) && !p1.equals(p2) && getDistanceOfPoints(p1, p2) < nearDistance) {
                    toRemove.add(p2);
                }
            }
        }
        filtered.removeAll(toRemove);
        return filtered;
    }

    private int getDistanceOfPoints(Point p1, Point p2) {
        return (p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY());
    }

    private boolean isNode(Point p, TreeSet<Point> contour) {
        Point checkPoint = new Point(p.getX(), p.getY());
        for(int i = 1; i<= MainActivity.NODE_RADIUS; i++){
            checkPoint.setX(p.getX() + i);
            checkPoint.setY(p.getY() + i);
            if (!contour.contains(checkPoint)) {
                return false;
            }
            checkPoint.setX(p.getX() - i);
            checkPoint.setY(p.getY() + i);
            if (!contour.contains(checkPoint)) {
                return false;
            }
            checkPoint.setX(p.getX() + i);
            checkPoint.setY(p.getY() - i);
            if (!contour.contains(checkPoint)) {
                return false;
            }
            checkPoint.setX(p.getX() -i);
            checkPoint.setY(p.getY() - i);
            if (!contour.contains(checkPoint)) {
                return false;
            }
        }
        return true;
    }

    static class PointComparator implements Comparator<Point> {
        @Override
        public int compare(Point p1, Point p2) {
            int diff = p1.getDistanceFromOrigin() - p2.getDistanceFromOrigin();
            if (diff == 0) {
                return p1.getX() - p2.getX();
            }
            return diff;
        }
    }

    static class PointComparatorApprox implements Comparator<Point> {
        @Override
        public int compare(Point p1, Point p2) {
            int diff = p1.getDistanceFromOrigin() - p2.getDistanceFromOrigin();
            if (Math.abs(diff) < MainActivity.APPROXIMATION_RADIUS) {
               return 0;
            }
            return diff;
        }
    }
}

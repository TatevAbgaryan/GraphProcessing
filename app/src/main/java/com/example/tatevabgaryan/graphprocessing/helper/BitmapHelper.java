package com.example.tatevabgaryan.graphprocessing.helper;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 1/27/2018.
 */
public class BitmapHelper {

    public Bitmap createBitmapFromPoint(TreeSet<Point> contour) {
//        contour = new TreeSet<>(new PointComparator());
//        contour.add(new Point(276, 215));
//        contour.add(new Point(383, 47));

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

    public Bitmap createNumberBitmapFromIsland(Island island) {
        int minX = Integer.MAX_VALUE, maxX = 0, minY = Integer.MAX_VALUE, maxY = 0;
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        image = Bitmap.createScaledBitmap(image, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Point p = new Point(x, y);
                if (island.getPoints().contains(p)) {
                    image.setPixel(x, y, Color.rgb(255, 255, 255));
                    if (x < minX) minX = x;
                    if(y < minY) minY = y;
                    if(x > maxX) maxX = x;
                    if(y > maxY) maxY = y;
                } else {
                    image.setPixel(x, y, Color.rgb(0, 0, 0));
                }
            }
        }

        return Bitmap.createBitmap(image, minX-3, minY-3, maxX-minX+10, maxY - minY+10);
    }
    public boolean isEdge(final Point p1, final Point p2, final TreeSet<Point> contour) {
        boolean smallerX = p1.getX() < p2.getX();
        final int x1 = smallerX ? p1.getX() : p2.getX(), x2 = smallerX ? p2.getX() : p1.getX(),
                y1 = smallerX ? p1.getY() : p2.getY(),
                y2 = smallerX ? p2.getY() : p1.getY();
        for (int i = x1 + 1; i <= x2 - 1; i++) {
            double y = y2 - (double) ((x2 - i) * (y2 - y1)) / (x2 - x1);
            if (isInteger(y)) {
                if (!contour.contains(new Point(i, (int) y)))
                    return false;
            }
        }
        return true;
    }

    private boolean isInteger(double y) {
        if ((y == Math.floor(y)) && !Double.isInfinite(y)) {
            return true;
        }
        return false;
    }

    public TreeSet<Point> filterOnePixelFromEach(TreeSet<Point> nodes) {
        TreeSet<Point> filtered = new TreeSet<>(new PointComparator());
        List<Point> pointsOfEachNode = new ArrayList<>();
        Point previousPoint = null;
        for (Point p : nodes) {
            if (previousPoint == null) {
                pointsOfEachNode.add(p);
            } else {
                if (getDistanceOfPoints(previousPoint, p) > MainActivity.NODE_POINT_DISTANCE) {
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
                if (!toRemove.contains(p1) && !p1.equals(p2) && getDistanceOfPoints(p1, p2) < MainActivity.NODE_POINT_DISTANCE) {
                    toRemove.add(p2);
                }
            }
        }
        filtered.removeAll(toRemove);
        return filtered;
    }

    public int getDistanceOfPoints(Point p1, Point p2) {
        return (int)(Math.sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY())));
    }

    public boolean isNode(Point p, TreeSet<Point> contour) {
        int rowNbr[] = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
        int colNbr[] = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 1; i <= MainActivity.NODE_RADIUS; i++) {
            for (int j = 0; j<rowNbr.length; j++){
                Point checkP = new Point(p.getX() + i*rowNbr[j], p.getY() + i*colNbr[j]);
                if(!contour.contains(checkP)){
                  return false;
                }
            }
        }
        return true;
    }

}

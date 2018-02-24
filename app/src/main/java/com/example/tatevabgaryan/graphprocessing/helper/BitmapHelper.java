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
//        contour.add(new Point(436, 75));
//        contour.add(new Point(595, 159));
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

    public Bitmap createBitmapFromNodes(List<TreeSet<Point>> nodes) {
       List<Point> contour = new ArrayList<>();
//        contour.add(new Point(436, 75));
        for(TreeSet<Point> nodePoints : nodes){
            contour.addAll(nodePoints);
        }
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
                    if(foundY && i == x2-1){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean containsApprox(TreeSet<Point> contour, Point linePoint){
        for(Point p: contour){
            if(getDistanceOfPoints(p, linePoint) < 10){
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
                    if (pointsOfEachNode.size() > 5) {
                        grouped.add(pointsOfEachNode);
                        pointsOfEachNode = new TreeSet<>(new PointComparator());
                        pointsOfEachNode.add(p);
                    }
                } else {
                    pointsOfEachNode.add(p);
                }
            }
            previousPoint = p;
        }
        grouped.add(pointsOfEachNode);
        return grouped;
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

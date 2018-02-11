package com.example.tatevabgaryan.graphprocessing.process;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.comparator.PointComparatorApprox;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.helper.BitmapHelper;
import com.example.tatevabgaryan.graphprocessing.model.Edge;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 2/10/2018.
 */

public class ProcessGraph implements ProcessGraphI {
    BitmapHelper bitmapHelper = new BitmapHelper();

    @Override
    public TreeSet<Point> getContourFromBitmap(Bitmap bitmap) {
        TreeSet<Point> contour = new TreeSet<>(new PointComparator());
        bitmap = Bitmap.createScaledBitmap(bitmap, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        Bitmap grayScaled = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (int x = 0; x < grayScaled.getWidth(); x++) {
            for (int y = 0; y < grayScaled.getHeight(); y++) {
                int r = Color.red(grayScaled.getPixel(x, y));
                int g = Color.green(grayScaled.getPixel(x, y));
                int b = Color.blue(grayScaled.getPixel(x, y));
                int mid = (r + g + b) / 3;
                if (mid < 100) {
                    contour.add(new Point(x, y));
                }
            }
        }
        return contour;
    }

    @Override
    public TreeSet<Point> findGraphNodes(TreeSet<Point> contour) {
        TreeSet<Point> nodes = new TreeSet<>(new PointComparator());
        for (Point p : contour) {
            if (bitmapHelper.isNode(p, contour)) {
                nodes.add(p);
            }
        }
        return bitmapHelper.filterOnePixelFromEach(nodes);
    }

    @Override
    public List<Edge> findEdges(TreeSet<Point> nodes, TreeSet<Point> contour) {
        List<Edge> edges = new ArrayList<>();
        List<Point> nodesList = new ArrayList<>(nodes);
        TreeSet<Point> contourApprox = new TreeSet<>(new PointComparatorApprox());
        contourApprox.addAll(contour);
        for (int i = 0; i < nodesList.size(); i++) {
            for (int j = i + 1; j < nodesList.size(); j++) {
                if (bitmapHelper.isEdge(nodesList.get(i), nodesList.get(j), contourApprox)) {
                    edges.add(new Edge(nodesList.get(i), nodesList.get(j)));
                }
            }
        }
        return edges;
    }

    @Override
    public List<Island> findIslands(TreeSet<Point> contour) {
        List<Island> islands = new ArrayList<>();
        TreeSet<Point> pointsOfEachIsland = new TreeSet<>(new PointComparator());
        Point previousPoint = null;
        for (Point p : contour) {
            if (previousPoint == null) {
                pointsOfEachIsland.add(p);
            } else {
                if (bitmapHelper.getDistanceOfPoints(previousPoint, p) > MainActivity.ISLAND_POINT_DISTANCE) {
                    if (pointsOfEachIsland.size() > 10) {
                        Island island = new Island(pointsOfEachIsland);
                        islands.add(island);
                    }
                    pointsOfEachIsland = new TreeSet<>(new PointComparator());
                    pointsOfEachIsland.add(p);
                } else {
                    pointsOfEachIsland.add(p);
                }
            }
            previousPoint = p;
        }
        // merge islands
        List<Island> removeIslands = new ArrayList<>();
        for (int i = 0; i < islands.size(); i++) {
            for (int j = i+1; j<islands.size(); j++){
                Point pI = new ArrayList<>(islands.get(i).getPoints()).get(0);
                Point pJ = new ArrayList<>(islands.get(j).getPoints()).get(0);
                if(bitmapHelper.getDistanceOfPoints(pI, pJ) < MainActivity.ISLAND_POINT_DISTANCE){
                    islands.get(i).getPoints().addAll(islands.get(j).getPoints());
                    removeIslands.add(islands.get(j));
                }
            }
        }
        islands.removeAll(removeIslands);
        return islands;
    }
}

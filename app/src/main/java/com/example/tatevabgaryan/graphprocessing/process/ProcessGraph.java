package com.example.tatevabgaryan.graphprocessing.process;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.helper.BitmapHelper;
import com.example.tatevabgaryan.graphprocessing.helper.GraphHelper;
import com.example.tatevabgaryan.graphprocessing.helper.IslandHelper;
import com.example.tatevabgaryan.graphprocessing.helper.OCRHelper;
import com.example.tatevabgaryan.graphprocessing.model.Contour;
import com.example.tatevabgaryan.graphprocessing.model.Edge;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 2/10/2018.
 */

public class ProcessGraph implements ProcessGraphI {
    GraphHelper graphHelper = new GraphHelper();

    @Override
    public Contour getContourFromBitmap(Bitmap bitmap) {
        TreeSet<Point> points = new TreeSet<>(new PointComparator());
        int[][] matrix = new int[BitmapContext.getWidth()][BitmapContext.getHeight()];
        //bitmap = Bitmap.createScaledBitmap(bitmap, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        Bitmap grayScaled = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (int x = 0; x < grayScaled.getWidth(); x++) {
            for (int y = 0; y < grayScaled.getHeight(); y++) {
                int r = Color.red(grayScaled.getPixel(x, y));
                int g = Color.green(grayScaled.getPixel(x, y));
                int b = Color.blue(grayScaled.getPixel(x, y));
                int mid = (r + g + b) / 3;
                if (mid < 100) {
                    points.add(new Point(x, y));
                    matrix[x][y] = 1;
                } else {
                    matrix[x][y] = 0;
                }
            }
        }
        Contour contour = new Contour();
        contour.setMatrix(matrix);
        contour.setPoints(points);
        contour.setRowSize(BitmapContext.getWidth());
        contour.setColumnSize(BitmapContext.getHeight());
        return contour;
    }

    @Override
    public List<TreeSet<Point>> findGraphNodes(TreeSet<Point> contour) {
        TreeSet<Point> nodes = new TreeSet<>(new PointComparator());
        for (Point p : contour) {
            if (graphHelper.isNode(p, contour)) {
                nodes.add(p);
            }
        }
        return graphHelper.groupPointsOfEachNode(nodes);
    }

    @Override
    public List<Edge> findEdges(List<TreeSet<Point>> nodes, TreeSet<Point> contour) {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                //TODO save node points as arraylist
                if (graphHelper.isEdge(new ArrayList<>(nodes.get(i)), new ArrayList<>(nodes.get(j)), contour)) {
                    edges.add(new Edge(nodes.get(i), nodes.get(j)));
                }
            }
        }
        return edges;
    }

    @Override
    public List<Island> findIslands(Contour contour, Context context) {
        IslandHelper islandHelper = new IslandHelper();
        List<Island> islands = islandHelper.findIslands(contour.getMatrix(), contour.getRowSize(), contour.getColumnSize());
        graphHelper.numerateIslands(islands, context);
        return islands;
    }

    @Override
    public void mapEdgesAndNumberIslands(Graph graph, List<Island> islands) {
        List<Edge> edges = graph.getEdges();
        islands = graphHelper.getNumberIslands(islands);

        for (Edge edge : edges) {
            double minDistance = Double.MAX_VALUE;
            Island islandOfEdge = null;
            double distance;
            for (Island island : islands) {
                if ((distance = graphHelper.getDistanceOfNumberFromEdge(island, edge)) < minDistance) {
                    minDistance = distance;
                    islandOfEdge = island;
                }
            }
            edge.setNumberIsland(islandOfEdge);
        }
    }
}

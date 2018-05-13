package com.example.tatevabgaryan.graphprocessing.builder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.annimon.stream.Stream;
import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.helper.GraphHelper;
import com.example.tatevabgaryan.graphprocessing.helper.IslandHelper;
import com.example.tatevabgaryan.graphprocessing.model.Contour;
import com.example.tatevabgaryan.graphprocessing.model.Edge;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Tatev.Abgaryan on 2/10/2018.
 */

public class GraphBuilder implements IGraphBuilder {
    GraphHelper graphHelper = new GraphHelper();
    Graph graph = new Graph();

    @Override
    public void getContourFromBitmap(final Bitmap bitmap) {
        final TreeSet<Point> points = new TreeSet<>(new PointComparator());
        final int[][] matrix = new int[bitmap.getWidth()][bitmap.getHeight()];

//        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
//        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
//        final ArrayList pixelsList =Lis;
//        Stream.of(pixelsList).forEach(p -> addInContour((Integer) p, points, matrix, pixelsList.indexOf(p)));

        final Bitmap grayScaled = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int threadCount = 5;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int startH = i * bitmap.getHeight() / threadCount;
            final int endH = i == threadCount - 1 ? bitmap.getHeight() : (i + 1) * bitmap.getHeight() / threadCount;
            Thread ti = new Thread(new Runnable() {
                @Override
                public void run() {
                    findPoints(grayScaled, points, matrix, startH, endH);
                }
            });
            ti.start();
            threads[i] = ti;
        }
        for (int i = 0; i < threadCount; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Contour contour = new Contour();
        contour.setMatrix(matrix);
        contour.setPoints(points);
        contour.setRowSize(bitmap.getWidth());
        contour.setColumnSize(bitmap.getHeight());
        graph.setContour(contour);
    }


    @Override
    public void findIslands() {
        IslandHelper islandHelper = new IslandHelper();
        List<Island> islands = islandHelper.findIslands(graph);
        graph.setIslands(islands);
    }

    @Override
    public void findGraphNodes() {
        TreeSet<Point> graphContour = graph.getGraphIsland().getPoints();
        TreeSet<Point> nodes = new TreeSet<>(new PointComparator());
        for (Point p : graphContour) {
            if (graphHelper.isNode(p, graphContour)) {
                nodes.add(p);
            }
        }

        graph.setNodes(graphHelper.groupPointsOfEachNode(nodes, graph.getIslands()));
    }

    @Override
    public void findEdges() {
        TreeSet<Point> graphContour = graph.getGraphIsland().getPoints();
        List<TreeSet<Point>> nodes = graph.getNodes();
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                if (graphHelper.isEdge(new ArrayList<>(nodes.get(i)), new ArrayList<>(nodes.get(j)), graphContour)) {
                    edges.add(new Edge(nodes.get(i), nodes.get(j)));
                }
            }
        }
        graph.setEdges(edges);
    }

    @Override
    public void numerateIslands() {
        graphHelper.numerateIslands(graph.getIslands());
    }

    @Override
    public void mapEdgesAndNumberIslands() {
        List<Edge> edges = graph.getEdges();
        List<Island> islands = graph.getIslands();

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

    @Override
    public Graph getGraph() {
        return graph;
    }

    private void findPoints(Bitmap grayScaled, TreeSet<Point> points, int[][] matrix, int statH, int endH) {
        Log.d("distributed", "stath = " + statH + " endH = " + endH);
        for (int x = 0; x < grayScaled.getWidth(); x++) {
            for (int y = statH; y < endH; y++) {
                int r = Color.red(grayScaled.getPixel(x, y));
                int g = Color.green(grayScaled.getPixel(x, y));
                int b = Color.blue(grayScaled.getPixel(x, y));
                int mid = (r + g + b) / 3;
                if (mid < 125) {
                    points.add(new Point(x, y));
                    matrix[x][y] = 1;
                } else {
                    Log.d("midColor " + statH, mid + "");
                    matrix[x][y] = 0;
                }
            }
        }
    }

    private void addInContour(Integer pixel, TreeSet<Point> points, int[][] matrix, int index) {
        int y = index / BitmapContext.getWidth();
        int x = index % BitmapContext.getWidth();
        if ((Color.red(pixel) + Color.green(pixel) + Color.red(pixel) < 120)) {
            points.add(new Point(x, y));
            matrix[x][y] = 1;
        } else {
            matrix[x][y] = 0;
        }
    }
}

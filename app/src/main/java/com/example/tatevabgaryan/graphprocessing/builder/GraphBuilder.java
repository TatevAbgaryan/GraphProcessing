package com.example.tatevabgaryan.graphprocessing.builder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.helper.GraphHelper;
import com.example.tatevabgaryan.graphprocessing.helper.IslandHelper;
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

public class GraphBuilder implements IGraphBuilder {
    GraphHelper graphHelper = new GraphHelper();

    Graph graph = new Graph();

    @Override
    public void getContourFromBitmap(Bitmap bitmap) {
        TreeSet<Point> points = new TreeSet<>(new PointComparator());
        int[][] matrix = new int[bitmap.getWidth()][bitmap.getHeight()];
        Bitmap grayScaled = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (int x = 0; x < grayScaled.getWidth(); x++) {
            for (int y = 0; y < grayScaled.getHeight(); y++) {
                int r = Color.red(grayScaled.getPixel(x, y));
                int g = Color.green(grayScaled.getPixel(x, y));
                int b = Color.blue(grayScaled.getPixel(x, y));
                int mid = (r + g + b) / 3;
                if (mid < 105) {
                    points.add(new Point(x, y));
                    matrix[x][y] = 1;
                } else {
                    Log.d("midColor", mid+"");
                    matrix[x][y] = 0;
                }
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
    public void findGraphNodes() {
        TreeSet<Point> contour = graph.getContour().getPoints();
        TreeSet<Point> nodes = new TreeSet<>(new PointComparator());
        for (Point p : contour) {
            if (graphHelper.isNode(p, contour)) {
                nodes.add(p);
            }
        }

        graph.setNodes(graphHelper.groupPointsOfEachNode(nodes, graph.getIslands()));
    }

    @Override
    public void findEdges() {
        TreeSet<Point> contour = graph.getContour().getPoints();
        List<TreeSet<Point>> nodes = graph.getNodes();
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                //TODO save node points as arraylist
                if (graphHelper.isEdge(new ArrayList<>(nodes.get(i)), new ArrayList<>(nodes.get(j)), contour)) {
                    edges.add(new Edge(nodes.get(i), nodes.get(j)));
                }
            }
        }
        graph.setEdges(edges);
    }

    @Override
    public void findIslands() {
        Contour contour = graph.getContour();
        IslandHelper islandHelper = new IslandHelper();
        List<Island> islands = islandHelper.findIslands(contour.getMatrix(), contour.getRowSize(), contour.getColumnSize());
        graph.setIslands(islands);
    }

    @Override
    public void numerateIslands(Context context) {
        graphHelper.numerateIslands(graph.getIslands(), context);
    }

    @Override
    public void mapEdgesAndNumberIslands() {
        List<Edge> edges = graph.getEdges();
        //TODO change this, graph island is already known
        List<Island> islands = graphHelper.getNumberIslands(graph.getIslands());

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
}

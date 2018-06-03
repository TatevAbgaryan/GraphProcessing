package com.example.tatevabgaryan.graphprocessing.helper;

import android.util.Log;

import com.example.tatevabgaryan.graphprocessing.model.Edge;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 3/3/2018.
 */

public class PathHelper {

    public void fillAllPairShortestPaths(double[][] shortestRoutes, double[][] shortestDistances, Graph graph) {
        List<TreeSet<Point>> nodes = graph.getNodes();
        List<Edge> edges = graph.getEdges();
        // fill matrixes with initial values
        for (int i = 0; i < shortestDistances.length; i++)
            for (int j = 0; j < shortestDistances.length; j++) {
                shortestRoutes[i][j] = j;
                Edge graphEdge = new Edge(i, j);
                int index;
                if (i == j) {
                    shortestDistances[i][j] = 0;
                } else if ((index = edges.indexOf(graphEdge)) != -1) {
                    Edge e = edges.get(index);
                    Log.d("edge = ",  e.getStartNode() + " " + e.getEndNode());
                    Island numberIsland = e.getNumberIsland();
                    int value = numberIsland.getValue();
                    shortestDistances[i][j] = value;
                    shortestDistances[j][i] = value;
                } else {
                    if (shortestDistances[i][j] == 0)
                        shortestDistances[i][j] = Double.POSITIVE_INFINITY;
                }

            }

        // all pair shortest paths Floyd-Warshal
        for (int i = 0; i < shortestDistances.length; i++) {
            // compute shortest paths using only 0, 1, ..., i as intermediate vertices
            for (int v = 0; v < shortestDistances.length; v++) {
                for (int w = 0; w < shortestDistances.length; w++) {
                    if (shortestDistances[v][w] > shortestDistances[v][i] + shortestDistances[i][w]) {
                        shortestDistances[v][w] = shortestDistances[v][i] + shortestDistances[i][w];
                        shortestRoutes[v][w] = shortestRoutes[v][i];
                    }
                }
            }
        }
    }
}
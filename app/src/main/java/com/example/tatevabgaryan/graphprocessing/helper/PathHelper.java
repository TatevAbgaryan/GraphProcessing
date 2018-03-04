package com.example.tatevabgaryan.graphprocessing.helper;

import com.example.tatevabgaryan.graphprocessing.model.Edge;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 3/3/2018.
 */

public class PathHelper {

    public void fillAllPairShortestPaths(double[][] shortestRoutes, Graph graph) {
        double[][] shortestLengths = new double[shortestRoutes.length][shortestRoutes.length];
        List<TreeSet<Point>> nodes = graph.getNodes();
        List<Edge> edges = graph.getEdges();
        // fill matrixes with initial values
        for (int i = 0; i < shortestLengths.length; i++)
            for (int j = 0; j < shortestLengths.length; j++) {
                shortestRoutes[i][j] = j;
                Edge graphEdge = new Edge(nodes.get(i), nodes.get(j));
                int index;
                if (i == j) {
                    shortestLengths[i][j] = 0;
                } else if ((index = edges.indexOf(graphEdge)) != -1) {
                    shortestLengths[i][j] = edges.get(index
                    ).getNumberIsland().getValue();
                } else {
                    shortestLengths[i][j] = Double.POSITIVE_INFINITY;
                }

            }

        // all pair shortest paths Floyd-Warshal
        for (int i = 0; i < shortestLengths.length; i++) {
            // compute shortest paths using only 0, 1, ..., i as intermediate vertices
            for (int v = 0; v < shortestLengths.length; v++) {
                for (int w = 0; w < shortestLengths.length; w++) {
                    if (shortestLengths[v][w] > shortestLengths[v][i] + shortestLengths[i][w]) {
                        shortestLengths[v][w] = shortestLengths[v][i] + shortestLengths[i][w];
                        shortestRoutes[v][w] = shortestLengths[v][i];
                    }
                }
            }
        }
    }
}

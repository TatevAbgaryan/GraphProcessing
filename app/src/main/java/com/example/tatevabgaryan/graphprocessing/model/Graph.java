package com.example.tatevabgaryan.graphprocessing.model;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 1/28/2018.
 */

public class Graph {
    private TreeSet<Point> nodes;
    private List<Edge> edges;

    public TreeSet<Point> getNodes() {
        return nodes;
    }

    public void setNodes(TreeSet<Point> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
}

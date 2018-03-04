package com.example.tatevabgaryan.graphprocessing.model;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 1/28/2018.
 */

public class Graph {
    private List<TreeSet<Point>> nodes;
    private List<Edge> edges;
    private Contour contour;
    private List<Island> islands;

    public List<TreeSet<Point>> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeSet<Point>> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Contour getContour() {
        return contour;
    }

    public void setContour(Contour contour) {
        this.contour = contour;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public void setIslands(List<Island> islands) {
        this.islands = islands;
    }
}

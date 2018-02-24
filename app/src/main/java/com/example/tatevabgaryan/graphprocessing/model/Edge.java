package com.example.tatevabgaryan.graphprocessing.model;

import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 1/29/2018.
 */

public class Edge {

    private TreeSet<Point> startNode;
    private TreeSet<Point> endNode;
    private int lenght;

    public Edge(TreeSet<Point> startNode, TreeSet<Point> endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.lenght = lenght;
    }

    public TreeSet<Point> getStartNode() {
        return startNode;
    }

    public void setStartNode(TreeSet<Point> startNode) {
        this.startNode = startNode;
    }

    public TreeSet<Point> getEndNode() {
        return endNode;
    }

    public void setEndNode(TreeSet<Point> endNode) {
        this.endNode = endNode;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }
}

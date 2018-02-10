package com.example.tatevabgaryan.graphprocessing.model;

/**
 * Created by Tatev.Abgaryan on 1/29/2018.
 */

public class Edge {

    private Point startNode;
    private Point endNode;
    private int lenght;

    public Edge(Point startNode, Point endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public Point getStartNode() {
        return startNode;
    }

    public void setStartNode(Point startNode) {
        this.startNode = startNode;
    }

    public Point getEndNode() {
        return endNode;
    }

    public void setEndNode(Point endNode) {
        this.endNode = endNode;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }
}

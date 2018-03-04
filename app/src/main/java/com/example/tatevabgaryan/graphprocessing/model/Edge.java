package com.example.tatevabgaryan.graphprocessing.model;

import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 1/29/2018.
 */

public class Edge {

    private TreeSet<Point> startNode;
    private TreeSet<Point> endNode;
    private Island numberIsland;

    public Edge(TreeSet<Point> startNode, TreeSet<Point> endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
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

    public Island getNumberIsland() {
        return numberIsland;
    }

    public void setNumberIsland(Island numberIsland) {
        this.numberIsland = numberIsland;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;

        Edge edge = (Edge) o;

        if (getStartNode().size() != edge.getStartNode().size())
            return false;
        if (getEndNode().size() != edge.getEndNode().size())
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = getStartNode().hashCode();
        result = 31 * result + getEndNode().hashCode();
        result = 31 * result + (getNumberIsland() != null ? getNumberIsland().hashCode() : 0);
        return result;
    }
}

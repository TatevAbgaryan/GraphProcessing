package com.example.tatevabgaryan.graphprocessing.model;

import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 1/29/2018.
 */

public class Edge {

    private int startNode;
    private int endNode;
    private Island numberIsland;

    public Edge(int startNode, int endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public int getStartNode() {
        return startNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }

    public int getEndNode() {
        return endNode;
    }

    public void setEndNode(int endNode) {
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
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (startNode != edge.startNode) return false;
        if (endNode != edge.endNode) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = startNode;
        result = 31 * result + endNode;
        result = 31 * result + (numberIsland != null ? numberIsland.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "startNode=" + startNode +
                ", endNode=" + endNode +
                ", numberIsland=" + numberIsland.getValue() +
                '}';
    }
}

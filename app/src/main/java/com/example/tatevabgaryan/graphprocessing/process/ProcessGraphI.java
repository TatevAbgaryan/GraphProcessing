package com.example.tatevabgaryan.graphprocessing.process;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.tatevabgaryan.graphprocessing.model.Contour;
import com.example.tatevabgaryan.graphprocessing.model.Edge;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 2/10/2018.
 */

public interface ProcessGraphI {

    public Contour getContourFromBitmap(Bitmap bitmap);

    public List<TreeSet<Point>> findGraphNodes(TreeSet<Point> contour);

    public List<Edge> findEdges(List<TreeSet<Point>> nodes, TreeSet<Point> contour);

    public List<Island> findIslands(Contour contour, Context context);

    public void mapEdgesAndNumberIslands(Graph graph, List<Island> islands);
    // ...
}

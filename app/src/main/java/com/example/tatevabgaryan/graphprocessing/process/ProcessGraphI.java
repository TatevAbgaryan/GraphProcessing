package com.example.tatevabgaryan.graphprocessing.process;

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

    public TreeSet<Point> findGraphNodes(TreeSet<Point> contour);

    public List<Edge> findEdges(TreeSet<Point> nodes, TreeSet<Point> contour);

    public List<Island> findIslands(Contour contour);

    // ...
}

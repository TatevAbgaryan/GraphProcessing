package com.example.tatevabgaryan.graphprocessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.helper.BitmapHelper;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;
import com.example.tatevabgaryan.graphprocessing.process.ProcessGraph;

import java.util.List;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    public static final int SCALE = 10;
    public static final int NODE_RADIUS = 5;
    public static final int APPROXIMATION_RADIUS = 15;
    public static final int NODE_POINT_DISTANCE = 100;
    public static final int ISLAND_POINT_DISTANCE = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BitmapHelper bitmapHelper = new BitmapHelper();
        ProcessGraph processGraph = new ProcessGraph();

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.graph_numbers);
        BitmapContext.setHeight(bm.getHeight()/SCALE);
        BitmapContext.setWidth(bm.getWidth()/SCALE);

        Graph graph = new Graph();
        TreeSet<Point> contour = processGraph.getContourFromBitmap(bm);
        //graph.setNodes(processGraph.findGraphNodes(contour));
        //graph.setEdges(processGraph.findEdges(graph.getNodes(), contour));
        List<Island> islands = processGraph.findIslands(contour);
        ImageView imageView = (ImageView)findViewById(R.id.image_view);
        //imageView.setImageBitmap(bitmapHelper.createBitmapFromPoint(contour));
        //imageView.setImageBitmap(bitmapHelper.createBitmapFromPoint(graph.getNodes()));
        TreeSet<Point> islandPoints = islands.get(2).getPoints();
        imageView.setImageBitmap(bitmapHelper.createBitmapFromPoint(islandPoints));
    }
}

package com.example.tatevabgaryan.graphprocessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.helper.BitmapHelper;
import com.example.tatevabgaryan.graphprocessing.helper.OCRHelper;
import com.example.tatevabgaryan.graphprocessing.model.Contour;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;
import com.example.tatevabgaryan.graphprocessing.process.ProcessGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    public static final int SCALE = 10;
    public static final int NODE_RADIUS = 5;
    public static final int APPROXIMATION_RADIUS = 15;
    public static final int NODE_POINT_DISTANCE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);

        BitmapHelper bitmapHelper = new BitmapHelper();
        ProcessGraph processGraph = new ProcessGraph();

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.graph_numbers);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bm = Bitmap.createScaledBitmap(bm, bm.getWidth() / SCALE, bm.getHeight() / SCALE, true);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        BitmapContext.setHeight(bm.getHeight());
        BitmapContext.setWidth(bm.getWidth());

        Graph graph = new Graph();
        Contour contour = processGraph.getContourFromBitmap(bm);
        TreeSet<Point> contourPoints = contour.getPoints();
        graph.setNodes(processGraph.findGraphNodes(contourPoints));
        graph.setEdges(processGraph.findEdges(graph.getNodes(), contourPoints));
        List<Island> islands = processGraph.findIslands(contour, this);
        processGraph.mapEdgesAndNumberIslands(graph, islands);
        imageView.setImageBitmap(bitmapHelper.createBitmapFromPoint(contourPoints));
    }
}
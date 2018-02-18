package com.example.tatevabgaryan.graphprocessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
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
        ImageView imageView = (ImageView)findViewById(R.id.image_view);

        BitmapHelper bitmapHelper = new BitmapHelper();
        ProcessGraph processGraph = new ProcessGraph();
        OCRHelper ocrHelper = new OCRHelper();

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.without_numbers);
        BitmapContext.setHeight(bm.getHeight()/SCALE);
        BitmapContext.setWidth(bm.getWidth()/SCALE);

        Graph graph = new Graph();
        Contour contour = processGraph.getContourFromBitmap(bm);
        TreeSet<Point> contourPoints = contour.getPoints();
        TreeSet<Point> nearPoints = new TreeSet<>(new PointComparator());
//       Point myPoint = new Point(436, 75);
//        for(Point p: contourPoints){
//            if(bitmapHelper.getDistanceOfPoints(p, myPoint) < 10){
//                nearPoints.add(p);
//            }
//        }
        graph.setNodes(processGraph.findGraphNodes(contourPoints));
        graph.setEdges(processGraph.findEdges(graph.getNodes(), contourPoints));
//        List<Island> islands = processGraph.findIslands(contour);
//        Bitmap numberBitmap = bitmapHelper.createNumberBitmapFromIsland(islands.get(4));
//        int number = ocrHelper.numberFromBitmap(numberBitmap, this);
        imageView.setImageBitmap(bitmapHelper.createBitmapFromPoint(contourPoints));
    }
}


package com.example.tatevabgaryan.graphprocessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.helper.BitmapHelper;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    public static final int SCALE = 5;
    public static final int NODE_RADIUS = 10;
    public static final int APPROXIMATION_RADIUS = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BitmapHelper bitmapHelper = new BitmapHelper();
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.without_numbers);
        BitmapContext.setHeight(bm.getHeight()/SCALE);
        BitmapContext.setWidth(bm.getWidth()/SCALE);

        Graph graph = new Graph();
        TreeSet<Point> contour = bitmapHelper.separateContour(bm);
        graph.setEdges(bitmapHelper.findEdges(graph, contour));

        ImageView imageView = (ImageView)findViewById(R.id.image_view);
        //imageView.setImageBitmap(bitmapHelper.createBitmapFromPoint(contour));
         imageView.setImageBitmap(bitmapHelper.createBitmapFromPoint(graph.getNodes()));
    }
}

package com.example.tatevabgaryan.graphprocessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.tatevabgaryan.graphprocessing.builder.GraphDirector;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.helper.BitmapHelper;
import com.example.tatevabgaryan.graphprocessing.helper.PathHelper;
import com.example.tatevabgaryan.graphprocessing.model.Graph;

public class MainActivity extends AppCompatActivity {

    public static final int SCALE = 10;
    public static final int NODE_RADIUS = 5;
    public static final int NODE_POINT_DISTANCE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);

        BitmapHelper bitmapHelper = new BitmapHelper();

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.graph_numbers);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bm = Bitmap.createScaledBitmap(bm, bm.getWidth() / SCALE, bm.getHeight() / SCALE, true);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        BitmapContext.setHeight(bm.getHeight());
        BitmapContext.setWidth(bm.getWidth());

        GraphDirector graphDirector = new GraphDirector(bm, this);
        Graph graph = graphDirector.buildGraph();
        PathHelper pathHelper = new PathHelper();
        double[][] shortestPaths = new double[graph.getNodes().size()][graph.getNodes().size()];
        pathHelper.fillAllPairShortestPaths(shortestPaths, graph);
        imageView.setImageBitmap(bitmapHelper.createBitmapFromPoint(graph.getContour().getPoints()));
    }
}
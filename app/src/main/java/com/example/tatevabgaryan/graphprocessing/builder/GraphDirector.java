package com.example.tatevabgaryan.graphprocessing.builder;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.tatevabgaryan.graphprocessing.model.Graph;

/**
 * Created by Tatev.Abgaryan on 3/3/2018.
 */

public class GraphDirector {
    private IGraphBuilder builder = new GraphBuilder();
    private Bitmap bitmap;
    private Context context;

    public GraphDirector(Bitmap bitmap, final Context context) {
        this.bitmap = bitmap;
        this.context = context;
    }

    public Graph buildGraph(){
        builder.getContourFromBitmap(bitmap);
        builder.findIslands();
        Thread thread = new Thread(){
            public void run() {
                builder.numerateIslands(context);
            }
        };
        thread.start();
        builder.findGraphNodes();
        builder.findEdges();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
        builder.mapEdgesAndNumberIslands();
        return builder.getGraph();
    }

}

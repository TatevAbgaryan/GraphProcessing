package com.example.tatevabgaryan.graphprocessing.builder;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.tatevabgaryan.graphprocessing.model.Graph;

/**
 * Created by Tatev.Abgaryan on 2/10/2018.
 */

public interface IGraphBuilder {

    public void getContourFromBitmap(Bitmap bitmap);

    public void findIslands();

    public void findGraphNodes();

    public void findEdges();

    public void numerateIslands();

    public void mapEdgesAndNumberIslands();

    public Graph getGraph();
}

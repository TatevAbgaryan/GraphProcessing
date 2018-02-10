package com.example.tatevabgaryan.graphprocessing.tasks;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.tatevabgaryan.graphprocessing.model.Point;

class GrayScaleRunnable implements Runnable {
    private Point startPixel;
    private Point endPixel;
    private Bitmap bitmap;

    public GrayScaleRunnable(Point startPixel, Point endPixel, Bitmap bitmap) {
        this.startPixel = startPixel;
        this.endPixel = endPixel;
        this.bitmap = bitmap;
    }

    @Override
    public void run(){
        for (int x = startPixel.getX(); x < endPixel.getX(); x++) {
            for (int y = startPixel.getY(); y < endPixel.getY(); y++) {
                int r = Color.red(bitmap.getPixel(x, y));
                int g = Color.green(bitmap.getPixel(x, y));
                int b = Color.blue(bitmap.getPixel(x, y));
                int mid = (r + g + b) / 3;
                bitmap.setPixel(x, y, Color.rgb(mid, mid, mid));
                if (Color.red(bitmap.getPixel(x, y)) < 100) {
                //    BitmapHelper.contour.add(new Point(x, y));
                }
            }
        }
    }
}
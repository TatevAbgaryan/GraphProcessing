package com.example.tatevabgaryan.graphprocessing.helper;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 1/27/2018.
 */
public class BitmapHelper {

    public Bitmap createBitmapFromPoint(TreeSet<Point> contour) {
//        contour = new TreeSet<>(new PointComparator());
//        contour.add(new Point(436, 75));
//        contour.add(new Point(595, 159));
//        contour.add(new Point(383, 47));

        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        image = Bitmap.createScaledBitmap(image, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Point p = new Point(x, y);
                if (contour.contains(p)) {
                    image.setPixel(x, y, Color.rgb(0, 0, 0));
                } else {
                    image.setPixel(x, y, Color.rgb(255, 255, 255));
                }
            }
        }
        return image;
    }

    public Bitmap createBitmapFromNodes(List<TreeSet<Point>> nodes) {
       List<Point> contour = new ArrayList<>();
//        contour.add(new Point(436, 75));
        for(TreeSet<Point> nodePoints : nodes){
            contour.addAll(nodePoints);
        }
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        image = Bitmap.createScaledBitmap(image, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Point p = new Point(x, y);
                if (contour.contains(p)) {
                    image.setPixel(x, y, Color.rgb(0, 0, 0));
                } else {
                    image.setPixel(x, y, Color.rgb(255, 255, 255));
                }
            }
        }
        return image;
    }

    public Bitmap createNumberBitmapFromIsland(Island island) {
        int minX = Integer.MAX_VALUE, maxX = 0, minY = Integer.MAX_VALUE, maxY = 0;
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        image = Bitmap.createScaledBitmap(image, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Point p = new Point(x, y);
                if (island.getPoints().contains(p)) {
                    image.setPixel(x, y, Color.rgb(255, 255, 255));
                    if (x < minX) minX = x;
                    if(y < minY) minY = y;
                    if(x > maxX) maxX = x;
                    if(y > maxY) maxY = y;
                } else {
                    image.setPixel(x, y, Color.rgb(0, 0, 0));
                }
            }
        }

        return Bitmap.createBitmap(image, minX-3 >= 0 ? minX-3 : minX, minY-3 >= 0 ? minY-3 : minY,
                maxX-minX+10 < BitmapContext.getWidth() ?  maxX-minX+10 : BitmapContext.getWidth(),
                maxY - minY+10  < BitmapContext.getHeight() - minY -3 ? maxY - minY+10 : maxY - minY -1);
    }
}

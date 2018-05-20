package com.example.tatevabgaryan.graphprocessing.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.ViewGroup;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 1/27/2018.
 */
public class BitmapHelper {

    public Bitmap createBitmapFromPoint(TreeSet<Point> contour) {
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        image = Bitmap.createScaledBitmap(image, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        image = fillBitmapPixels(image, Color.WHITE, Color.BLACK, contour);
        return image;
    }

    public Bitmap createBitmapFromNodes(List<TreeSet<Point>> nodes, int w, int h) {
        List<Point> nodeCenters = new ArrayList<>();
        for (TreeSet<Point> nodePoints : nodes) {
            ArrayList<Point> points = new ArrayList<>();
            points.addAll(nodePoints);
            nodeCenters.add(points.get(points.size()/2));
        }
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        GraphHelper graphHelper = new GraphHelper();
        for(int i = 0; i<image.getWidth(); i++){
            for (int j = 0; j<image.getHeight(); j++){
                for(Point p : nodeCenters) {
                    if (graphHelper.getDistanceOfPoints(p, new Point(i,j)) < MainActivity.NODE_POINT_DISTANCE){
                        image.setPixel(i, j, Color.parseColor("#339933"));
                    }
                }
            }
        }
        image = Bitmap.createScaledBitmap(image, w, h, true);
        return image;
    }

    public Bitmap createNumberBitmapFromIsland(Island island) {
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        image = image.copy(Bitmap.Config.ARGB_8888, true);
        image.setPixels(island.getPixels(), 0, BitmapContext.getWidth(), 0, 0, BitmapContext.getWidth(), BitmapContext.getHeight());
        return Bitmap.createBitmap(image, island.getMinX()-3, island.getMinY()-3, island.getMaxX() - island.getMinX() + 6, island.getMaxY() - island.getMinY() +6);
    }

    public Bitmap createBitmapFormCameraStream(byte[] bytes) {
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    private Bitmap fillBitmapPixels(Bitmap bitmap, int bgColor, int contourColor, TreeSet<Point> contour) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        Arrays.fill(pixels, bgColor);
        for (Point p : contour) {
            pixels[p.getX() + p.getY() * bitmap.getWidth()] = contourColor;
        }
        bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bitmap;
    }

    private Bitmap fillBitmapPixelsScaledToPreview(Bitmap bitmap, int bgColor, int contourColor, TreeSet<Point> contour, int w, int h) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        Arrays.fill(pixels, bgColor);
        for (Point p : contour) {
            p = new Point(p.getX() * w / BitmapContext.getWidth(), p.getY() * h / BitmapContext.getHeight());
            pixels[p.getX() + p.getY() * bitmap.getWidth()] = contourColor;
        }
        bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bitmap;
    }
}

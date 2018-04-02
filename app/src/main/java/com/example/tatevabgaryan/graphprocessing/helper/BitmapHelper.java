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
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Tatev.Abgaryan on 1/27/2018.
 */
public class BitmapHelper {

    public Bitmap createBitmapFromPoint(TreeSet<Point> contour) {
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
        for (TreeSet<Point> nodePoints : nodes) {
            contour.addAll(nodePoints);
        }
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Point p = new Point(x, y);
                if (contour.contains(p)) {
                    image.setPixel(x, y, Color.RED);
                } else {
                    image.setPixel(x, y, Color.TRANSPARENT);
                }
            }
        }
        image = Bitmap.createScaledBitmap(image, BitmapContext.getWidth() * MainActivity.SCALE, BitmapContext.getHeight() * MainActivity.SCALE, true);
        return image;
    }

    public Bitmap createNumberBitmapFromIsland(Island island) {
        int minX = Integer.MAX_VALUE, maxX = 0, minY = Integer.MAX_VALUE, maxY = 0;
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        image = Bitmap.createScaledBitmap(image, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {
                Point p = new Point(x, y);
                if (island.getPoints().contains(p)) {
                    image.setPixel(x, y, Color.rgb(0, 0, 0));
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                } else {
                    image.setPixel(x, y, Color.rgb(255, 255, 255));
                }
            }
        }
        return Bitmap.createBitmap(image, minX - 10, minY - 10, maxX - minX + 20, maxY - minY + 20);
    }

    public Bitmap createBitmapFormCameraStream(byte[] bytes) {
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }
}

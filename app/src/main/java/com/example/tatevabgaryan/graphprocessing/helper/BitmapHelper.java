package com.example.tatevabgaryan.graphprocessing.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.ViewGroup;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
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
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth() * MainActivity.SCALE, BitmapContext.getHeight()* MainActivity.SCALE, Bitmap.Config.ARGB_8888);
        image = Bitmap.createScaledBitmap(image, BitmapContext.getWidth()* MainActivity.SCALE, BitmapContext.getHeight()* MainActivity.SCALE, true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Point p = new Point(x/MainActivity.SCALE, y/MainActivity.SCALE);
                if (contour.contains(p)) {
                    image.setPixel(x, y, Color.rgb(0, 0, 0));
                } else {
                    image.setPixel(x, y, Color.TRANSPARENT);
                }
            }
        }
        return image;
    }

    public Bitmap createNumberBitmapFromIsland(Island island) {
        int minX = Integer.MAX_VALUE, maxX = 0, minY = Integer.MAX_VALUE, maxY = 0;
        Bitmap image = Bitmap.createBitmap(BitmapContext.getWidth(), BitmapContext.getHeight(), Bitmap.Config.ARGB_8888);
        image = Bitmap.createScaledBitmap(image, BitmapContext.getWidth(), BitmapContext.getHeight(), true);
        for (int x = 1; x < image.getWidth()-1; x++) {
            for (int y = 1; y < image.getHeight()-1; y++) {
                Point p = new Point(x, y);
                if (island.getPoints().contains(p)) {
                    image.setPixel(x, y, Color.rgb(255, 255, 255));
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                } else {
                    image.setPixel(x, y, Color.rgb(0, 0, 0));
                }
            }
        }
        return Bitmap.createBitmap(image, minX, minY,maxX - minX,maxY - minY);
    }

    public Bitmap createBitmapFormCameraStream(byte[] bytes, ViewGroup.LayoutParams params){

        int maxSize = 816;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
        int srcSize = Math.max(opt.outWidth, opt.outHeight);
        opt.inSampleSize = maxSize < srcSize ? (srcSize / maxSize) : 1;
        opt.inJustDecodeBounds = false;
        Bitmap tmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);

        //Scaling and rotation
        float scale = Math.max((float) maxSize / opt.outWidth, (float) maxSize / opt.outHeight);
        Matrix matrix = new Matrix();
        int size = Math.min(opt.outWidth, opt.outHeight);
        //TODO always 90?
        matrix.setRotate(90);
        matrix.postScale(scale, scale);

        int adj;
        float previewRate = (float) params.width / (float) params.height;
        float cameraRate = (float) opt.outHeight / (float) opt.outWidth;
        if (cameraRate > previewRate) {
            adj = (int) (size * (cameraRate - previewRate) * 0.5);
        } else{
            adj = (int) (size * (previewRate - cameraRate) * 0.5);
        }
        Bitmap source = Bitmap.createBitmap(tmp, adj + (opt.outWidth - size) / 2, adj + (opt.outHeight - size) / 2, size - adj * 2, size - adj * 2, matrix, true);
        return source;
    }
}

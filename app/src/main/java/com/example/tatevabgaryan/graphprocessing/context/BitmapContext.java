package com.example.tatevabgaryan.graphprocessing.context;

/**
 * Created by Tatev.Abgaryan on 1/28/2018.
 */

public class BitmapContext {
    //TODO from camera
    private static int height;
    private static int width;

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int bHeight) {
        height = bHeight;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int bWidth) {
        width = bWidth;
    }
}

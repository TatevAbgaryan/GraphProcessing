package com.example.tatevabgaryan.graphprocessing.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.tatevabgaryan.graphprocessing.ocr.Classification;
import com.example.tatevabgaryan.graphprocessing.ocr.Classifier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Tatev.Abgaryan on 2/17/2018.
 */

public class OCRHelper {
    private static final int INPUT_SIZE = 28;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";

    private static final String MODEL_FILE = "file:///android_asset/expert-graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";
    private static Classifier classifier;
    private static Executor executor = Executors.newSingleThreadExecutor();

    public static void initialize(final Context context) {
        if(classifier == null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        classifier = Classifier.create(context.getAssets(),
                                MODEL_FILE,
                                LABEL_FILE,
                                INPUT_SIZE,
                                INPUT_NAME,
                                OUTPUT_NAME);
                    } catch (final Exception e) {
                        throw new RuntimeException("Error initializing TensorFlow!", e);
                    }
                }
            });
        }
    }

    public int numberFromBitmap(Bitmap numberBitmap) {
        numberBitmap = Bitmap.createScaledBitmap(numberBitmap, INPUT_SIZE, INPUT_SIZE, true);
        int[] pixels = new int[numberBitmap.getWidth() * numberBitmap.getHeight()];
        numberBitmap.getPixels(pixels, 0, numberBitmap.getWidth(), 0, 0, numberBitmap.getWidth(), numberBitmap.getHeight());

        float[] retPixels = new float[pixels.length];
        for (int i = 0; i < pixels.length; ++i) {
            // Set 0 for white and 255 for black pixel
            int pix = pixels[i];
            int b = pix & 0xff;
            retPixels[i] = (float)((0xff - b)/255.0);
        }
        Classification res = classifier.recognize(retPixels);
        Log.d("ofaman recognized =" , res.getLabel());
        return  Integer.valueOf(res.getLabel());
    }
}

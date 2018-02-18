package com.example.tatevabgaryan.graphprocessing.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.tatevabgaryan.graphprocessing.R;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.KNearest;
import org.opencv.ml.Ml;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatev.Abgaryan on 2/17/2018.
 */

public class OCRHelper {
    static {
        OpenCVLoader.initDebug();
       // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public int numberFromBitmap(Bitmap numberBitmap, Context context) {
        Mat digits = new Mat();
        Resources res = context.getResources();
        Bitmap digitsBitmap = BitmapFactory.decodeResource(res, R.drawable.digits);
        Utils.bitmapToMat(digitsBitmap, digits);

        Mat trainData = new Mat();
        List<Integer> trainLabs = new ArrayList<>();
        for (int r = 0; r < 50; r++) {
            for (int c = 0; c < 100; c++) {
                Mat num = digits.submat(new Rect(c * 40, r * 40, 40, 40));
                num.convertTo(num, CvType.CV_32F);
                trainData.push_back(num.reshape(1, 1));
                trainLabs.add(r / 5);
            }
        }
        // make a Mat of the train labels, and train knn:
        KNearest knn = KNearest.create();
        knn.train(trainData, Ml.ROW_SAMPLE, Converters.vector_int_to_Mat(trainLabs));

        numberBitmap = numberBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Mat numberMat = new Mat();
        Utils.bitmapToMat(numberBitmap, numberMat);
        numberMat.convertTo(numberMat, CvType.CV_32F);
        Imgproc.resize(numberMat, numberMat, new Size(40,40));
        return (int)knn.findNearest(numberMat.reshape(1,1), 1, new Mat());
    }
}

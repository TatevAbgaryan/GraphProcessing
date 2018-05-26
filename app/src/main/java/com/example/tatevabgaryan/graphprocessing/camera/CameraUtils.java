package com.example.tatevabgaryan.graphprocessing.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;

/**
 * Created by Tatev.Abgaryan on 3/9/2018.
 */

public class CameraUtils {

    public static int getCameraDisplayOrientation(int cameraId, Context context) {
        int rotation = ((Activity)context).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        result = ((360 - degrees) + info.orientation);

        return result % 360;
    }

}

package com.example.tatevabgaryan.graphprocessing.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.example.tatevabgaryan.graphprocessing.MainActivity;
import com.example.tatevabgaryan.graphprocessing.builder.GraphDirector;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.helper.BitmapHelper;
import com.example.tatevabgaryan.graphprocessing.model.Graph;

import java.io.IOException;
import java.util.List;

public class HolderCallback implements SurfaceHolder.Callback {

    private Camera camera;
    private Context context;

    public HolderCallback(Camera camera, Context context){
        this.camera = camera;
        this.context = context;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(CameraUtils.getCameraDisplayOrientation(0, context));
            camera.startPreview();
            Camera.Parameters p = camera.getParameters();
            p.set("jpeg-quality", 100);
            p.setPictureFormat(PixelFormat.JPEG);
            List<Camera.Size> allSizes = p.getSupportedPictureSizes();
            Camera.Size size = allSizes.get(0); // get top size
            for (int i = 0; i < allSizes.size(); i++) {
                if (allSizes.get(i).width > size.width)
                    size = allSizes.get(i);
            }
            p.setPictureSize(size.width, size.height);
            camera.setParameters(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        camera.stopPreview();
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}
package com.example.tatevabgaryan.graphprocessing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.tatevabgaryan.graphprocessing.builder.GraphDirector;
import com.example.tatevabgaryan.graphprocessing.camera.CameraUtils;
import com.example.tatevabgaryan.graphprocessing.camera.HolderCallback;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.helper.BitmapHelper;
import com.example.tatevabgaryan.graphprocessing.model.Graph;


public class MainActivity extends Activity {
    public static final int SCALE = 2;
    public static final int NODE_RADIUS = 3;
    public static final int NODE_POINT_DISTANCE = 20;

    SurfaceView sv;
    SurfaceHolder holder;
    HolderCallback holderCallback;
    Camera camera;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image_view);
        sv = findViewById(R.id.surfaceView);

        holder = sv.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        releaseCameraAndPreview();
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        holderCallback = new HolderCallback(camera, this);
        holder.addCallback(holderCallback);
        RectF prevRectF = CameraUtils.getPreviewSize(true, camera, this);
        sv.getLayoutParams().height = (int) (prevRectF.bottom);
        sv.getLayoutParams().width = (int) (prevRectF.right);

        sv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("onTouch11", "onTouch entered");
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        int maxSize = 816;
                        camera.stopPreview();
                        BitmapFactory.Options opt = new BitmapFactory.Options();
                        opt.inJustDecodeBounds = true;
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
                        int srcSize = Math.max(opt.outWidth, opt.outHeight);
                        System.out.println("out w:" + opt.outWidth + " h:" + opt.outHeight);

                        opt.inSampleSize = maxSize < srcSize ? (srcSize / maxSize) : 1;

                        System.out.println("sample size " + opt.inSampleSize);

                        opt.inJustDecodeBounds = false;


                        Bitmap tmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);

                        //Scaling and rotation
                        float scale = Math.max((float) maxSize / opt.outWidth, (float) maxSize / opt.outHeight);
                        Matrix matrix = new Matrix();
                        System.out.println("sample out w:" + opt.outWidth + " h:" + opt.outHeight);
                        int size = Math.min(opt.outWidth, opt.outHeight);
                        matrix.setRotate(90);
                        matrix.postScale(scale, scale);

                        int adj;
                        float previewRate = (float) sv.getLayoutParams().width / (float) sv.getLayoutParams().height;
                        float cameraRate = (float) opt.outHeight / (float) opt.outWidth;
                        System.out.println("preview Rate" + previewRate + " camera Rate " + cameraRate);
                        if (cameraRate > previewRate) {
                            adj = (int) (size * (cameraRate - previewRate) * 0.5);
                            System.out.println("rate adjust");
                        } else{
                            adj = (int) (size * (previewRate - cameraRate) * 0.5);
                        }

                        Bitmap source = Bitmap.createBitmap(tmp, adj + (opt.outWidth - size) / 2, adj + (opt.outHeight - size) / 2, size - adj * 2, size - adj * 2, matrix, true);
                        source = Bitmap.createScaledBitmap(source, source.getWidth() / SCALE, source.getHeight() / SCALE, false);
                        imageView.setImageBitmap(source);
                        BitmapContext.setHeight(source.getWidth());
                        BitmapContext.setWidth(source.getWidth());
                        GraphDirector graphDirector = new GraphDirector(source, MainActivity.this);
                        Graph graph = graphDirector.buildGraph();
                        BitmapHelper bitmapHelper = new BitmapHelper();
                        imageView.setImageBitmap(bitmapHelper.createBitmapFromPoint(graph.getGraphIsland().getPoints()));
                    }
                });
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null)
            camera.release();
        camera = null;
    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
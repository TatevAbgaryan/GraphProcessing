package com.example.tatevabgaryan.graphprocessing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
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
import com.example.tatevabgaryan.graphprocessing.helper.GraphHelper;
import com.example.tatevabgaryan.graphprocessing.helper.PathHelper;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class MainActivity extends Activity {
    public static final int SCALE = 5;
    public static final int NODE_RADIUS = 4;
    public static final int NODE_POINT_DISTANCE = 20;

    private SurfaceView sv;
    private SurfaceHolder holder;
    private HolderCallback holderCallback;
    private Camera camera;
    private ImageView imageView;
    private Point touchPoint1 = null;
    private Point touchPoint2 = null;
    private Graph graph;
    private double[][] shortestPaths;
    private BitmapHelper bitmapHelper = new BitmapHelper();
    private int countClick = 0;

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
        final RectF prevRectF = CameraUtils.getPreviewSize(true, camera, this);
        sv.getLayoutParams().height = (int) (prevRectF.bottom);
        sv.getLayoutParams().width = (int) (prevRectF.right);

        sv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, final MotionEvent event) {
                countClick++;
                if (countClick == 1) {
                    touchPoint1 = new Point((int) event.getX() / SCALE, (int) event.getY() / SCALE);
                } else if (countClick % 2 == 0) {
                    touchPoint2 = new Point((int) event.getX() / SCALE, (int) event.getY() / SCALE);
                    camera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] bytes, Camera camera) {
                            //camera.stopPreview();
                            Bitmap source = bitmapHelper.createBitmapFormCameraStream(bytes);
                            graph = processGraph(source);
                            PathHelper pathHelper = new PathHelper();
                            shortestPaths = new double[graph.getNodes().size()][graph.getNodes().size()];
                            pathHelper.fillAllPairShortestPaths(shortestPaths, graph);
                           imageView.setImageBitmap(bitmapHelper.createBitmapFromNodes(graph.getNodes()));
                        }
                    });
                } else {
                    touchPoint1 = new Point((int) event.getX() / SCALE, (int) event.getY() / SCALE);
                }

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

    private Graph processGraph(Bitmap source) {
        source = Bitmap.createScaledBitmap(source, source.getWidth() / SCALE, source.getHeight() / SCALE, false);
        imageView.setImageBitmap(source);
        BitmapContext.setHeight(source.getHeight());
        BitmapContext.setWidth(source.getWidth());
        GraphDirector graphDirector = new GraphDirector(source, MainActivity.this);
        return graphDirector.buildGraph();
    }

    private List<TreeSet<Point>> getShortestPath() {
        GraphHelper graphHelper = new GraphHelper();
        int nodeIndex1 = graphHelper.findNearestNode(graph, touchPoint1);
        int nodeIndex2 = graphHelper.findNearestNode(graph, touchPoint2);
        List<TreeSet<Point>> nodes = graph.getNodes();
        List<TreeSet<Point>> routeNodes = new ArrayList<>();
        double next = nodeIndex1;
        routeNodes.add(nodes.get((int) next));
        while (next != nodeIndex2) {
            next = shortestPaths[(int) next][nodeIndex2];
            routeNodes.add(nodes.get((int) next));
        }
        routeNodes.add(nodes.get(nodeIndex2));
        return routeNodes;
    }
}
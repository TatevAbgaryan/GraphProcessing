package com.example.tatevabgaryan.graphprocessing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tatevabgaryan.graphprocessing.builder.GraphDirector;
import com.example.tatevabgaryan.graphprocessing.camera.HolderCallback;
import com.example.tatevabgaryan.graphprocessing.comparator.PointComparator;
import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.helper.BitmapHelper;
import com.example.tatevabgaryan.graphprocessing.helper.GraphHelper;
import com.example.tatevabgaryan.graphprocessing.helper.OCRHelper;
import com.example.tatevabgaryan.graphprocessing.helper.PathHelper;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;


public class MainActivity extends Activity {
    public static final int SCALE = 12;
    public static final int NODE_RADIUS = 3;
    public static final int NODE_POINT_DISTANCE = 8;

    private SurfaceView sv;
    private SurfaceHolder holder;
    private HolderCallback holderCallback;
    private Camera camera;
    private ImageView imageView;
    private RelativeLayout distanceLayout;
    private TextView distance;
    private Point touchPoint1 = null;
    private Point touchPoint2 = null;
    private Graph graph;
    private double[][] shortestPaths;
    private double[][] shortestDistances;
    private BitmapHelper bitmapHelper = new BitmapHelper();
    private int countClick = 0;
    private int screenWidth;
    private int screenHeight;
    private double shortestPathDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image_view);
        distanceLayout = findViewById(R.id.distance_layout);
        distance = findViewById(R.id.distance);
        distanceLayout.setVisibility(View.GONE);
        sv = findViewById(R.id.surfaceView);
        holder = sv.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        releaseCameraAndPreview();
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        holderCallback = new HolderCallback(camera, this);
        holder.addCallback(holderCallback);
        OCRHelper.initialize(this);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        sv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, final MotionEvent event) {
                distanceLayout.setVisibility(View.GONE);
                highlightPoint(new Point((int) event.getX(), (int) event.getY()));
                countClick++;
                if (countClick == 1) {
                    touchPoint1 = new Point((int) (event.getX() * screenWidth / sv.getWidth()), (int) (event.getY() * screenHeight / sv.getHeight()));
                } else if (countClick % 2 == 0) {
                    touchPoint2 = new Point((int) (event.getX() * screenWidth / sv.getWidth()), (int) (event.getY() * screenHeight / sv.getHeight()));
                    camera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] bytes, Camera camera) {
                            camera.startPreview();
                            Bitmap source = bitmapHelper.createBitmapFormCameraStream(bytes);
                            graph = processGraph(source);

                            PathHelper pathHelper = new PathHelper();
                            shortestPaths = new double[graph.getNodes().size()][graph.getNodes().size()];
                            shortestDistances = new double[graph.getNodes().size()][graph.getNodes().size()];
                            pathHelper.fillAllPairShortestPaths(shortestPaths, shortestDistances, graph);
                            List<TreeSet<Point>> pathNodes = getShortestPath();
                            List<TreeSet<Point>> pathNodesToDraw = new ArrayList<>();
                            final Handler handler = new Handler();
                            Log.d("ofaman shortestPaths", Arrays.deepToString(shortestPaths));
                            Runnable runnable = new Runnable() {
                                int currentNode = 0;

                                public void run() {
                                    pathNodesToDraw.add(pathNodes.get(currentNode));
                                    imageView.setImageBitmap(bitmapHelper.createBitmapFromNodes(pathNodesToDraw, screenWidth, screenHeight));
                                    currentNode++;
                                    if (currentNode < pathNodes.size())
                                        handler.post(this);
                                    else
                                        imageView.setImageBitmap(bitmapHelper.createBitmapFromNodesWithEdges(pathNodes, screenWidth, screenHeight));

                                }
                            };
                            setDistanceImage();
                            handler.post(runnable);
                        }
                    });
                } else {
                    touchPoint1 = new Point((int) (event.getX() * screenWidth / sv.getWidth()), (int) (event.getY() * screenHeight / sv.getHeight()));
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        holderCallback = new HolderCallback(camera, this);
        holder.addCallback(holderCallback);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private Graph processGraph(Bitmap source) {
        source = Bitmap.createScaledBitmap(source, source.getWidth() / SCALE, source.getHeight() / SCALE, false);
        BitmapContext.setHeight(source.getHeight());
        BitmapContext.setWidth(source.getWidth());
        GraphDirector graphDirector = new GraphDirector(source, MainActivity.this);
        return graphDirector.buildGraph();
    }

    private List<TreeSet<Point>> getShortestPath() {
        GraphHelper graphHelper = new GraphHelper();
        touchPoint1.setX(touchPoint1.getX() * BitmapContext.getWidth() / screenWidth);
        touchPoint1.setY(touchPoint1.getY() * BitmapContext.getHeight() / screenHeight);
        touchPoint2.setX(touchPoint2.getX() * BitmapContext.getWidth() / screenWidth);
        touchPoint2.setY(touchPoint2.getY() * BitmapContext.getHeight() / screenHeight);

        int nodeIndex1 = graphHelper.findNearestNode(graph, touchPoint1);
        int nodeIndex2 = graphHelper.findNearestNode(graph, touchPoint2);
        shortestPathDistance = shortestDistances[nodeIndex1][nodeIndex2];
        Log.d("ofaman nodeSize", graph.getNodes().size() + "");
        Log.d("ofaman n1", nodeIndex1 + "");
        Log.d("ofaman n2", nodeIndex2 + "");

        List<TreeSet<Point>> nodes = graph.getNodes();
        List<TreeSet<Point>> routeNodes = new ArrayList<>();
        double next = nodeIndex1;
        routeNodes.add(nodes.get((int) next));
        while (next != nodeIndex2) {
            next = shortestPaths[(int) next][nodeIndex2];
            routeNodes.add(nodes.get((int) next));
            Log.d("ofaman next", next + "");
        }
        return routeNodes;
    }

    private void highlightPoint(Point point) {
        Bitmap bm = Bitmap.createBitmap(sv.getWidth(), sv.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.argb(190, 220, 20, 60));
        c.drawCircle(point.getX(), point.getY(), 30, p);
        imageView.setImageBitmap(bm);
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                imageView.setImageBitmap(null);
            }
        };
        handler.postDelayed(r, 500);
    }

    private void setDistanceImage() {
        if (shortestPathDistance != Double.MAX_VALUE) {
            distance.setText(String.valueOf((int) shortestPathDistance));
            distanceLayout.setVisibility(View.VISIBLE);
        }
    }
}
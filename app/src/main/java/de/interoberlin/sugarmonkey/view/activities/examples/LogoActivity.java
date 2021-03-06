package de.interoberlin.sugarmonkey.view.activities.examples;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import de.interoberlin.sauvignon.lib.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.lib.model.svg.SVG;
import de.interoberlin.sauvignon.lib.model.svg.elements.circle.SVGCircle;
import de.interoberlin.sauvignon.lib.model.svg.transform.transform.SVGTransformTranslate;
import de.interoberlin.sauvignon.lib.model.util.SVGPaint;
import de.interoberlin.sauvignon.lib.model.util.Vector2;
import de.interoberlin.sauvignon.lib.view.DebugLine;
import de.interoberlin.sauvignon.lib.view.SVGPanel;
import de.interoberlin.sugarmonkey.R;
import de.interoberlin.sugarmonkey.controller.SugarMonkeyController;

// import de.interoberlin.sauvignon.lib.

public class LogoActivity extends Activity {
    private static Context context;
    private static Activity activity;
    // private static SugarMonkeyController controller;

    private static SensorManager sensorManager;
    private WindowManager windowManager;
    private static Display display;

    private static SVG svg;
    private static SVGPanel panel;

    private static LinearLayout lnr;
    private static DebugLine dlFps;

    private static boolean running = false;
    private static long startTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Get activity and context
        activity = this;
        context = getApplicationContext();

        // Get instances of managers
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

        svg = SvgLoader.getSVGFromAsset(context, "logo.svg");

        panel = new SVGPanel(activity);
        panel.setSVG(svg);
        panel.setBackgroundColor(new SVGPaint(255, 200, 200, 200));
        panel.setBoundingRectsParallelToAxes(true);

        // Add surface view
        activity.addContentView(panel, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        panel.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Read values
                float x = event.getX();
                float y = event.getY();

                // Vibrate
                // ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);

                // Inform panel
                panel.setTouch(new Vector2(x, y));

                return true;
            }
        });

        // Initialize
        uiInit();
    }

    public void onResume() {
        super.onResume();
        panel.resume();
        running = true;
        startTime = System.currentTimeMillis();
        uiUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        panel.pause();
        running = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public Display getDisplay() {
        return display;
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public static void draw() {
        if (lnr != null) {
            lnr.removeAllViews();

            // Add debug lines
            dlFps = new DebugLine(activity, "FPS", String.valueOf(SugarMonkeyController.getFps()), String.valueOf(SugarMonkeyController.getCurrentFps()));

            lnr.setOrientation(LinearLayout.VERTICAL);
            lnr.addView(dlFps, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * Initializes svg
     */
    public static void uiInit() {
        synchronized (svg) {

        }
    }

    public static void uiUpdate() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    synchronized (svg) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {

                        }

                        // Get time since start
                        long time = System.currentTimeMillis() - startTime;

                        // Get elements
                        SVGCircle c1 = (SVGCircle) svg.getElementById("c1");

                        int angle = (int) time % 36;

                        float d = 30;
                        float x = (float) (d * Math.sin(90 - angle));
                        float y = (float) (d * Math.sin(angle));

                        c1.setAnimationTransform(new SVGTransformTranslate(x, y));
                        // c1.setAnimate()
                    }
                }
            }
        }
        );

        t.start();
    }

    public static void uiDraw() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                draw();
            }
        });
    }

    public static void uiToast(final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
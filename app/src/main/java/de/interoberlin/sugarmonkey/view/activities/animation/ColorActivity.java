package de.interoberlin.sugarmonkey.view.activities.animation;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.interoberlin.sauvignon.lib.controller.loader.SvgLoader;
import de.interoberlin.sauvignon.lib.model.smil.AAnimate;
import de.interoberlin.sauvignon.lib.model.smil.AnimateColor;
import de.interoberlin.sauvignon.lib.model.svg.SVG;
import de.interoberlin.sauvignon.lib.model.svg.elements.rect.SVGRect;
import de.interoberlin.sauvignon.lib.model.util.SVGPaint;
import de.interoberlin.sauvignon.lib.model.util.Vector2;
import de.interoberlin.sauvignon.lib.view.SVGPanel;
import de.interoberlin.sugarmonkey.R;

public class ColorActivity extends Activity
{
	private static Context			context;
	private static Activity			activity;
	// private static SugarMonkeyController controller;

	private static SensorManager	sensorManager;
	private WindowManager			windowManager;
	private static Display			display;

	private static SVG svg;
	private static SVGPanel panel;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// Get activity and context
		activity = this;
		context = getApplicationContext();

		// Get instances of managers
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();

		svg = SvgLoader.getSVGFromAsset(context, "rect.svg");

		panel = new SVGPanel(activity);
		panel.setSVG(svg);
		panel.setBackgroundColor(new SVGPaint(255, 200, 200, 200));

		// Add surface view
		activity.addContentView(panel, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		panel.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
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

		// Get controller
		// controller = (SugarMonkeyController) getApplicationContext();

		// Initialize
		uiInit();
	}

	public void onResume()
	{
		super.onResume();
		panel.resume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		panel.pause();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	public Display getDisplay()
	{
		return display;
	}

	public SensorManager getSensorManager()
	{
		return sensorManager;
	}

	public static void uiInit()
	{
		synchronized (svg)
		{
			SVGRect r = (SVGRect) svg.getElementById("r");

			AnimateColor a = new AnimateColor();
			a.setBegin("0");
			a.setFrom("black");
			a.setTo("red");
			a.setDur("10");
			a.setRepeatCount("1000");

			List<AAnimate> animations = new ArrayList<AAnimate>();
			animations.add(a);
			r.setAnimations(animations);
		}
	}

	public static void uiDraw()
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
			}
		});
	}

	public static void uiToast(final String message)
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
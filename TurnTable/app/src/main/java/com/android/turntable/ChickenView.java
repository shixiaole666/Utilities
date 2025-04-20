package com.android.turntable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ChickenView extends View {

    private Paint bodyPaint;
    private Paint eyePaint;
    private Paint beakPaint;
    public static String TAG = "ChickenView";

    public ChickenView(Context context) {
        super(context);
        Log.d(TAG, "ChickenView: lele00");
        init();
    }

    public ChickenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "ChickenView: lele01");
        init();
    }

    public ChickenView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.d(TAG, "ChickenView: lele02");
        init();
    }

    private void init() {
        bodyPaint = new Paint();
        bodyPaint.setColor(Color.YELLOW);
        bodyPaint.setStyle(Paint.Style.FILL);

        eyePaint = new Paint();
        eyePaint.setColor(Color.BLACK);
        eyePaint.setStyle(Paint.Style.FILL);

        beakPaint = new Paint();
        beakPaint.setColor(Color.RED);
        beakPaint.setStyle(Paint.Style.FILL);
        Log.d(TAG, "init: lele");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画鸡的身体
        canvas.drawCircle(200, 200, 100, bodyPaint);

        // 画鸡的眼睛
        canvas.drawCircle(170, 180, 10, eyePaint);
        canvas.drawCircle(230, 180, 10, eyePaint);

        // 画鸡的嘴
        Path path = new Path();
        path.moveTo(200, 200);
        path.lineTo(180, 220);
        path.lineTo(220, 220);
        path.close();
        canvas.drawPath(path, beakPaint);
        Log.d(TAG, "onDraw: lele");
    }
}
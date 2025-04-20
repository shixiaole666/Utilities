package com.android.turntable;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.android.turntable.utils.PreferencesUtils;

import java.util.Random;

public class RollingNumberView extends View {

    private Paint paint;
    private int number = 0;
    public static String TAG = "RollingNumberView";
    private ValueAnimator valueAnimator;
    private String[] selects = PreferencesUtils.getString(getContext(), "roll_selects", "A,B,C,D,E,F,G,H").split("[,; ，；]+"); // selects数组
    private int selected;
    public RollingNumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);
        valueAnimator = ValueAnimator.ofInt(0, 3 * selects.length + selected);
        valueAnimator.setDuration(5000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, "onAnimationUpdate: num:" + number);
                number = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    public void startRolling() {
        Random random = new Random();
        selected = random.nextInt(selects.length);
        Log.d(TAG, "init: startRolling:" + selected);
        Log.d(TAG, "startRolling: nums:" + (3 * selects.length + selected));
        init();
        valueAnimator.start();

    }

    public void setSelects(String[] selects) {
        this.selects = selects;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: show:" +selects[number%selects.length]);
        canvas.drawText(selects[number%selects.length], getWidth() / 2, getHeight() / 2, paint);
    }
}
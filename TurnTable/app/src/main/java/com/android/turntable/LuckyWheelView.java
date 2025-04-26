package com.android.turntable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.turntable.utils.PreferencesUtils;

import java.util.Random;

public class LuckyWheelView extends View {

    private String[] prizes = PreferencesUtils.getString(getContext(), "luck_selects", "A,B,C,D,E,F,G,H").split("[,; ，；]+"); // 奖品数组
    private int[] colors = {Color.parseColor("#f9d3e3"), Color.parseColor("#e60012"), Color.parseColor("#efefef"), Color.parseColor("#99806c"), Color.parseColor("#1a2847"), Color.parseColor("#f0c2a2"), Color.parseColor("#fffbc7"),
            Color.parseColor("#a67eb7"), Color.parseColor("#d4e5ef"), Color.parseColor("#87c0ca"), Color.parseColor("#4c8045"), Color.parseColor("#c6beb1")}; // 扇形颜色
    private Paint paint;
    private Paint arrowPaint;
    private float[] angles; // 扇形角度

    private int currentPrizeIndex; // 当前选中的奖品
    private boolean isRunning; // 是否正在旋转
    private float currentAngle; // 当前旋转角度
    private float targetAngle; // 目标旋转角度
    private OnStopListener onStopListener; // 停止旋转监听器
    public static String TAG = "LuckyWheelView";
    private int select;
    private float angle;

    public LuckyWheelView(Context context) {
        super(context);
        Log.d(TAG, "LuckyWheelView: sdw00");
        init();
    }

    public LuckyWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "LuckyWheelView: to sdw01");
        init();
    }

    public LuckyWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "LuckyWheelView: sdw02");
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextSize(60);
        arrowPaint = new Paint();
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPaint.setAntiAlias(true);
        arrowPaint.setTextSize(50);
        angles = new float[prizes.length];
        angle = 360f / prizes.length;
        for (int i = 0; i < prizes.length; i++) {
            angles[i] = i * angle;
        }
        invalidate();
    }

    public void setPrizes(String[] prizes) {
        this.prizes = prizes;
        angles = new float[prizes.length];
        angle = 360f / prizes.length;
        for (int i = 0; i < prizes.length; i++) {
            angles[i] = i * angle;
        }

    }

    public void start() {
        if (!isRunning) {
            currentAngle = 0;
            Random random = new Random();
            select = random.nextInt(360);
            targetAngle = 360 * 3 + select;

            isRunning = true;
            post(new Runnable() {
                @Override
                public void run() {
                    rotate();
                }
            });
        }
    }

    private void rotate() {
        currentAngle += 5;
        if (currentAngle >= targetAngle) {
            currentAngle = targetAngle;
            isRunning = false;
            if (onStopListener != null) {
                Log.d(TAG, "rotate: select:"+select);
                if (select >= 355)
                    select = 354;

                int selectPrize = select <= 45 ? prizes.length - 1 : (int) (((select/5)*5-45)/angle);
                Log.d(TAG, "rotate: selectPrize:"+((select/5)*5+5));
                onStopListener.onStop(prizes[prizes.length - 1 - selectPrize]);
            }
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    rotate();
                }
            });
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 16;
        int centerX = width / 2;
        int centerY = height / 2;

        // 绘制扇形
        for (int i = 0; i < prizes.length; i++) {
            paint.setColor(colors[i % colors.length]);
            paint.setAlpha(128);
            float startAngle = angles[i] + currentAngle;
            float sweepAngle = 360f / prizes.length;
            float textAngle = startAngle + sweepAngle / 2;
            // 计算文本的长度
            String text = prizes[i];
            float textWidth = paint.measureText(text);

            // 计算文本的起始位置
            float angleInRadians = (float) Math.toRadians(textAngle);
            float x = centerX + (float) (0.8 * radius * Math.cos(angleInRadians)) - textWidth / 2; // 0.8 是为了让文本居中显示在圆弧内部
            float y = centerY + (float) (0.8 * radius * Math.sin(angleInRadians)) + textWidth / 2; // 0.8 是为了让文本居中显示在圆弧内部

            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, angles[i] + currentAngle, 360f / prizes.length, true, paint);
            // 绘制居中的文本
            Log.d(TAG, "onDraw: text:" + text);
            paint.setColor(Color.BLACK);
            canvas.drawText(text, x, y, paint);
            // 恢复画笔的颜色
            paint.setColor(colors[i % colors.length]);
        }

//        // 绘制指针
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(8);
//        canvas.drawLine(centerX, centerY - radius, centerX, centerY - radius - 100, paint);

        // 计算指针终点的坐标
        float pointerAngle = 45;  // 指针右偏30度
        float pointerLength = radius / 2;  // 指针长度为圆半径的一半
        float pointerEndX = centerX + (float) (pointerLength * Math.cos(Math.toRadians(pointerAngle)));
        float pointerEndY = centerY + (float) (pointerLength * Math.sin(Math.toRadians(pointerAngle)));

        // 绘制指针
        arrowPaint.setColor(Color.parseColor("#FF99DD"));
        arrowPaint.setStrokeWidth(8);
        canvas.drawLine(centerX, centerY, pointerEndX, pointerEndY, arrowPaint);

        // 绘制指针箭头
        Path arrowPath = new Path();
//        arrowPath.moveTo(pointerEndX + 20, pointerEndY + 20);
//        arrowPath.lineTo(pointerEndX + 20, pointerEndY - 20);
//        arrowPath.lineTo(pointerEndX - 20, pointerEndY + 20);
        arrowPath.addCircle(pointerEndX, pointerEndY, 50, Path.Direction.CW);
        arrowPath.close();
        canvas.drawPath(arrowPath, arrowPaint);

        // 判断是否停止转动
        if (isRunning) {
//            float angle = currentAngle % 360;
////            for (int i = 0; i < prizes.length; i++) {
////                if (angle >= angles[i] && angle < angles[i] + 360f / prizes.length) {
////                    currentPrizeIndex = i;
////                    break;
////                }
////            }

        }
    }

    public void setOnStopListener(OnStopListener onStopListener) {
        this.onStopListener = onStopListener;
    }

    public interface OnStopListener {
        void onStop(String prize);
    }
}
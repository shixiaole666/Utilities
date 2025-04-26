package com.android.turntable.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DraggableFab extends FloatingActionButton {
    private int lastAction =  MotionEvent.ACTION_DOWN; // 上次的点击类型
    private float startX; // 开始的位置X
    private float startY; // 开始的位置Y
    private float lastX; // 最开始的位置X
    private float lastY; // 最开始的位置Y
    private int parentWidth;// 父布局的宽
    private int parentHeight; // 父布局的高


    public DraggableFab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        // 可以点击
        setPressed(true);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                getParentDimensions();
                lastAction = MotionEvent.ACTION_DOWN;
                startX = getX() - event.getRawX();
                startY = getY() - event.getRawY();
                lastX = getX();
                lastY = getY();
                break;

            case MotionEvent.ACTION_MOVE:
                lastAction = MotionEvent.ACTION_MOVE;
                setX(event.getRawX() + startX);// 设置位置
                setY(event.getRawY() + startY);
                checkBoundaries();
                // 返回true消费该事件
                break;

            case MotionEvent.ACTION_UP:
                // 没有移动
                if (lastAction == MotionEvent.ACTION_DOWN) {
                    // 移动像素小于10
                } else if (Math.abs(getX() - lastX) < 10 && Math.abs(getY() - lastY) < 10) {
                } else {
                    // 变成点击
                    lastAction = MotionEvent.ACTION_DOWN;
                    // 设置不可点击
                    setPressed(false);
                }
                break;
            default:
                return false;
        }
        // 保留原有的水波纹效果
        return super.onTouchEvent(event);

    }

    /**
     * 获取父布局宽高
     */
    private void getParentDimensions() {
        if (getParent() != null && getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) getParent();
            parentWidth = parent.getWidth();
            parentHeight = parent.getHeight();
        }
    }

    /**
     * 边界限制
     */
    private void checkBoundaries() {
        // 左边
        if (getX() < 0) {
            setX(0);
        }
        if (getY() < 0) {
            setY(0);
        }
        // 这里可以根据所需隐藏部分View在屏幕外，我这里使用了工具类，将17转化为17sp，有兴趣的同学可以在评论区留言要，当然如果不需要隐藏View，只需要改为注释代码即可
//        if (getX() > parentWidth - getWidth()+ DensityUtil.dip2px(getContext(),17)) {
//            setX(parentWidth - getWidth()+ DensityUtil.dip2px(getContext(),17));
//        }
//        if (getY() > parentHeight - getHeight()) {
//            setY(parentHeight - getHeight());
//        }
        if (getX() > parentWidth - getWidth()) {
            setX(parentWidth - getWidth());
        }
        if (getY() > parentHeight - getHeight()) {
            setY(parentHeight - getHeight());
        }
    }

}
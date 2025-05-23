package com.android.turntable.views;


import static android.widget.RelativeLayout.CENTER_HORIZONTAL;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.turntable.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 21-8-11 下午4:32
 * @author lkk
 * @Description  数字解锁键盘View
 */

public class UnLockPanel extends LinearLayout {

    private static final String TAG = "NumLockPanel";
    private String[] numArr = new String[]{"1","2","3","4","5","6","7","8","9", "", "0"};

    private int mPaddingLeftRight;
    private int mPaddingTopBottom;
    //4个密码位ImageView
    private ArrayList<CircleImageView> mResultIvList;

    private LinearLayout inputResultView;
    //存储当前输入内容
    private StringBuilder mPassWord;
    //振动效果
    private Vibrator mVibrator;
    //整个键盘的颜色
    private int mPanelColor;
    //4个密码位的宽度
    private int mResultIvRadius;
    //数字键盘的每个圆的宽度
    private int mNumRadius;
    //每个圆的边界宽度
    private int mStrokeWidth;

    private boolean NumisUse =true;//键盘是否可用
    private boolean InterceptTouchVal=false; //是否拦截事件
    private GridLayout numContainer;


    public UnLockPanel(Context context) {
        this(context, null);

    }

    public UnLockPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public UnLockPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaddingLeftRight = dip2px(context, 15);
        mPaddingTopBottom = dip2px(context, 10);
//        mPanelColor = Color.BLACK; //颜色代码可采用Color.parse("#000000");
        mPanelColor = Color.WHITE; //颜色代码可采用Color.parse("#ffffff");
        mResultIvRadius = dip2px(context, 24);
        mNumRadius = dip2px(context, 70);
        mStrokeWidth = dip2px(context, 1);
        mVibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
        mResultIvList = new ArrayList<>();
        mPassWord = new StringBuilder();
        setOrientation(VERTICAL);
        setGravity(CENTER_HORIZONTAL);
        initView(context);

    }

    public void initView(Context context){
        //4个结果号码
        inputResultView = new LinearLayout(context);
        for(int i=0; i<4; i++){
            CircleImageView mResultItem = new CircleImageView(context);
            mResultIvList.add(mResultItem);
            LayoutParams params = new LayoutParams(mResultIvRadius, mResultIvRadius);
            params.leftMargin = dip2px(context, 10);
            params.rightMargin = dip2px(context, 10);
            mResultItem.setPadding(dip2px(context, 2),dip2px(context, 2),dip2px(context, 2),dip2px(context, 2));
            mResultItem.setLayoutParams(params);
            inputResultView.addView(mResultItem);
        }
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = dip2px(context, 10);
        inputResultView.setLayoutParams(params);
        addView(inputResultView);

        //数字键盘
        numContainer = new GridLayout(context);
        numContainer.setColumnCount(3);
        for(int i=0; i<numArr.length; i++){
            RelativeLayout numItem = new RelativeLayout(context);
            numItem.setPadding(mPaddingLeftRight,mPaddingTopBottom,mPaddingLeftRight,mPaddingTopBottom);
            RelativeLayout.LayoutParams gridItemParams = new RelativeLayout.LayoutParams(mNumRadius, mNumRadius);
            gridItemParams.addRule(CENTER_IN_PARENT);
            final TextView numTv = new TextView(context);
            numTv.setText(numArr[i]);
            numTv.setTextColor(mPanelColor);
            numTv.setTextSize(30);
            numTv.setGravity(Gravity.CENTER);
            numTv.setLayoutParams(gridItemParams);
            final CircleImageView numBgIv = new CircleImageView(context);
            numBgIv.setLayoutParams(gridItemParams);
            numTv.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            if(!NumisUse){
                                return true;
                            }
                            numBgIv.setFillCircle();
                            numTv.setTextColor(Color.BLACK);
                            if(mPassWord.length() < 4){
                                mPassWord.append(numTv.getText());
                                mResultIvList.get(mPassWord.length()-1).setFillCircle();
                                if(mInputListener!=null && mPassWord.length() == 4){
                                    mInputListener.inputFinish(mPassWord.toString());
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            numBgIv.setStrokeCircle();
                            numTv.setTextColor(mPanelColor);
                            if (!NumisUse){
                                InterceptTouchVal=true;
                            }
                            break;
                    }

                    return true;
                }
            });
            numItem.addView(numBgIv);
            numItem.addView(numTv);
            numContainer.addView(numItem);
            if(i == 9){
                numItem.setVisibility(INVISIBLE); // i = 9 button invisible
            }
        }

        //删除按钮
        RelativeLayout deleteItem = new RelativeLayout(context);
        deleteItem.setPadding(mPaddingLeftRight,mPaddingTopBottom,mPaddingLeftRight,mPaddingTopBottom);
        RelativeLayout.LayoutParams gridItemParams = new RelativeLayout.LayoutParams(mNumRadius, mNumRadius);
        gridItemParams.addRule(CENTER_IN_PARENT);
        //假如删除按钮是设置自定义图片资源的话，可用注释这段
        ImageView deleteIv = new ImageView(context);
        deleteIv.setImageResource(R.drawable.ic_delete);
        deleteIv.setLayoutParams(gridItemParams);
        deleteItem.addView(deleteIv);

        //假如删除按钮是设置自定义文字资源的话，可用注释这段
//        TextView deleteTv = new TextView(context);
//        deleteTv.setText("Delete");
//        deleteTv.setTextColor(mPanelColor);
//        deleteTv.setTextSize(dip2px(context, 8));
//        deleteTv.setLayoutParams(gridItemParams);
//        deleteTv.setGravity(Gravity.CENTER);
//        deleteItem.addView(deleteTv);
        numContainer.addView(deleteItem);
        deleteIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        LayoutParams gridParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gridParams.gravity = Gravity.CENTER_HORIZONTAL;
        numContainer.setLayoutParams(gridParams);
        addView(numContainer);

    }

    /**
     * 输入错误的状态显示(包括震动，密码位左右摇摆效果，重置密码位)
     */
    public void showErrorStatus(){
        mVibrator.vibrate(new long[]{100,100,100,100},-1);
        List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(inputResultView, "translationX", -50.0f,50.0f,-50.0f,0.0f);
        translationXAnim.setDuration(400);
        animators.add(translationXAnim);
        AnimatorSet btnSexAnimatorSet = new AnimatorSet();
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.start();
        btnSexAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetResult();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    /**
     * 删除
     */
    public void delete(){
        if(mPassWord.length() == 0){
            return;
        }
        mResultIvList.get(mPassWord.length()-1).setStrokeCircle();
        mPassWord.deleteCharAt(mPassWord.length()-1);
        mDeleInputListener.inputDeleNum(mPassWord.length());

    }

    /**
     * 重置密码输入
     */
    public void resetResult(){
        for(int i=0; i<mResultIvList.size(); i++){
            mResultIvList.get(i).setStrokeCircle();
        }
        mPassWord.delete(0, 4);   // (0,4) 包含左边，不包含右边，即0,1,2,3

    }

    /**
     * 监听输入完毕的接口
     */
    private InputListener mInputListener;

    public void setInputListener(InputListener mInputListener) {
        this.mInputListener = mInputListener;

    }

    public interface InputListener{
        void inputFinish(String result);
    }


    /**
     * 删除 输入完毕接口
     */

    private DeleInputListener mDeleInputListener;

    public void setDeleInputListener(DeleInputListener mDeleInputListener){
        this.mDeleInputListener=mDeleInputListener;

    }

    public interface DeleInputListener{
        void inputDeleNum(int result);
    }



    /**
     * dip/dp转像素
     *
     * @param dipValue
     *            dip或 dp大小
     * @return 像素值
     */
    public static int dip2px(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dipValue * (metrics.density) + 0.5f);
    }

    /**
     * 圆形背景ImageView（设置实心或空心）
     */
    public class CircleImageView extends androidx.appcompat.widget.AppCompatImageView {

        private Paint mPaint;
        private int mWidth;
        private int mHeight;

        public CircleImageView(Context context) {
            this(context, null);

        }

        public CircleImageView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);

        }

        public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView(context);

        }

        public void initView(Context context){
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mPanelColor);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setAntiAlias(true);

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mWidth = w;
            mHeight = h;

        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(mWidth/2, mHeight/2, mWidth/2 - 6, mPaint);
            super.draw(canvas);

        }

        /**
         * 设置圆为实心状态
         */
        public void setFillCircle(){
            mPaint.setStyle(Paint.Style.FILL);
            invalidate();

        }

        /**
         * 设置圆为空心状态
         */
        public void setStrokeCircle(){
            mPaint.setStyle(Paint.Style.STROKE);
            invalidate();

        }
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) { // 拦截
//        if (NumisUse){
//            return false;
//        }
//        return InterceptTouchVal;
//    }

    public void setGridLayoutItemClickable(boolean numisUse){
        NumisUse=numisUse;
        InterceptTouchVal=false;
//        for (int i=0; i<numContainer.getChildCount(); i++){
//            if (i<8){ //处理键盘颜色
//                RelativeLayout view= (RelativeLayout) numContainer.getChildAt(i);
//                TextView txview= (TextView) view.getChildAt(1);
//                txview.setTextColor(Color.parseColor("#727272"));
//            }
//        }

    }

}
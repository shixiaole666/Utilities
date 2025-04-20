package com.android.turntable;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.turntable.clock.AlarmActivity;
import com.android.turntable.fragment.ArinFragment;
import com.android.turntable.fragment.IreneFragment;
import com.android.turntable.fragment.SuzyFragment;
import com.android.turntable.fragment.WallpaperActivity;
import com.android.turntable.fragment.YukeeFragment;
import com.android.turntable.service.AppLockService;
import com.android.turntable.utils.PreferencesUtils;
import com.android.turntable.utils.TransparentStatusBar;
import com.android.turntable.views.DraggableFab;
import com.android.turntable.views.UnLockPanel;
import com.android.turntable.views.WallPaperAdapter;
import com.android.turntable.views.Wallpaper;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class YukeeActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "YukeeActivity";
    // 悬浮窗口的权限请求代码
    private static final int OVERLAY_PERMISSION_REQ_CODE = 1;
    private int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 2;

    /**
     * 用于展示消息的Fragment
     */
    private YukeeFragment yukeeFragment;

    /**
     * 用于展示联系人的Fragment
     */
    private IreneFragment ireneFragment;

    /**
     * 用于展示动态的Fragment
     */
    private ArinFragment arinFragment;

    /**
     * 用于展示设置的Fragment
     */
    private SuzyFragment suzyFragment;

    /**
     * 消息界面布局
     */
    private View yukeeLayout;

    /**
     * 联系人界面布局
     */
    private View ireneLayout;

    /**
     * 动态界面布局
     */
    private View arinLayout;

    /**
     * 设置界面布局
     */
    private View suzyLayout;

    /**
     * 在Tab布局上显示消息图标的控件
     */
    private ImageView yukeeImage;

    /**
     * 在Tab布局上显示联系人图标的控件
     */
    private ImageView ireneImage;

    /**
     * 在Tab布局上显示动态图标的控件
     */
    private ImageView arinImage;

    /**
     * 在Tab布局上显示设置图标的控件
     */
    private ImageView suzyImage;

    /**
     * 在Tab布局上显示消息标题的控件
     */
    private TextView yukeeText;

    /**
     * 在Tab布局上显示联系人标题的控件
     */
    private TextView ireneText;

    /**
     * 在Tab布局上显示动态标题的控件
     */
    private TextView arinText;

    /**
     * 在Tab布局上显示设置标题的控件
     */
    private TextView suzyText;

    private ImageView menuButton;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private TextView timeTextView;
    private Handler handler;
    private Runnable updateTimeRunnable;
    private View floatingWidget;
    private WindowManager windowManager;
    private View unlockView;
    private boolean threadIsTerminate = false;
    private ActivityManager activityManager;
    private PackageManager packageManager;
    private ApplicationInfo mAppInfo;
    private ImageView mUnLockIcon;
    private TextView mUnLockText, mUnlockFailTip;
    private String pkgName, appLabel;
    private Drawable iconDrawable;
    private UnLockPanel mLockNumView;
    private String password = "0328";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yukee);
        TransparentStatusBar.transparentStatusBar(getWindow());
        TransparentStatusBar.transparentNavBar(getWindow());
        // 初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();
        threadIsTerminate = true;
        // 第一次启动时选中第0个tab
        setTabSelection(0);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 处理菜单项点击事件
                switch (item.getItemId()) {
                    case R.id.menu_item1:
                        // 壁纸
                        startActivity(new Intent(YukeeActivity.this, WallpaperActivity.class));
                        break;
                    case R.id.menu_item2:
                        // 悬浮时钟
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(YukeeActivity.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                        } else {
                            if (floatingWidget == null) {
                                showFloatingWidget(); // 显示悬浮窗口
                                handler.post(updateTimeRunnable); // 开始更新时间
                            } else {
                                removeFloatingWidget();
                                handler.removeCallbacks(updateTimeRunnable); // 停止更新时间

                            }
                        }
                        break;
                    // 应用锁
                    case R.id.menu_item3:
                        if (ContextCompat.checkSelfPermission(YukeeActivity.this, Manifest.permission.PACKAGE_USAGE_STATS) != PackageManager.PERMISSION_GRANTED) {
                            // 没有权限，需要请求权限
//                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PACKAGE_USAGE_STATS}, REQUEST_CODE);
//                            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                            startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(new Intent(YukeeActivity.this, AppLockActivity.class));
                        } else {
                            startActivity(new Intent(YukeeActivity.this, AppLockActivity.class));
                        }
                        break;
                    // Clock
                    case R.id.menu_item4:
                        startActivity(new Intent(YukeeActivity.this, AlarmActivity.class));
                        break;
                    // widget
                    case R.id.menu_item5:
                        startActivity(new Intent(YukeeActivity.this, TodoActivity.class));
                        break;

                    // 其他
                    default:


                }
                // 关闭侧边菜单
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        DraggableFab fab = findViewById(R.id.fab);

        // 设置悬浮球按钮点击事件
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 在点击事件中执行相应的操作
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        handler = new Handler();
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                handler.postDelayed(this, 1000); // 每隔一秒更新一次时间
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void checkForegroundApp() {
        Log.d(TAG, "checkForegroundApp: sdw00");
        while (threadIsTerminate) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "checkForegroundApp: sdw01 " + getTopPackageName(this) + PreferencesUtils.getBoolean(this, getTopPackageName(this), false));
            if (PreferencesUtils.getBoolean(this, getTopPackageName(this), false)) {
                Log.d(TAG, "checkForegroundApp: sdw02");
                pkgName  = getTopPackageName(this);
                if (unlockView == null)
                    Log.d(TAG, "checkForegroundApp: sdw123");
                unLockView();
            }
        }
    }

    /**
     * 获取栈顶应用包名
     */
    public String getTopPackageName(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            //5.0以后需要用这方法
            Log.d(TAG, "getTopPackageName: sdw");
            UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();

                }
            }
            if (!android.text.TextUtils.isEmpty(result)) {
                return result;
            }
        }
        return "";
    }

    private void unLockView() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT
        );

        unlockView = LayoutInflater.from(this).inflate(R.layout.view_unlock, null);
        mUnLockIcon = unlockView.findViewById(R.id.unlock_icon);
        mUnLockText = unlockView.findViewById(R.id.unlock_text);
        mUnlockFailTip = unlockView.findViewById(R.id.unlock_fail_tip);
        mLockNumView = unlockView.findViewById(R.id.unlock_lock_view);
        initLockNumResult();
        try {
            mAppInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (mAppInfo != null) {
                iconDrawable = packageManager.getApplicationIcon(mAppInfo);
                appLabel = packageManager.getApplicationLabel(mAppInfo).toString();
                mUnLockIcon.setImageDrawable(iconDrawable);
                mUnLockText.setText(appLabel);

                mLockNumView.setDeleInputListener(new UnLockPanel.DeleInputListener() {
                    @Override
                    public void inputDeleNum(int result) {

                    }
                });

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        windowManager.addView(unlockView, params);
    }

    private void removeUnlockView() {
        if (unlockView != null && windowManager != null) {
            windowManager.removeView(unlockView);
            unlockView = null;
        }
    }

    private void initLockNumResult() {
        mLockNumView.setInputListener(new UnLockPanel.InputListener() {
            @RequiresApi(api = Build.VERSION_CODES.N_MR1)
            @Override
            public void inputFinish(String result) {
                if (result.equals(password)) {//解锁成功,更改数据库状态
                    Log.d(TAG, "inputFinish: sdw123 successful");
                    removeUnlockView();
                } else {
                    inputError();
                }
            }
        });
    }

    /**
     * 输入密码错误
     */
    private void inputError() {
//        mUnlockFailTip.setText(R.string.password_enter);
        Log.d(TAG, "inputError: sdw123 error");

    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        Log.d(TAG, "updateTime: Date: " + currentTime);
        Log.d(TAG, "updateTime: calendar: " + sdf.format(Calendar.getInstance().getTime()));
        timeTextView.setText(currentTime);
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        yukeeLayout = findViewById(R.id.yukee_layout);
        ireneLayout = findViewById(R.id.irene_layout);
        arinLayout = findViewById(R.id.arin_layout);
        suzyLayout = findViewById(R.id.suzy_layout);
        yukeeImage = (ImageView) findViewById(R.id.yukee_image);
        ireneImage = (ImageView) findViewById(R.id.irene_image);
        arinImage = (ImageView) findViewById(R.id.arin_image);
        suzyImage = (ImageView) findViewById(R.id.suzy_image);
        yukeeText = (TextView) findViewById(R.id.yukee_text);
        ireneText = (TextView) findViewById(R.id.irene_text);
        arinText = (TextView) findViewById(R.id.arin_text);
        suzyText = (TextView) findViewById(R.id.suzy_text);
//        menuButton = findViewById(R.id.menu);
//        menuButton.setOnClickListener(this);
        yukeeLayout.setOnClickListener(this);
        ireneLayout.setOnClickListener(this);
        arinLayout.setOnClickListener(this);
        suzyLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yukee_layout:
                // 当点击了消息tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.irene_layout:
                // 当点击了联系人tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.arin_layout:
                // 当点击了动态tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.suzy_layout:
                // 当点击了设置tab时，选中第4个tab
                setTabSelection(3);
                break;

//            case R.id.menu:
//                Log.d(TAG, "onClick: menu");
//                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                yukeeImage.setImageResource(R.drawable.selected);
                yukeeText.setTextColor(Color.WHITE);
                if (yukeeFragment == null) {
                    // 如果yukeeFragment为空，则创建一个并添加到界面上
                    yukeeFragment = new YukeeFragment();
                    transaction.add(R.id.content, yukeeFragment);
                } else {
                    // 如果yukeeFragment不为空，则直接将它显示出来
                    transaction.show(yukeeFragment);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                ireneImage.setImageResource(R.drawable.selected);
                ireneText.setTextColor(Color.WHITE);
                if (ireneFragment == null) {
                    // 如果ireneFragment为空，则创建一个并添加到界面上
                    ireneFragment = new IreneFragment();
                    transaction.add(R.id.content, ireneFragment);
                } else {
                    // 如果ireneFragment不为空，则直接将它显示出来
                    transaction.show(ireneFragment);
                }
                break;
            case 2:
                // 当点击了动态tab时，改变控件的图片和文字颜色
                arinImage.setImageResource(R.drawable.selected);
                arinText.setTextColor(Color.WHITE);
                if (arinFragment == null) {
                    // 如果arinFragment为空，则创建一个并添加到界面上
                    arinFragment = new ArinFragment();
                    transaction.add(R.id.content, arinFragment);
                } else {
                    // 如果arinFragment不为空，则直接将它显示出来
                    transaction.show(arinFragment);
                }
                break;
            case 3:
            default:
                // 当点击了设置tab时，改变控件的图片和文字颜色
                suzyImage.setImageResource(R.drawable.selected);
                suzyText.setTextColor(Color.WHITE);
                if (suzyFragment == null) {
                    // 如果suzyFragment为空，则创建一个并添加到界面上
                    suzyFragment = new SuzyFragment();
                    transaction.add(R.id.content, suzyFragment);
                } else {
                    // 如果suzyFragment不为空，则直接将它显示出来
                    transaction.show(suzyFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        yukeeImage.setImageResource(R.drawable.unselected);
        yukeeText.setTextColor(Color.parseColor("#f9d3e3"));
        ireneImage.setImageResource(R.drawable.unselected);
        ireneText.setTextColor(Color.parseColor("#f9d3e3"));
        arinImage.setImageResource(R.drawable.unselected);
        arinText.setTextColor(Color.parseColor("#f9d3e3"));
        suzyImage.setImageResource(R.drawable.unselected);
        suzyText.setTextColor(Color.parseColor("#f9d3e3"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (yukeeFragment != null) {
            transaction.hide(yukeeFragment);
        }
        if (ireneFragment != null) {
            transaction.hide(ireneFragment);
        }
        if (arinFragment != null) {
            transaction.hide(arinFragment);
        }
        if (suzyFragment != null) {
            transaction.hide(suzyFragment);
        }
    }

    // 显示悬浮窗口的方法
    private void showFloatingWidget() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        Log.d(TAG, "showFloatingWindow: sdw00");
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        floatingWidget = LayoutInflater.from(this).inflate(R.layout.floating_view, null);
        timeTextView = floatingWidget.findViewById(R.id.timeText);

        windowManager.addView(floatingWidget, params);

        // 可以添加一些拖拽悬浮窗口的功能
        floatingWidget.setOnTouchListener(new View.OnTouchListener() {
            private int dx, dy;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = (int) event.getRawX() - params.x;
                        dy = (int) event.getRawY() - params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = (int) event.getRawX() - dx;
                        params.y = (int) event.getRawY() - dy;
                        windowManager.updateViewLayout(v, params);
                        break;
                }
                return true;
            }
        });
    }

    private void removeFloatingWidget() {
        if (floatingWidget != null && windowManager != null) {
            windowManager.removeView(floatingWidget);
            floatingWidget = null;
        }
    }

    // 处理权限请求结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: sdw " + requestCode);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                showFloatingWidget(); // 显示悬浮窗口
            } else {
                Toast.makeText(this, "未授予悬浮窗口权限，无法显示悬浮窗口", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            if (ContextCompat.checkSelfPermission(YukeeActivity.this, Manifest.permission.PACKAGE_USAGE_STATS) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onActivityResult: sdw00");
                Toast.makeText(this, "未授予应用使用情况权限，无法正常使用", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "onActivityResult: sdw01");
                startActivity(new Intent(YukeeActivity.this, AppLockActivity.class));
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable); // 停止更新时间
        removeFloatingWidget();
        removeUnlockView();
        threadIsTerminate = false;
    }
}
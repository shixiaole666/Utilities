package com.android.turntable.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.turntable.R;
import com.android.turntable.utils.PreferencesUtils;
import com.android.turntable.views.UnLockPanel;

import java.util.List;


public class AppLockService extends IntentService {

    public static final String TAG = "AppLockService";
    private boolean threadIsTerminate = false;
    private ActivityManager activityManager;
    private PackageManager packageManager;
    private View unlockView;
    private WindowManager windowManager;
    private Handler handler;
    private ApplicationInfo mAppInfo;
    private ImageView mUnLockIcon;
    private TextView mUnLockText, mUnlockFailTip;
    private String pkgName, appLabel;
    private Drawable iconDrawable;
    private UnLockPanel mLockNumView;
    private String password = "0328";
    private boolean isUnlock = true;
    private String lastPkg = "";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public AppLockService() {
        super("AppLock");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        threadIsTerminate = true;
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        packageManager = getPackageManager();
        handler = new Handler(Looper.getMainLooper());
        Log.d(TAG, "onCreate: sdw");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        checkForegroundApp();
    }

    private void checkForegroundApp() {
        Log.d(TAG, "checkForegroundApp: sdw00");
        while (threadIsTerminate) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pkgName  = getTopPackageName(this);
            if (!pkgName.isEmpty() && lastPkg != "" && !pkgName.equals(lastPkg)) {
                isUnlock = true;
            }
            Log.d(TAG, "checkForegroundApp: sdw01 " + pkgName + PreferencesUtils.getBoolean(this, pkgName, false));
            if (PreferencesUtils.getBoolean(this, pkgName, false) && isUnlock) {
                Log.d(TAG, "checkForegroundApp: sdw02");
                if (unlockView == null)
                    Log.d(TAG, "checkForegroundApp: sdw123");
                    unLockView();
            }
        }
    }

    private void unLockView() {
        isUnlock = false;
        Log.d(TAG, "unLockView: sdw00");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // 在主线程中创建窗口
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

                    unlockView = LayoutInflater.from(AppLockService.this).inflate(R.layout.view_unlock, null);
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
                    Log.d(TAG, "unLockView: sdw123" + Thread.currentThread().getName());
                }
            });
    }

    private void initLockNumResult() {
        mLockNumView.setInputListener(new UnLockPanel.InputListener() {
            @RequiresApi(api = Build.VERSION_CODES.N_MR1)
            @Override
            public void inputFinish(String result) {
                if (result.equals(password)) {//解锁成功,更改数据库状态
                    Log.d(TAG, "inputFinish: sdw123 successful");
                    removeUnlockView();
                    lastPkg = pkgName;
                } else {
                    inputError();
                }
            }
        });
    }

    /**
     * 解锁成功，移除窗口
     */
    private void removeUnlockView() {
        if (unlockView != null && windowManager != null) {
            Log.d(TAG, "removeUnlockView: sdw123 remove" + Thread.currentThread().getName());
            windowManager.removeView(unlockView);
            isUnlock = false;
            unlockView = null;
        }

    }

    /**
     * 输入密码错误
     */
    private void inputError() {
//        mUnlockFailTip.setText(R.string.password_enter);
        Log.d(TAG, "inputError: sdw123 error");

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


    @Override
    public void onDestroy() {
        super.onDestroy();
        threadIsTerminate = false;
    }
}
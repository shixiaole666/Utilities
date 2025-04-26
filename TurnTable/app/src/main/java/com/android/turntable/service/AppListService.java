package com.android.turntable.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.android.turntable.applock.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppListService extends Service {

    private ArrayList<AppInfo> appList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadAppList();
        return START_STICKY;
    }

    private void loadAppList() {
        appList = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // 获取所有已安装应用的信息
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : applications) {
            if (packageManager.getLaunchIntentForPackage(app.packageName) != null) {
                AppInfo appInfo = new AppInfo();

                // 获取应用图标
                appInfo.setIcon(app.loadIcon(packageManager));

                // 获取应用名
                appInfo.setName(app.loadLabel(packageManager).toString());

                // 设置应用包名
                appInfo.setPackageName(app.packageName);

                appList.add(appInfo);
            }
        }
    }

    public ArrayList<AppInfo> getAppList() {
        return appList;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
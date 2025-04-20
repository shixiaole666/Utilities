package com.android.turntable;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.turntable.adapter.AppListAdapter;
import com.android.turntable.applock.AppInfo;
import com.android.turntable.service.AppListService;
import com.android.turntable.service.AppLockService;
import com.android.turntable.utils.PreferencesUtils;
import com.android.turntable.utils.TransparentStatusBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppLockActivity extends AppCompatActivity {

    public static final String TAG = "AppLockActivity";
    private RecyclerView appRecyclerView;
    private AppListAdapter adapter;
    private ArrayList<AppInfo> appList = new ArrayList<>();
    private HashMap<String, Boolean> appLockHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        TransparentStatusBar.transparentStatusBar(getWindow());
        TransparentStatusBar.transparentNavBar(getWindow());
        loadLauncherApps(this);
        appRecyclerView = findViewById(R.id.app_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        appRecyclerView.setLayoutManager(layoutManager);
        adapter = new AppListAdapter(this, appList);
        appRecyclerView.setAdapter(adapter);
        // 启动获取应用列表的服务
        Intent intent = new Intent(this, AppLockService.class);
        startService(intent);

    }

    public void loadLauncherApps(Context context) {
        appList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            AppInfo appInfo = new AppInfo();
            String pkgName = resolveInfo.activityInfo.packageName;
            appInfo.setIcon(resolveInfo.loadIcon(packageManager));
            appInfo.setName(resolveInfo.loadLabel(packageManager).toString());
            appInfo.setPackageName(pkgName);
            Log.d(TAG, "loadLauncherApps: cyq pkgName:" + pkgName + " isLock:" + PreferencesUtils.getBoolean(context, pkgName, false));
//            PreferencesUtils.putBoolean(context, pkgName, PreferencesUtils.getBoolean(context, pkgName, false));
            appList.add(appInfo);
        }
    }

}

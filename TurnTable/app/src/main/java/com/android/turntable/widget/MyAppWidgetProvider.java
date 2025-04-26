package com.android.turntable.widget;

import android.appwidget.AppWidgetManager;  
import android.appwidget.AppWidgetProvider;  
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.turntable.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyAppWidgetProvider extends AppWidgetProvider {  
    
    public static final String TAG = "MyAppWidgetProvider";
    private List<String> wellList = new ArrayList<>(
            List.of("慢慢来 谁没有一个努力的过程",
                    "因为有目标 所以一切皆有可能",
                    "你应该提前准备 而不是提前焦虑",
                    "没有躺赢的命 那就站起来奔跑",
                    "做你想做的事 成为你想成为的人",
                    "想多了都是问题 想通了都是答案",
                    "想不通就学习 哪有那么多破事让你烦",
                    "行为就是答案 所以我从来不问为什么",
                    "竭尽全力 才知道自己有多么出色",
                    "我们都会成为更好的人",
                    "我的人生不应该是潦草诗",
                    "缓解焦虑最好的方法 就是去做让你焦虑的事"));
    Random random = new Random();

    @Override  
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {  
        // 更新小部件内容  
        for (int appWidgetId : appWidgetIds) {  
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setTextViewText(R.id.well_text, wellList.get(random.nextInt(12)));
            // 可以在这里更新小部件视图内容，比如设置点击事件等  
            appWidgetManager.updateAppWidget(appWidgetId, views);
            Log.d(TAG, "onUpdate: sdw");
        }  
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }
}
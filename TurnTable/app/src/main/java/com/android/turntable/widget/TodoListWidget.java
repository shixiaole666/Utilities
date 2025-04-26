package com.android.turntable.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.android.turntable.R;
import com.android.turntable.TodoActivity;
import com.android.turntable.service.TodoRemoteViewsService;
import com.android.turntable.utils.TodoPreferences;

/**
 * Implementation of App Widget functionality.
 */
public class TodoListWidget extends AppWidgetProvider {

    public static final String ACTION_REFRESH = "com.example.appwidget.ACTION_REFRESH";
    public static final String ACTION_TOGGLE_TODO = "com.example.appwidget.ACTION_TOGGLE_TODO";
    public static final String EXTRA_TODO_ID = "todo_id";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        
        if (ACTION_REFRESH.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, TodoListWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.todo_list);
        } else if (ACTION_TOGGLE_TODO.equals(intent.getAction())) {
            String todoId = intent.getStringExtra(EXTRA_TODO_ID);
            if (todoId != null) {
                TodoPreferences preferences = new TodoPreferences(context);
                preferences.toggleTodo(todoId);
                // 刷新widget
                Intent refreshIntent = new Intent(context, TodoListWidget.class);
                refreshIntent.setAction(ACTION_REFRESH);
                context.sendBroadcast(refreshIntent);
            }
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_list_widget);

        // 设置ListView的适配器
        Intent serviceIntent = new Intent(context, TodoRemoteViewsService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.todo_list, serviceIntent);

        // 设置空视图
        views.setEmptyView(R.id.todo_list, R.id.empty_view);

        // 设置列表项点击事件
        Intent toggleIntent = new Intent(context, TodoListWidget.class);
        toggleIntent.setAction(ACTION_TOGGLE_TODO);
        PendingIntent togglePendingIntent = PendingIntent.getBroadcast(
            context, 0, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        views.setPendingIntentTemplate(R.id.todo_list, togglePendingIntent);

        // 设置添加按钮点击事件
        Intent mainIntent = new Intent(context, TodoActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(
            context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        views.setOnClickPendingIntent(R.id.add_button, mainPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

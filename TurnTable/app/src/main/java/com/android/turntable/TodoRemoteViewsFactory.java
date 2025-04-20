package com.android.turntable;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.turntable.utils.TodoPreferences;

import java.util.List;

public class TodoRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<Todo> todos;
    private TodoPreferences preferences;

    public TodoRemoteViewsFactory(Context context) {
        this.context = context;
        this.preferences = new TodoPreferences(context);
    }

    @Override
    public void onCreate() {
        // 初始化时不需要加载数据
    }

    @Override
    public void onDataSetChanged() {
        // 当数据更新时重新加载
        todos = preferences.getTodos();
    }

    @Override
    public void onDestroy() {
        todos.clear();
    }

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position < 0 || position >= todos.size()) {
            return null;
        }

        Todo todo = todos.get(position);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.todo_item);
        rv.setTextViewText(R.id.todo_text, todo.getContent());
        rv.setImageViewResource(R.id.todo_checkbox, 
            todo.isCompleted() ? R.drawable.complete : R.drawable.incomplete);

        // 设置点击事件的填充Intent
        Intent fillIntent = new Intent();
        fillIntent.putExtra("todo_id", todo.getId());
        rv.setOnClickFillInIntent(R.id.todo_item_container, fillIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
} 

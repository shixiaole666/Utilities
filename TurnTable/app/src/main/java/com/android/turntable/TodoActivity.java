package com.android.turntable;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.turntable.utils.TransparentStatusBar;
import com.android.turntable.widget.TodoListWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.android.turntable.adapter.TodoAdapter;
import com.android.turntable.utils.TodoPreferences;

public class TodoActivity extends AppCompatActivity {
    private TodoPreferences preferences;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        TransparentStatusBar.transparentStatusBar(getWindow());
        TransparentStatusBar.transparentNavBar(getWindow());
        preferences = new TodoPreferences(this);

        RecyclerView recyclerView = findViewById(R.id.todo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TodoAdapter(preferences.getTodos(), preferences);
        recyclerView.setAdapter(adapter);

        ImageButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddTodoDialog());
    }

    private void showAddTodoDialog() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_todo, null);
        EditText editTodo = dialogView.findViewById(R.id.edit_todo);

        AlertDialog mDialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setTitle("添加待办事项")
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String content = editTodo.getText().toString().trim();
                    if (!content.isEmpty()) {
                        preferences.addTodo(content);
                        adapter.refreshData();
                        refreshWidget();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
                mDialog.getWindow().setBackgroundDrawableResource(R.drawable.widget_bg);
                mDialog.show();
    }

    private void refreshWidget() {
        Intent intent = new Intent(this, TodoListWidget.class);
        intent.setAction(TodoListWidget.ACTION_REFRESH);
        sendBroadcast(intent);
    }
}


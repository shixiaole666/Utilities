package com.android.turntable.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.turntable.Todo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TodoPreferences {
    private static final String PREF_NAME = "todo_preferences";
    private static final String KEY_TODOS = "todos";
    private final SharedPreferences preferences;
    private final Gson gson;

    public TodoPreferences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public List<Todo> getTodos() {
        String json = preferences.getString(KEY_TODOS, "[]");
        return gson.fromJson(json, new TypeToken<List<Todo>>(){}.getType());
    }

    public void saveTodos(List<Todo> todos) {
        String json = gson.toJson(todos);
        preferences.edit().putString(KEY_TODOS, json).apply();
    }

    public void addTodo(String content) {
        List<Todo> todos = getTodos();
        todos.add(new Todo(UUID.randomUUID().toString(), content, false));
        saveTodos(todos);
    }

    public void toggleTodo(String id) {
        List<Todo> todos = getTodos();
        for (Todo todo : todos) {
            if (todo.getId().equals(id)) {
                todo.setCompleted(!todo.isCompleted());
                break;
            }
        }
        saveTodos(todos);
    }

    public void deleteTodo(String id) {
        List<Todo> todos = getTodos();
        todos.removeIf(todo -> todo.getId().equals(id));
        saveTodos(todos);
    }
} 

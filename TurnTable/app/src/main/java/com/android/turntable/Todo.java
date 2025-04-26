package com.android.turntable;

public class Todo {
    private String id;
    private String content;
    private boolean completed;

    public Todo(String id, String content, boolean completed) {
        this.id = id;
        this.content = content;
        this.completed = completed;
    }

    public String getId() { return id; }

    public String getContent() { return content; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
} 

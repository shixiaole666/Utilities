package com.android.turntable.applock;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private Drawable icon;
    private String name;
    private String packageName;

    public AppInfo() {

    }

    // Getter and Setter methods

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
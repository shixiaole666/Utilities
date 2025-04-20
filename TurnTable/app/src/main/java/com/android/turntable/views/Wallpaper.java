package com.android.turntable.views;

import android.widget.ImageView;

public class Wallpaper {

    private String name;

    private int imageId;

    public Wallpaper(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}

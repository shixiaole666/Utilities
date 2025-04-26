package com.android.turntable.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.turntable.R;
import com.android.turntable.utils.TransparentStatusBar;
import com.android.turntable.views.WallPaperAdapter;
import com.android.turntable.views.Wallpaper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WallpaperActivity extends AppCompatActivity {
    private Wallpaper[] wallpapers = {new Wallpaper("Yukee00", R.drawable.yukee0), new Wallpaper("Yukee01", R.drawable.yukee1), new Wallpaper("Yukee02", R.drawable.yukee2), new Wallpaper("Yukee03", R.drawable.yukee3),
            new Wallpaper("Yukee04", R.drawable.yukee4), new Wallpaper("Yukee05", R.drawable.yukee5), new Wallpaper("Yukee06", R.drawable.yukee6)};
    private List<Wallpaper> wallpaperList = new ArrayList<>();
    private WallPaperAdapter wallPaperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wallpaper);
        TransparentStatusBar.transparentStatusBar(getWindow());
        TransparentStatusBar.transparentNavBar(getWindow());
        initWallpaper();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        wallPaperAdapter = new WallPaperAdapter(wallpaperList);
        recyclerView.setAdapter(wallPaperAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initWallpaper() {
//        wallpaperList.clear();
//        for (int i = 0; i < 50; i++) {
//            Random random = new Random();
//            int index = random.nextInt(wallpapers.length);
//            wallpaperList.add(wallpapers[index]);
//        }
        wallpaperList = Arrays.asList(wallpapers);
    }
}

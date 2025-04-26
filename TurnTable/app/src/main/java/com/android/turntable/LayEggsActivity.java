package com.android.turntable;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.turntable.utils.TransparentStatusBar;

public class LayEggsActivity extends AppCompatActivity {
    public static String TAG = "LayEggsActivity ";
    private ChickenView layEggsView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay);
        TransparentStatusBar.transparentStatusBar(getWindow());
        TransparentStatusBar.transparentNavBar(getWindow());
//        layEggsView = findViewById(R.id.lay_eggs_view);

    }


}

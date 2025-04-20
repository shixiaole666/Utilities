package com.android.turntable;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.turntable.clock.AlarmActivity;
import com.android.turntable.utils.TransparentStatusBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static String TAG = "AlarmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TransparentStatusBar.transparentStatusBar(getWindow());
        TransparentStatusBar.transparentNavBar(getWindow());
        Button wheelBtn = findViewById(R.id.wheel_btn);
        Button rollBtn = findViewById(R.id.roll_btn);
        Button layBtn = findViewById(R.id.lay_btn);
        wheelBtn.setOnClickListener(this);
        rollBtn.setOnClickListener(this);
        layBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wheel_btn:
                startActivity(new Intent(MainActivity.this, LuckWheelActivity.class));
                break;
            case R.id.roll_btn:
                startActivity(new Intent(MainActivity.this, RollSelectActivity.class));
                break;
            case R.id.lay_btn:
                startActivity(new Intent(MainActivity.this, LayEggsActivity.class));
                break;
            default:
        }
    }
}

package com.android.turntable;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.turntable.utils.PreferencesUtils;
import com.android.turntable.utils.TransparentStatusBar;

public class RollSelectActivity extends AppCompatActivity {

    private RollingNumberView rollingNumberView;
    public static String TAG = "RollSelectActivity";
    private EditText rollEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);
        TransparentStatusBar.transparentStatusBar(getWindow());
        TransparentStatusBar.transparentNavBar(getWindow());
        rollingNumberView = findViewById(R.id.rolling_number_view);
        rollEditText = findViewById(R.id.rollEditText);
    }

    public void onClick(View view) {
        String select = rollEditText.getText().toString();

        if (select.isEmpty()) {
            select = PreferencesUtils.getString(this, "roll_selects", "A,B,C,D,E,F,G,H");
            Log.d(TAG, "onClick: select00:" + select);
        } else {
            Log.d(TAG, "onClick: select11:" + select);
            PreferencesUtils.putString(this, "roll_selects", select);
        }

        String[] selects = select.split("[,; ，；]+");
        rollingNumberView.setSelects(selects);
        rollingNumberView.startRolling();
    }
}
package com.android.turntable;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.turntable.utils.PreferencesUtils;
import com.android.turntable.utils.TransparentStatusBar;

public class LuckWheelActivity extends AppCompatActivity {

    public static String TAG = "LuckWheelActivity";
    private LuckyWheelView luckyWheelView;
    private EditText prizeEditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);
        TransparentStatusBar.transparentStatusBar(getWindow());
        TransparentStatusBar.transparentNavBar(getWindow());
        luckyWheelView = findViewById(R.id.luckyWheelView);
        prizeEditText = findViewById(R.id.prizeEditText);
        resultTextView = findViewById(R.id.resultTextView);

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prize = prizeEditText.getText().toString();
//                if (!TextUtils.isEmpty(prize)) {
                Log.d(TAG, "onClick: " + prize);
                if (prize.isEmpty()) {
                    prize = PreferencesUtils.getString(LuckWheelActivity.this, "luck_selects", "A,B,C,D,E,F,G,H");
                } else {
                    PreferencesUtils.putString(LuckWheelActivity.this, "luck_selects", prize);

                }
                Log.d(TAG, "onClick: prize" + prize);
                String[] prizes = prize.split("[,; ，；]+");
                luckyWheelView.setPrizes(prizes);
                luckyWheelView.setOnStopListener(new LuckyWheelView.OnStopListener() {
                    @Override
                    public void onStop(String prize) {
                        resultTextView.setText(prize);
                    }
                });
                luckyWheelView.start();
//                } else {
//                    Toast.makeText(AlarmActivity.this, "请输入奖品", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}

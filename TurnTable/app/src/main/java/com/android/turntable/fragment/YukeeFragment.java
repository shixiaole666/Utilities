package com.android.turntable.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.Button;

import com.android.turntable.LayEggsActivity;
import com.android.turntable.LuckWheelActivity;
import com.android.turntable.MainActivity;
import com.android.turntable.R;
import com.android.turntable.RollSelectActivity;
import com.android.turntable.YukeeActivity;

public class YukeeFragment extends Fragment{

    // 在 Fragment 中定义一个变量来保存宿主 Activity 的引用
    private YukeeActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 将宿主 Activity 的引用保存到变量中
        mActivity = (YukeeActivity) context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View yukeeLayout = inflater.inflate(R.layout.layout_yukee, container, false);
        Button wheelBtn = yukeeLayout.findViewById(R.id.wheel_btn);
        Button rollBtn = yukeeLayout.findViewById(R.id.roll_btn);
        Button layBtn = yukeeLayout.findViewById(R.id.lay_btn);
        wheelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, LuckWheelActivity.class));
            }
        });
        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, RollSelectActivity.class));
            }
        });
        layBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, LayEggsActivity.class));
            }
        });
        return yukeeLayout;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // 在 Fragment 分离时将宿主 Activity 的引用置空
        mActivity = null;
    }

}

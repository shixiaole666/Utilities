package com.android.turntable.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.turntable.R;

public class WallPaperFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ireneLayout = inflater.inflate(R.layout.layout_wallpaper, container, false);
        return ireneLayout;
    }

}
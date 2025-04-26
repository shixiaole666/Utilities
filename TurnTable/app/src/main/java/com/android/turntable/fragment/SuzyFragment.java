package com.android.turntable.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;

import com.android.turntable.R;

public class SuzyFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View suzyLayout = inflater.inflate(R.layout.layout_suzy, container, false);
        return suzyLayout;
    }

}

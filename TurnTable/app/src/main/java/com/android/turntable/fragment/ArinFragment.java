package com.android.turntable.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import com.android.turntable.R;

public class ArinFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View arinLayout = inflater.inflate(R.layout.layout_arin, container, false);
        return arinLayout;
    }

}

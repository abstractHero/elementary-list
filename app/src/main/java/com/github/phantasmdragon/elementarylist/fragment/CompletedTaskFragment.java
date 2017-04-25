package com.github.phantasmdragon.elementarylist.fragment;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.phantasmdragon.elementarylist.R;

import org.jetbrains.annotations.Contract;

public class CompletedTaskFragment extends Fragment {

    @Contract(" -> !null")
    public static CompletedTaskFragment newInstance() {
        return new CompletedTaskFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_task, container, false);
        return view;
    }
}

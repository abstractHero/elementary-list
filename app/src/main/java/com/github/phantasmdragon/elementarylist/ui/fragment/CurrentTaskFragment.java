package com.github.phantasmdragon.elementarylist.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.phantasmdragon.elementarylist.R;

import org.jetbrains.annotations.Contract;

public class CurrentTaskFragment extends ListFragment {

    @Contract(" -> !null")
    public static CurrentTaskFragment newInstance() {
        return new CurrentTaskFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_task, container, false);
        return view;
    }
}

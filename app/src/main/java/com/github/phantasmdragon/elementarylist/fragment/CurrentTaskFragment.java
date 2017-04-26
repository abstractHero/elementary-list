package com.github.phantasmdragon.elementarylist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.phantasmdragon.elementarylist.R;
import com.github.phantasmdragon.elementarylist.custom.rowadapter.CustomRowAdapter;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;

public class CurrentTaskFragment extends Fragment {

    //Array List how example for demonstration. Will be deleted
    private ArrayList<String> tasks = new ArrayList<>(Arrays.asList("Hi, how are you?", "Hi, how are you?", "Hi, how are you?",
            "Hi, how are you?", "Hi, how are you?", "Hi, how are you?", "Hi, how are you?", "Hi, how are you?"));

    @Contract(" -> !null")
    public static CurrentTaskFragment newInstance() {
        return new CurrentTaskFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CustomRowAdapter rowAdapter = new CustomRowAdapter(getActivity().getApplicationContext(), tasks);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        RecyclerView currentTaskRecycler = (RecyclerView) getActivity().findViewById(R.id.recycler_current_task);
        currentTaskRecycler.setAdapter(rowAdapter);
        currentTaskRecycler.setLayoutManager(layoutManager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_task, container, false);
    }
}

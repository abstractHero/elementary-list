package com.github.phantasmdragon.elementarylist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

    private CustomRowAdapter rowAdapter;
    private RecyclerView currentTaskRecycler;

    //Array List how example for demonstration. Will be deleted
    private ArrayList<String> tasks = new ArrayList<>(Arrays.asList("11111", "22222", "33333",
            "44444", "55555", "66666", "77777", "88888", "99999", "101010"
            , "121212", "131313", "141414", "151515", "161616", "171717", "181818"));

    @Contract(" -> !null")
    public static CurrentTaskFragment newInstance() {
        return new CurrentTaskFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            tasks = savedInstanceState.getStringArrayList("list");
        }

        currentTaskRecycler = (RecyclerView)getActivity().findViewById(R.id.recycler_current_task);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        currentTaskRecycler.setLayoutManager(layoutManager);
        currentTaskRecycler.setAdapter(rowAdapter);

        final FloatingActionButton fab = ((FloatingActionButton)getActivity().findViewById(R.id.button_float));
        currentTaskRecycler.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) fab.hide();
                else        fab.show();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_task, container, false);
        rowAdapter = new CustomRowAdapter(view.getContext(), tasks);
        return view;
    }

    public void addTaskToList(Bundle infoAboutNewTask) {
        String taskName = infoAboutNewTask.getString("task_name");
        tasks.add(0, taskName);
        if (rowAdapter != null) rowAdapter.notifyItemInserted(0);

        currentTaskRecycler.scrollToPosition(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list", tasks);
    }
}

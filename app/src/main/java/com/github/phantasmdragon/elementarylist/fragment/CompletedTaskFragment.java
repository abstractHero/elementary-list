package com.github.phantasmdragon.elementarylist.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.Map;

public class CompletedTaskFragment extends Fragment {

    public static final String COMPLETED_TASK = "CompletedTaskList";

    private CustomRowAdapter rowAdapter;
    private RecyclerView completedTaskRecycler;
    private SharedPreferences completedTaskPreference;

    private ArrayList<String> completedTasks = new ArrayList<>();

    @Contract(" -> !null")
    public static CompletedTaskFragment newInstance() {
        return new CompletedTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        completedTaskPreference = getActivity().getSharedPreferences(COMPLETED_TASK, Context.MODE_PRIVATE);
        loadTask();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_task, container, false);

        rowAdapter = new CustomRowAdapter(view.getContext(), completedTasks);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            completedTasks = savedInstanceState.getStringArrayList("completedList");
        }

        completedTaskRecycler = (RecyclerView)getActivity().findViewById(R.id.recycler_completed_task);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        completedTaskRecycler.setLayoutManager(layoutManager);
        completedTaskRecycler.setAdapter(rowAdapter);
    }

    private void loadTask() {
        Map<String, ?> allPreference = completedTaskPreference.getAll();
        for (int i = 0; i < allPreference.size(); i++) {
            String taskName = completedTaskPreference.getString(String.valueOf(i), null);
            if (taskName != null) completedTasks.add(0, taskName);
        }
    }

    private void saveTask(String taskName) {
        SharedPreferences.Editor editor = completedTaskPreference.edit();
        editor.putString(String.valueOf(completedTasks.size()-1), taskName);
        editor.apply();
    }

    public void addTaskToList(Bundle infoAboutNewTask) {
        String taskName = infoAboutNewTask.getString("completed_task_name");
        completedTasks.add(0, taskName);
        saveTask(taskName);
        if (rowAdapter != null) rowAdapter.notifyItemRangeChanged(0, completedTasks.size());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("completedList", completedTasks);
    }
}

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
import com.github.phantasmdragon.elementarylist.activity.MainActivity;
import com.github.phantasmdragon.elementarylist.custom.rowadapter.CustomRowAdapter;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Map;

public class CompletedTaskFragment extends Fragment {

    private final String NAME_LIST = "completedList";
    private final String NAME_FILE = "CompletedTaskList";

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
        completedTaskPreference = getActivity().getSharedPreferences(NAME_FILE, Context.MODE_PRIVATE);
        loadTask();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_task, container, false);

        rowAdapter = new CustomRowAdapter(view.getContext(), completedTasks, CompletedTaskFragment.class.getSimpleName());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            completedTasks = savedInstanceState.getStringArrayList(NAME_LIST);
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

    private void deleteTask(int position) {
        SharedPreferences.Editor deleteEditor = completedTaskPreference.edit();
        deleteEditor.remove(String.valueOf(position));
        deleteEditor.apply();
    }

    public Bundle getInfoAboutTask(int position) {
        Bundle infoAboutTask = new Bundle();
        infoAboutTask.putString(MainActivity.NAME_TASK, completedTasks.get(position));
        return infoAboutTask;
    }

    public void addTask(Bundle infoAboutNewTask) {
        String taskName = infoAboutNewTask.getString(MainActivity.NAME_TASK);
        completedTasks.add(0, taskName);
        saveTask(taskName);
        if (rowAdapter != null) rowAdapter.notifyItemRangeChanged(0, completedTasks.size());
    }

    public void removeTask(int position) {
        deleteTask(position);
        completedTasks.remove(position);
        rowAdapter.notifyItemRemoved(position);
        rowAdapter.notifyItemRangeChanged(0, completedTasks.size());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(NAME_LIST, completedTasks);
    }
}

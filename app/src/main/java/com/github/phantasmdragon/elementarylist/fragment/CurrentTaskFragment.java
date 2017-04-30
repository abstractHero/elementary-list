package com.github.phantasmdragon.elementarylist.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.Map;

public class CurrentTaskFragment extends Fragment {

    public static final String CURRENT_TASK = "CurrentTaskList";

    private CustomRowAdapter rowAdapter;
    private RecyclerView currentTaskRecycler;
    private SharedPreferences currentTaskPreference;

    private ArrayList<String> tasks = new ArrayList<>();

    @Contract(" -> !null")
    public static CurrentTaskFragment newInstance() {
        return new CurrentTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentTaskPreference = getActivity().getSharedPreferences(CURRENT_TASK, Context.MODE_PRIVATE);
        loadTask();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_task, container, false);

        rowAdapter = new CustomRowAdapter(view.getContext(), tasks);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            tasks = savedInstanceState.getStringArrayList("currentList");
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

    private void loadTask() {
        Map<String, ?> allPreference = currentTaskPreference.getAll();
        for (int i = 0; i < allPreference.size(); i++) {
            String taskName = currentTaskPreference.getString(String.valueOf(i), null);
            if (taskName != null) tasks.add(0, taskName);
        }
    }

    private void saveTask(String taskName) {
        SharedPreferences.Editor saveEditor = currentTaskPreference.edit();
        saveEditor.putString(String.valueOf(tasks.size()-1), taskName);
        saveEditor.apply();
    }

    private void deleteTask(int position) {
        SharedPreferences.Editor deleteEditor = currentTaskPreference.edit();
        deleteEditor.remove(String.valueOf(position));
        deleteEditor.apply();
    }

    public void addTaskToList(Bundle infoAboutNewTask) {
        String taskName = infoAboutNewTask.getString("task_name");
        tasks.add(0, taskName);
        saveTask(taskName);
        if (rowAdapter != null) rowAdapter.notifyItemRangeChanged(0, tasks.size());

        currentTaskRecycler.scrollToPosition(0);
    }

    public Bundle getCompletedTask(int position) {
        Bundle infoAboutTask = new Bundle();
        infoAboutTask.putString("completed_task_name", tasks.get(position));
        return infoAboutTask;
    }

    public void taskIsFinished(int position) {
        deleteTask(position);
        tasks.remove(position);
        rowAdapter.notifyItemRemoved(position);
        rowAdapter.notifyItemRangeChanged(0, tasks.size());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("currentList", tasks);
    }
}

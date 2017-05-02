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

        final FloatingActionButton fab = ((FloatingActionButton)getActivity().findViewById(R.id.button_float));
        completedTaskRecycler.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) fab.hide();
                else        fab.show();
            }
        });
    }

    private void loadTask() {
        Map<String, ?> allPreference = completedTaskPreference.getAll();
        if (allPreference.size() > 0) {
            for (String set: allPreference.keySet()) {
                String taskName = completedTaskPreference.getString(set, "");
                completedTasks.add(0, taskName);
            }
        }
    }

    private void saveTask(String taskName) {
        SharedPreferences.Editor editor = completedTaskPreference.edit();
        editor.putString(String.valueOf(taskName.hashCode()), taskName);
        editor.apply();
    }

    private void deleteTask(String taskName) {
        SharedPreferences.Editor deleteEditor = completedTaskPreference.edit();
        deleteEditor.remove(String.valueOf(taskName.hashCode()));
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
        rowAdapter.notifyItemRangeChanged(0, completedTasks.size());

        completedTaskRecycler.scrollToPosition(0);
    }

    public void removeTask(int position) {
        deleteTask(completedTasks.get(position));
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

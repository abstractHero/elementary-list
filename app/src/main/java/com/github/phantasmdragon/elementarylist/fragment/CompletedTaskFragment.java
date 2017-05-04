package com.github.phantasmdragon.elementarylist.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class CompletedTaskFragment extends Fragment {

    private final String NAME_LIST_ID = "completedId";
    private final String NAME_LIST = "completedList";
    private final String NAME_FILE = "CompletedTaskList";

    private CustomRowAdapter rowAdapter;
    private RecyclerView completedTaskRecycler;
    private SharedPreferences completedTaskPreference;

    private ArrayList<String> completedTasks = new ArrayList<>();
    private ArrayList<String> completedTasksId = new ArrayList<>();

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
            completedTasksId = savedInstanceState.getStringArrayList(NAME_LIST_ID);
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
        Set<String> keySet = completedTaskPreference.getAll().keySet();
        TreeSet<String> sortKeySet = new TreeSet<>(keySet);
        for (String key: sortKeySet) {
            completedTasks.add(completedTaskPreference.getString(key, ""));
            completedTasksId.add(key.substring(key.indexOf("_")+1));
        }
    }

    private void saveTask(String taskId, Bundle taskInfo) {
        SharedPreferences.Editor saveEditor = completedTaskPreference.edit();
        saveEditor.putString(taskId, taskInfo.getString(MainActivity.NAME_TASK))
                .apply();
    }

    private void deleteTask(String taskId) {
        SharedPreferences.Editor deleteEditor = completedTaskPreference.edit();
        if (completedTaskPreference.contains(taskId)) deleteEditor.remove(taskId).apply();
        else deleteEditor.remove(getKeyWithoutPrefix(taskId)).apply();
    }

    public Bundle getInfoAboutTask(int position) {
        Bundle infoAboutTask = new Bundle();
        infoAboutTask.putString(MainActivity.NAME_TASK, completedTasks.get(position));
        return infoAboutTask;
    }

    public void addTask(Bundle infoAboutNewTask) {
        completedTasksId.add(0, new UUID(System.currentTimeMillis(), System.nanoTime()).toString());

        completedTasks.add(0, infoAboutNewTask.getString(MainActivity.NAME_TASK));
        saveTask(completedTasksId.get(0), infoAboutNewTask);
        rowAdapter.notifyItemRangeChanged(0, completedTasks.size());

        completedTaskRecycler.scrollToPosition(0);
    }

    public void removeTask(int position) {
        deleteTask(getKey(position, completedTasksId.get(position)));
        completedTasks.remove(position);
        completedTasksId.remove(position);
        rowAdapter.notifyItemRemoved(position);
        rowAdapter.notifyItemRangeChanged(0, completedTasks.size());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(NAME_LIST, completedTasks);
        outState.putStringArrayList(NAME_LIST_ID, completedTasksId);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor positionEditor = completedTaskPreference.edit();
        positionEditor.clear();
        for (int i = 0; i < completedTasks.size(); i++) {

            String id = completedTasksId.get(i),
                    key = getKey(i, id);

            positionEditor.putString(key, completedTasks.get(i));
        }
        positionEditor.apply();
    }

    @NonNull
    private String getKey(int index, String key) {
        return String.valueOf(index).concat("_" + key);
    }

    @NonNull
    private String getKeyWithoutPrefix(String key) {
        return key.substring(key.indexOf("_")+1);
    }
}

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

public class CurrentTaskFragment extends Fragment {

    private final String NAME_LIST_ID = "unfulfilledId";
    private final String NAME_LIST = "unfulfilledList";
    private final String NAME_FILE = "CurrentTaskList";

    private CustomRowAdapter rowAdapter;
    private RecyclerView currentTaskRecycler;
    private SharedPreferences currentTaskPreference;

    private ArrayList<String> unfulfilledTasks = new ArrayList<>();
    private ArrayList<String> unfulfilledTasksId = new ArrayList<>();

    @Contract(" -> !null")
    public static CurrentTaskFragment newInstance() {
        return new CurrentTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentTaskPreference = getActivity().getSharedPreferences(NAME_FILE, Context.MODE_PRIVATE);
        loadTask();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_task, container, false);

        rowAdapter = new CustomRowAdapter(view.getContext(), unfulfilledTasks, CurrentTaskFragment.class.getSimpleName());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            unfulfilledTasks = savedInstanceState.getStringArrayList(NAME_LIST);
            unfulfilledTasksId = savedInstanceState.getStringArrayList(NAME_LIST_ID);
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
        Set<String> keySet = currentTaskPreference.getAll().keySet();
        TreeSet<String> sortKeySet = new TreeSet<>(keySet);
        for (String key: sortKeySet) {
            unfulfilledTasks.add(currentTaskPreference.getString(key, ""));
            unfulfilledTasksId.add(key.substring(key.indexOf("_")+1));
        }
    }

    private void saveTask(String taskId, Bundle taskInfo) {
        SharedPreferences.Editor saveEditor = currentTaskPreference.edit();
        saveEditor.putString(taskId, taskInfo.getString(MainActivity.NAME_TASK))
                  .apply();
    }

    private void deleteTask(String taskId) {
        SharedPreferences.Editor deleteEditor = currentTaskPreference.edit();
        if (currentTaskPreference.contains(taskId)) deleteEditor.remove(taskId).apply();
        else deleteEditor.remove(getKeyWithoutPrefix(taskId)).apply();
    }

    public void addTask(Bundle infoAboutNewTask) {
        unfulfilledTasksId.add(0, new UUID(System.currentTimeMillis(), System.nanoTime()).toString());

        unfulfilledTasks.add(0, infoAboutNewTask.getString(MainActivity.NAME_TASK));
        saveTask(unfulfilledTasksId.get(0), infoAboutNewTask);
        rowAdapter.notifyItemRangeChanged(0, unfulfilledTasks.size());

        currentTaskRecycler.scrollToPosition(0);
    }

    public Bundle getInfoAboutTask(int position) {
        Bundle infoAboutTask = new Bundle();
        infoAboutTask.putString(MainActivity.NAME_TASK, unfulfilledTasks.get(position));
        return infoAboutTask;
    }

    public void removeTask(int position) {
        deleteTask(getKey(position, unfulfilledTasksId.get(position)));
        unfulfilledTasks.remove(position);
        unfulfilledTasksId.remove(position);
        rowAdapter.notifyItemRemoved(position);
        rowAdapter.notifyItemRangeChanged(0, unfulfilledTasks.size());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(NAME_LIST, unfulfilledTasks);
        outState.putStringArrayList(NAME_LIST_ID, unfulfilledTasksId);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor positionEditor = currentTaskPreference.edit();
        positionEditor.clear();
        for (int i = 0; i < unfulfilledTasks.size(); i++) {

            String id = unfulfilledTasksId.get(i),
                  key = getKey(i, id);

            positionEditor.putString(key, unfulfilledTasks.get(i));
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

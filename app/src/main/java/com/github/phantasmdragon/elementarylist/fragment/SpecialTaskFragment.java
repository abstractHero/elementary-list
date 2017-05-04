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

public class SpecialTaskFragment extends Fragment {

    private final String NAME_LIST_ID = "specialId";
    private final String NAME_LIST = "specialList";
    private final String NAME_FILE = "SpecialTaskList";

    private CustomRowAdapter rowAdapter;
    private RecyclerView specialTaskRecycler;
    private SharedPreferences specialTaskPreference;

    private ArrayList<String> specialTasks = new ArrayList<>();
    private ArrayList<String> specialTasksId = new ArrayList<>();

    @Contract(" -> !null")
    public static SpecialTaskFragment newInstance() {
        return new SpecialTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        specialTaskPreference = getActivity().getSharedPreferences(NAME_FILE, Context.MODE_PRIVATE);
        loadTask();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_task, container, false);

        rowAdapter = new CustomRowAdapter(view.getContext(), specialTasks, SpecialTaskFragment.class.getSimpleName());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            specialTasks = savedInstanceState.getStringArrayList(NAME_LIST);
            specialTasksId = savedInstanceState.getStringArrayList(NAME_LIST_ID);
        }

        specialTaskRecycler = (RecyclerView)getActivity().findViewById(R.id.recycler_special_task);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        specialTaskRecycler.setLayoutManager(layoutManager);
        specialTaskRecycler.setAdapter(rowAdapter);

        final FloatingActionButton fab = ((FloatingActionButton)getActivity().findViewById(R.id.button_float));
        specialTaskRecycler.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) fab.hide();
                else        fab.show();
            }
        });
    }

    private void loadTask() {
        Set<String> keySet = specialTaskPreference.getAll().keySet();
        TreeSet<String> sortKeySet = new TreeSet<>(keySet);
        for (String key: sortKeySet) {
            specialTasks.add(specialTaskPreference.getString(key, ""));
            specialTasksId.add(key.substring(key.indexOf("_")+1));
        }
    }

    private void saveTask(String taskId, Bundle taskInfo) {
        SharedPreferences.Editor saveEditor = specialTaskPreference.edit();
        saveEditor.putString(taskId, taskInfo.getString(MainActivity.NAME_TASK))
                .apply();
    }

    private void deleteTask(String taskId) {
        SharedPreferences.Editor deleteEditor = specialTaskPreference.edit();
        if (specialTaskPreference.contains(taskId)) deleteEditor.remove(taskId).apply();
        else deleteEditor.remove(getKeyWithoutPrefix(taskId)).apply();
    }

    public void addTask(Bundle infoAboutNewTask) {
        specialTasksId.add(0, new UUID(System.currentTimeMillis(), System.nanoTime()).toString());

        specialTasks.add(0, infoAboutNewTask.getString(MainActivity.NAME_TASK));
        saveTask(specialTasksId.get(0), infoAboutNewTask);
        rowAdapter.notifyItemRangeChanged(0, specialTasks.size());

        specialTaskRecycler.scrollToPosition(0);
    }

    public Bundle getInfoAboutTask(int position) {
        Bundle infoAboutTask = new Bundle();
        infoAboutTask.putString(MainActivity.NAME_TASK, specialTasks.get(position));
        return infoAboutTask;
    }

    public void removeTask(int position) {
        deleteTask(getKey(position, specialTasksId.get(position)));
        specialTasks.remove(position);
        specialTasksId.remove(position);
        rowAdapter.notifyItemRemoved(position);
        rowAdapter.notifyItemRangeChanged(0, specialTasks.size());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(NAME_LIST, specialTasks);
        outState.putStringArrayList(NAME_LIST_ID, specialTasksId);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor positionEditor = specialTaskPreference.edit();
        positionEditor.clear();
        for (int i = 0; i < specialTasks.size(); i++) {

            String id = specialTasksId.get(i),
                    key = getKey(i, id);

            positionEditor.putString(key, specialTasks.get(i));
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

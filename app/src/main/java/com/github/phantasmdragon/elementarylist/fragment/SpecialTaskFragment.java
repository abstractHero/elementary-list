package com.github.phantasmdragon.elementarylist.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.os.Bundle;
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

public class SpecialTaskFragment extends Fragment {

    private final String NAME_LIST = "specialList";
    private final String NAME_FILE = "SpecialTaskList";

    private CustomRowAdapter rowAdapter;
    private RecyclerView specialTaskRecycler;
    private SharedPreferences specialTaskPreference;

    private ArrayList<String> specialTasks = new ArrayList<>();


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
        Map<String, ?> allPreference = specialTaskPreference.getAll();
        if (allPreference.size() > 0) {
            for (String set: allPreference.keySet()) {
                String taskName = specialTaskPreference.getString(set, "");
                specialTasks.add(0, taskName);
            }
        }
    }

    private void saveTask(String taskName) {
        SharedPreferences.Editor saveEditor = specialTaskPreference.edit();
        saveEditor.putString(String.valueOf(taskName.hashCode()), taskName);
        saveEditor.apply();
    }

    private void deleteTask(String taskName) {
        SharedPreferences.Editor deleteEditor = specialTaskPreference.edit();
        deleteEditor.remove(String.valueOf(taskName.hashCode()));
        deleteEditor.apply();
    }

    public void addTask(Bundle infoAboutNewTask) {
        String taskName = infoAboutNewTask.getString(MainActivity.NAME_TASK);
        specialTasks.add(0, taskName);
        saveTask(taskName);
        rowAdapter.notifyItemRangeChanged(0, specialTasks.size());

        specialTaskRecycler.scrollToPosition(0);
    }

    public Bundle getInfoAboutTask(int position) {
        Bundle infoAboutTask = new Bundle();
        infoAboutTask.putString(MainActivity.NAME_TASK, specialTasks.get(position));
        return infoAboutTask;
    }

    public void removeTask(int position) {
        deleteTask(specialTasks.get(position));
        specialTasks.remove(position);
        rowAdapter.notifyItemRemoved(position);
        rowAdapter.notifyItemRangeChanged(0, specialTasks.size());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(NAME_LIST, specialTasks);
    }
}

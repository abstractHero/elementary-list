package com.github.phantasmdragon.elementarylist.fragment.template;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.phantasmdragon.elementarylist.R;
import com.github.phantasmdragon.elementarylist.activity.MainActivity;
import com.github.phantasmdragon.elementarylist.custom.itemdecoration.CrossedOutItemDecoration;
import com.github.phantasmdragon.elementarylist.custom.rowadapter.CustomRowAdapter;
import com.github.phantasmdragon.elementarylist.fragment.CompletedTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.UnfulfilledTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.async.service.SaveTaskService;
import com.github.phantasmdragon.elementarylist.fragment.helper.callback.ItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.UUID;

public class TemplateTaskFragment extends Fragment {

    private String nameList;
    private String nameFile;
    private String nameListId;
    private String nameFragment;

    private CustomRowAdapter rowAdapter;
    private RecyclerView taskRecycler;
    private SharedPreferences taskPreference;

    private ArrayList<String> task = new ArrayList<>();
    private ArrayList<String> taskId = new ArrayList<>();

    private int mLayout;
    private int mRecyclerId;
    private boolean mTouched = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNames();
        setLayoutAndRecycler();
        taskPreference = getActivity().getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        loadTask();
        setRetainInstance(true);
    }

    private void setNames() {
        Bundle names = getArguments();
        nameList = names.getString("nameList");
        nameFile = names.getString("nameFile");
        nameListId = names.getString("nameListId");
        nameFragment = names.getString("nameFragment");
    }

    private void setLayoutAndRecycler() {
        if (nameFragment.equals(UnfulfilledTaskFragment.NAME_THIS)) {
            mLayout = R.layout.fragment_current_task;
            mRecyclerId = R.id.recycler_current_task;
        } else if (nameFragment.equals(CompletedTaskFragment.NAME_THIS)) {
            mLayout = R.layout.fragment_completed_task;
            mRecyclerId = R.id.recycler_completed_task;
        } else {
            mLayout = R.layout.fragment_special_task;
            mRecyclerId = R.id.recycler_special_task;
        }
    }

    protected void loadTask() {
        TreeSet<String> sortKeySet = new TreeSet<>(taskPreference.getAll().keySet());
        for (String key: sortKeySet) {
            task.add(taskPreference.getString(key, ""));
            taskId.add(key.substring(key.indexOf("_")+1));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(mLayout, container, false);
        rowAdapter = new CustomRowAdapter(view.getContext(), task, nameFragment);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            task = savedInstanceState.getStringArrayList(nameList);
            taskId = savedInstanceState.getStringArrayList(nameListId);
        }

        taskRecycler = (RecyclerView)getActivity().findViewById(mRecyclerId);
        taskRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskRecycler.setAdapter(rowAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(rowAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(taskRecycler);
    }

    protected void setDecoration() {
        taskRecycler.addItemDecoration(new CrossedOutItemDecoration(getContext()));
    }

    public void addTask(Bundle infoAboutNewTask) {
        taskId.add(0, new UUID(System.currentTimeMillis(), System.nanoTime()).toString());
        task.add(0, infoAboutNewTask.getString(MainActivity.NAME_TASK));
        mTouched = true;

        saveTask(taskId.get(0), infoAboutNewTask);
    }

    public void updatingAdapterAfterAdd() {
        rowAdapter.notifyItemRangeChanged(0, task.size());
        taskRecycler.scrollToPosition(0); //TODO: Run is scroll depending on the settings
    }

    public void removeTask(int position) {
        mTouched = true;
        removeTaskFromFile(getKey(position, taskId.get(position)));
        task.remove(position);
        taskId.remove(position);
    }

    public void updatingAdapterAfterRemove(int position) {
        rowAdapter.notifyItemRemoved(position);
        rowAdapter.notifyItemRangeChanged(0, task.size());
    }

    private void saveTask(String taskId, Bundle taskInfo) {
        SharedPreferences.Editor saveEditor = taskPreference.edit();
        saveEditor.putString(taskId, taskInfo.getString(MainActivity.NAME_TASK))
                  .apply();
    }

    private void removeTaskFromFile(String taskId) {
        SharedPreferences.Editor deleteEditor = taskPreference.edit();
        if (taskPreference.contains(taskId)) deleteEditor.remove(taskId)
                                                         .apply();
        else deleteEditor.remove(getKeyWithoutPrefix(taskId))
                         .apply();
    }

    @NonNull
    public static String getKey(int index, String id) {
        return String.valueOf(index).concat("_" + id);
    }

    @NonNull
    private String getKeyWithoutPrefix(String key) {
        return key.substring(key.indexOf("_")+1);
    }

    public Bundle getInfoAboutTask(int position) {
        Bundle infoAboutTask = new Bundle();
        infoAboutTask.putString(MainActivity.NAME_TASK, task.get(position));
        return infoAboutTask;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(nameList, task);
        outState.putStringArrayList(nameListId, taskId);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTouched) {
            mTouched = false;

            Intent intent = new Intent(getContext(), SaveTaskService.class);
            intent.putStringArrayListExtra("task", task);
            intent.putStringArrayListExtra("taskId", taskId);
            intent.putExtra("nameFile", nameFile);
            getActivity().startService(intent);
        }
    }
}

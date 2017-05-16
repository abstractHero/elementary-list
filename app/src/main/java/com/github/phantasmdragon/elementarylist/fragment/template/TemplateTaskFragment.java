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

    private String mNameList;
    private String mNameFile;
    private String mNameListId;
    private String mNameFragment;

    private CustomRowAdapter mRowAdapter;
    private RecyclerView mTaskRecycler;
    private SharedPreferences mTaskPreference;

    private ArrayList<String> mTask = new ArrayList<>();
    private ArrayList<String> mTaskId = new ArrayList<>();

    private int mLayout;
    private int mRecyclerId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNames();
        setLayoutAndRecycler();
        mTaskPreference = getActivity().getSharedPreferences(mNameFile, Context.MODE_PRIVATE);
        loadTask();
        setRetainInstance(true);
    }

    private void setNames() {
        Bundle names = getArguments();
        mNameList = names.getString("mNameList");
        mNameFile = names.getString("mNameFile");
        mNameListId = names.getString("mNameListId");
        mNameFragment = names.getString("mNameFragment");
    }

    private void setLayoutAndRecycler() {
        if (mNameFragment.equals(UnfulfilledTaskFragment.NAME_THIS)) {
            mLayout = R.layout.fragment_current_task;
            mRecyclerId = R.id.recycler_current_task;
        } else if (mNameFragment.equals(CompletedTaskFragment.NAME_THIS)) {
            mLayout = R.layout.fragment_completed_task;
            mRecyclerId = R.id.recycler_completed_task;
        } else {
            mLayout = R.layout.fragment_special_task;
            mRecyclerId = R.id.recycler_special_task;
        }
    }

    protected void loadTask() {
        TreeSet<String> sortKeySet = new TreeSet<>(mTaskPreference.getAll().keySet());
        for (String key: sortKeySet) {
            mTask.add(mTaskPreference.getString(key, ""));
            mTaskId.add(key.substring(key.indexOf("_")+1));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(mLayout, container, false);
        mRowAdapter = new CustomRowAdapter(view.getContext(), mTask, mNameFragment);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mTask = savedInstanceState.getStringArrayList(mNameList);
            mTaskId = savedInstanceState.getStringArrayList(mNameListId);
        }

        mTaskRecycler = (RecyclerView)getActivity().findViewById(mRecyclerId);
        mTaskRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskRecycler.setAdapter(mRowAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mRowAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mTaskRecycler);
    }

    protected void setDecoration() {
        mTaskRecycler.addItemDecoration(new CrossedOutItemDecoration(getContext()));
    }

    public void addTask(Bundle infoAboutNewTask) {
        mTaskId.add(0, new UUID(System.currentTimeMillis(), System.nanoTime()).toString());
        mTask.add(0, infoAboutNewTask.getString(MainActivity.NAME_TASK));

        saveTask(mTaskId.get(0), infoAboutNewTask);
    }

    public void updatingAdapterAfterAdd() {
        mRowAdapter.notifyItemRangeChanged(0, mTask.size());
        mTaskRecycler.scrollToPosition(0);
    }

    public void removeTask(int position) {
        removeTaskFromFile(getKey(position, mTaskId.get(position)));
        mTask.remove(position);
        mTaskId.remove(position);
    }

    public void updatingAdapterAfterRemove(int position) {
        mRowAdapter.notifyItemRemoved(position);
        mRowAdapter.notifyItemRangeChanged(0, mTask.size());
    }

    private void saveTask(String taskId, Bundle taskInfo) {
        SharedPreferences.Editor saveEditor = mTaskPreference.edit();
        saveEditor.putString(taskId, taskInfo.getString(MainActivity.NAME_TASK))
                  .apply();
    }

    private void removeTaskFromFile(String taskId) {
        SharedPreferences.Editor deleteEditor = mTaskPreference.edit();
        if (mTaskPreference.contains(taskId)) deleteEditor.remove(taskId)
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
        infoAboutTask.putString(MainActivity.NAME_TASK, mTask.get(position));
        return infoAboutTask;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(mNameList, mTask);
        outState.putStringArrayList(mNameListId, mTaskId);
    }

    @Override
    public void onPause() {
        super.onPause();
        Intent intent = new Intent(getContext(), SaveTaskService.class);
        intent.putStringArrayListExtra("mTask", mTask);
        intent.putStringArrayListExtra("mTaskId", mTaskId);
        intent.putExtra("mNameFile", mNameFile);
        getActivity().startService(intent);
    }
}

package com.github.phantasmdragon.elementarylist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.phantasmdragon.elementarylist.activity.MainActivity;
import com.github.phantasmdragon.elementarylist.fragment.template.TemplateTaskFragment;

public class CompletedTaskFragment extends TemplateTaskFragment {

    public static final String NAME_THIS = CompletedTaskFragment.class.getSimpleName();

    private static final String NAME_LIST_ID = "completedId";
    private static final String NAME_LIST = "completedList";
    private static final String NAME_FILE = "CompletedTaskList";

    public static CompletedTaskFragment newInstance() {
        CompletedTaskFragment completedTaskFragment = new CompletedTaskFragment();
        completedTaskFragment.setArguments(getNames());
        return completedTaskFragment;
    }

    private static Bundle getNames() {
        Bundle names = new Bundle();
        names.putString("nameList", NAME_LIST);
        names.putString("nameFile", NAME_FILE);
        names.putString("nameListId", NAME_LIST_ID);
        names.putString("nameFragment", CompletedTaskFragment.NAME_THIS);
        return names;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDecoration();
    }

    @Override
    public void addTask(Bundle infoAboutNewTask) {
        String nameTask = infoAboutNewTask.getString(MainActivity.NAME_TASK);
        if (nameTask == null) throw new NullPointerException(CompletedTaskFragment.NAME_THIS + ": nameTask is null in addTask");

        if (infoAboutNewTask.containsKey(SpecialTaskFragment.IS_SPECIAL)) {
            infoAboutNewTask.putString(MainActivity.NAME_TASK, nameTask.concat("1"));
        } else {
            infoAboutNewTask.putString(MainActivity.NAME_TASK, nameTask.concat("0"));
        }
        super.addTask(infoAboutNewTask);
    }

    @Override
    public Bundle getInfoAboutTask(int position) {
        Bundle info = super.getInfoAboutTask(position);
        String nameTask = info.getString(MainActivity.NAME_TASK);
        if (nameTask == null) throw new NullPointerException(CompletedTaskFragment.NAME_THIS + ": nameTask is null in getInfoAboutTask");

        info.putString(MainActivity.NAME_TASK, nameTask.substring(0, nameTask.length()-1));
        return info;
    }

    public boolean isSpecialTask(int position) {
        Bundle info = super.getInfoAboutTask(position);
        String nameTask = info.getString(MainActivity.NAME_TASK);
        return nameTask != null && nameTask.endsWith("1");
    }
}

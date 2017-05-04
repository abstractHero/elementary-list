package com.github.phantasmdragon.elementarylist.fragment;

import android.os.Bundle;

import com.github.phantasmdragon.elementarylist.fragment.template.TemplateTaskFragment;

public class CompletedTaskFragment extends TemplateTaskFragment {

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
        names.putString("nameFragment", CompletedTaskFragment.class.getSimpleName());
        return names;
    }
}

package com.github.phantasmdragon.elementarylist.fragment;

import android.os.Bundle;

import com.github.phantasmdragon.elementarylist.fragment.template.TemplateTaskFragment;

public class CurrentTaskFragment extends TemplateTaskFragment {

    private static final String NAME_LIST_ID = "unfulfilledId";
    private static final String NAME_LIST = "unfulfilledList";
    private static final String NAME_FILE = "CurrentTaskList";

    public static CurrentTaskFragment newInstance() {
        CurrentTaskFragment currentTaskFragment = new CurrentTaskFragment();
        currentTaskFragment.setArguments(getNames());
        return currentTaskFragment;
    }

    private static Bundle getNames() {
        Bundle names = new Bundle();
        names.putString("nameList", NAME_LIST);
        names.putString("nameFile", NAME_FILE);
        names.putString("nameListId", NAME_LIST_ID);
        names.putString("nameFragment", CurrentTaskFragment.class.getSimpleName());
        return names;
    }
}

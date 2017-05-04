package com.github.phantasmdragon.elementarylist.fragment;

import android.os.Bundle;

import com.github.phantasmdragon.elementarylist.fragment.template.TemplateTaskFragment;

public class UnfulfilledTaskFragment extends TemplateTaskFragment {

    private static final String NAME_LIST_ID = "unfulfilledId";
    private static final String NAME_LIST = "unfulfilledList";
    private static final String NAME_FILE = "UnfulfilledTaskList";

    public static UnfulfilledTaskFragment newInstance() {
        UnfulfilledTaskFragment unfulfilledTaskFragment = new UnfulfilledTaskFragment();
        unfulfilledTaskFragment.setArguments(getNames());
        return unfulfilledTaskFragment;
    }

    private static Bundle getNames() {
        Bundle names = new Bundle();
        names.putString("nameList", NAME_LIST);
        names.putString("nameFile", NAME_FILE);
        names.putString("nameListId", NAME_LIST_ID);
        names.putString("nameFragment", UnfulfilledTaskFragment.class.getSimpleName());
        return names;
    }
}

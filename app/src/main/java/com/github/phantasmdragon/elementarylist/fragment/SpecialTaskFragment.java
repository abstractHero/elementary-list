package com.github.phantasmdragon.elementarylist.fragment;

import android.os.Bundle;

import com.github.phantasmdragon.elementarylist.fragment.template.TemplateTaskFragment;

public class SpecialTaskFragment extends TemplateTaskFragment {

    private static final String NAME_LIST_ID = "specialId";
    private static final String NAME_LIST = "specialList";
    private static final String NAME_FILE = "SpecialTaskList";

    public static SpecialTaskFragment newInstance() {
        SpecialTaskFragment specialTaskFragment = new SpecialTaskFragment();
        specialTaskFragment.setArguments(getNames());
        return specialTaskFragment;
    }

    private static Bundle getNames() {
        Bundle names = new Bundle();
        names.putString("nameList", NAME_LIST);
        names.putString("nameFile", NAME_FILE);
        names.putString("nameListId", NAME_LIST_ID);
        names.putString("nameFragment", SpecialTaskFragment.class.getSimpleName());
        return names;
    }
}

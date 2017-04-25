package com.github.phantasmdragon.elementarylist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.phantasmdragon.elementarylist.R;
import com.github.phantasmdragon.elementarylist.custom.rowadapter.CustomRowAdapter;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;

public class CurrentTaskFragment extends ListFragment {

    //Для демонстрации, удалится в скором времени
    private final String[] catNames = new String[]{"Один", "Два", "Три",};

    private CustomRowAdapter rowListAdapter;

    @Contract(" -> !null")
    public static CurrentTaskFragment newInstance() {
        return new CurrentTaskFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rowListAdapter = new CustomRowAdapter(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(catNames)));
        setListAdapter(rowListAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_task, container, false);
        return view;
    }
}

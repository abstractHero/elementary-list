package com.github.phantasmdragon.elementarylist.fragment.async.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import static com.github.phantasmdragon.elementarylist.fragment.template.TemplateTaskFragment.getKey;

public class SaveTaskService extends IntentService {

    public SaveTaskService() {
        super(String.valueOf(System.currentTimeMillis()));
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) throw new NullPointerException("IntentService: Intent is null in onHandleIntent");

        String nameFile = intent.getStringExtra("nameFile");
        ArrayList<String> task = intent.getStringArrayListExtra("task");
        ArrayList<String> taskId = intent.getStringArrayListExtra("taskId");

        SharedPreferences taskPreference = getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor positionEditor = taskPreference.edit();

        positionEditor.clear();
        for (int i = 0; i < task.size(); i++) {

            String id = taskId.get(i),
                   key = getKey(i, id);

            positionEditor.putString(key, task.get(i));
        }
        positionEditor.apply();
    }
}

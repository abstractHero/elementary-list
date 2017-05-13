package com.github.phantasmdragon.elementarylist.activity.async;

import android.os.AsyncTask;
import android.os.Bundle;

import com.github.phantasmdragon.elementarylist.fragment.template.TemplateTaskFragment;

public class MoveAsyncTask<T extends TemplateTaskFragment, E extends TemplateTaskFragment> extends AsyncTask<Integer, Void, Void> {

    private T fromWhich;
    private E whither;

    private int mPosition;

    public MoveAsyncTask(T fromWhich, E whither) {
        this.fromWhich = fromWhich;
        this.whither = whither;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        mPosition = params[0];
        addTask(getBundleTask());
        return null;
    }

    private Bundle getBundleTask() {
        Bundle info = fromWhich.getInfoAboutTask(mPosition);
        fromWhich.removeTask(mPosition);
        publishProgress();

        return info;
    }

    @Override
    protected void onProgressUpdate(Void... object) {
        fromWhich.updatingAdapterAfterRemove(mPosition);
    }

    private void addTask(Bundle info) {
        whither.addTask(info);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        whither.updatingAdapterAfterAdd();
    }
}

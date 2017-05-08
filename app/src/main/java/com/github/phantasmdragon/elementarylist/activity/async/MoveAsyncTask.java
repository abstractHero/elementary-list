package com.github.phantasmdragon.elementarylist.activity.async;

import android.os.AsyncTask;
import android.os.Bundle;

import com.github.phantasmdragon.elementarylist.fragment.CompletedTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.SpecialTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.UnfulfilledTaskFragment;

public class MoveAsyncTask extends AsyncTask<Object, Object, Void> {

    private Object mNewPlaceForTask;

    private int mPosition;

    @Override
    protected Void doInBackground(Object... params) {
        mPosition = (Integer) params[2];
        addTask(params[1], getBundleTask(params[0]));
        return null;
    }

    private Bundle getBundleTask(Object fromWhich) {
        Bundle info;
        if (fromWhich instanceof CompletedTaskFragment) {
            CompletedTaskFragment completed = (CompletedTaskFragment)fromWhich;
            info = completed.getInfoAboutTask(mPosition);
            completed.removeTask(mPosition);
        } else if (fromWhich instanceof UnfulfilledTaskFragment) {
            UnfulfilledTaskFragment unfulfilled = (UnfulfilledTaskFragment)fromWhich;
            info = unfulfilled.getInfoAboutTask(mPosition);
            unfulfilled.removeTask(mPosition);
        } else {
            SpecialTaskFragment special = (SpecialTaskFragment)fromWhich;
            info = special.getInfoAboutTask(mPosition);
            special.removeTask(mPosition);
        }
        publishProgress(fromWhich);
        return info;
    }

    @Override
    protected void onProgressUpdate(Object... object) {
        Object fragment = object[0];
        if (fragment instanceof CompletedTaskFragment) {
            ((CompletedTaskFragment)fragment).updatingAdapterAfterRemove(mPosition);
        } else if (fragment instanceof UnfulfilledTaskFragment) {
            ((UnfulfilledTaskFragment)fragment).updatingAdapterAfterRemove(mPosition);
        } else {
            ((SpecialTaskFragment)fragment).updatingAdapterAfterRemove(mPosition);
        }
    }

    private void addTask(Object whither, Bundle info) {
        if (whither instanceof CompletedTaskFragment) {
            ((CompletedTaskFragment)whither).addTask(info);
        } else if (whither instanceof UnfulfilledTaskFragment) {
            ((UnfulfilledTaskFragment)whither).addTask(info);
        } else {
            ((SpecialTaskFragment)whither).addTask(info);
        }
        mNewPlaceForTask = whither;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mNewPlaceForTask instanceof CompletedTaskFragment) {
            ((CompletedTaskFragment) mNewPlaceForTask).updatingAdapterAfterAdd();
        } else if (mNewPlaceForTask instanceof UnfulfilledTaskFragment) {
            ((UnfulfilledTaskFragment) mNewPlaceForTask).updatingAdapterAfterAdd();
        } else {
            ((SpecialTaskFragment) mNewPlaceForTask).updatingAdapterAfterAdd();
        }
    }
}

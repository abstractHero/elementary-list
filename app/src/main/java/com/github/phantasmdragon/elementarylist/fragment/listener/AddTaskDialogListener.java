package com.github.phantasmdragon.elementarylist.fragment.listener;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * The activity that creates an instance of this dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it.
 */
public interface AddTaskDialogListener {
    void onDialogPositiveClick(DialogFragment dialog, Bundle infoAboutNewTask);
}

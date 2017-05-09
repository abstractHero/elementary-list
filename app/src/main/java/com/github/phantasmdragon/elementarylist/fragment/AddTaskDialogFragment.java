package com.github.phantasmdragon.elementarylist.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.github.phantasmdragon.elementarylist.R;
import com.github.phantasmdragon.elementarylist.activity.MainActivity;
import com.github.phantasmdragon.elementarylist.fragment.listener.AddTaskDialogListener;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AddTaskDialogFragment extends DialogFragment {

    private AddTaskDialogListener mListener;

    private InputMethodManager mInputManager;
    private EditText mEnteredName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (AddTaskDialogListener)getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement AddTaskDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_add_task, null);

        builder.setView(layout)
                .setTitle(getResources().getString(R.string.dialog_title))
                .setPositiveButton(R.string.task_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        hideKeyboard();
                        sendInfoFromDialog();
                    }
                })
                .setNegativeButton(R.string.task_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        hideKeyboard();
                        dialog.cancel();
                    }
                });

        mEnteredName = (EditText)layout.findViewById(R.id.edit_task_name);
        mInputManager = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);

        return builder.create();
    }

    private void hideKeyboard() {
        mInputManager.hideSoftInputFromWindow(mEnteredName.getWindowToken(), 0);
    }

    private void sendInfoFromDialog() {
        String taskName = mEnteredName.getText().toString();

        Bundle info = new Bundle();
        info.putString(MainActivity.NAME_TASK, taskName);

        mListener.onDialogPositiveClick(this, info);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }
}

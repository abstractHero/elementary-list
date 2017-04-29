package com.github.phantasmdragon.elementarylist.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.content.DialogInterface;

import com.github.phantasmdragon.elementarylist.R;
import com.github.phantasmdragon.elementarylist.fragment.listener.AddTaskDialogListener;

public class AddTaskDialogFragment extends DialogFragment {

    private AddTaskDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add_task, null))
                .setTitle(getResources().getString(R.string.dialog_title))
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sendInfoFromDialog();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    private void sendInfoFromDialog() {
        EditText enteredName = (EditText)getDialog().findViewById(R.id.edit_task_name);
        String taskName = "";
        if (enteredName != null) taskName = enteredName.getText().toString();

        Bundle info = new Bundle();
        info.putString("task_name", taskName);

        mListener.onDialogPositiveClick(this, info);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (AddTaskDialogListener)getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement AddTaskDialogListener");
        }
    }
}

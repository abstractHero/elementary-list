package com.github.phantasmdragon.elementarylist.custom.rowadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.phantasmdragon.elementarylist.R;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;

public class CustomRowAdapter extends RecyclerView.Adapter<CustomRowAdapter.ViewHolder> {

    private List<String> mCurrentTasks;
    private Context      mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox    mIsTaskCompleted;
        public TextView    mDescriptionTask;
        public ShineButton mIsImportantTask;

        public ViewHolder(View itemView) {
            super(itemView);
            mIsTaskCompleted = (CheckBox)itemView.findViewById(R.id.check_box_item);
            mDescriptionTask = (TextView)itemView.findViewById(R.id.text_item);
            mIsImportantTask = (ShineButton)itemView.findViewById(R.id.button_item);
        }
    }

    public CustomRowAdapter(Context context, List<String> tasks) {
        mCurrentTasks = tasks;
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public CustomRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);

        View taskView = inflater.inflate(R.layout.item_fragment_list, parent, false);

        return new ViewHolder(taskView);
    }

    @Override
    public void onBindViewHolder(CustomRowAdapter.ViewHolder holder, int position) {
        String nameTask = mCurrentTasks.get(position);

        holder.mDescriptionTask.setText(nameTask);
    }

    @Override
    public int getItemCount() {
        return mCurrentTasks.size();
    }
}

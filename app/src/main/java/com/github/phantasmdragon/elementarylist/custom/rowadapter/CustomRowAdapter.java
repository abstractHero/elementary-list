package com.github.phantasmdragon.elementarylist.custom.rowadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.phantasmdragon.elementarylist.R;
import com.github.phantasmdragon.elementarylist.fragment.CompletedTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.CurrentTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.SpecialTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.listener.OnCompletedClickListener;
import com.github.phantasmdragon.elementarylist.fragment.listener.OnSpecialClickListener;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;

public class CustomRowAdapter extends RecyclerView.Adapter<CustomRowAdapter.ViewHolder> {

    private final int CLICK_DELAY = 800;

    private OnCompletedClickListener mCompletedListener;
    private OnSpecialClickListener mSpecialListener;

    private List<String> mFragmentTasks;
    private Context mContext;
    private String mNameFragment;

    //boolean variable will be deleted later
    private boolean mIsOneCom = true, mIsOneImp = true;
    private long mTimeLastClickOnComplete = System.currentTimeMillis();
    private long mTimeLastClickOnImportant = System.currentTimeMillis();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox mIsTaskCompleted;
        public TextView mDescriptionTask;
        public ShineButton mIsImportantTask;

        public ViewHolder(View itemView) {
            super(itemView);
            mIsTaskCompleted = (CheckBox)itemView.findViewById(R.id.check_box_item);
            mDescriptionTask = (TextView)itemView.findViewById(R.id.text_item);
            mIsImportantTask = (ShineButton)itemView.findViewById(R.id.button_item);
        }
    }

    public CustomRowAdapter(Context context, List<String> tasks, String nameFragment) {
        mFragmentTasks = tasks;
        mContext = context;
        mNameFragment = nameFragment;
        mCompletedListener = (OnCompletedClickListener)mContext;
        mSpecialListener = (OnSpecialClickListener)mContext;
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
    public void onBindViewHolder(CustomRowAdapter.ViewHolder holder, final int position) {
        String nameTask = mFragmentTasks.get(position);

        if (mNameFragment.equals(CompletedTaskFragment.class.getSimpleName())) {
            holder.mIsTaskCompleted.setChecked(true);
            holder.mIsImportantTask.setEnabled(false);
        }
        if (mNameFragment.equals(CurrentTaskFragment.class.getSimpleName())) {
            holder.mIsTaskCompleted.setChecked(false);
            holder.mIsImportantTask.setChecked(false);
        }
        if (mNameFragment.equals(SpecialTaskFragment.class.getSimpleName())) {
            holder.mIsTaskCompleted.setChecked(false);
            holder.mIsImportantTask.setChecked(true);
        }

        holder.mDescriptionTask.setText(nameTask);
        holder.mIsTaskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - mTimeLastClickOnComplete > CLICK_DELAY || mIsOneCom) {
                    mTimeLastClickOnComplete = System.currentTimeMillis();
                    mCompletedListener.onTaskFinishClick(position, mNameFragment);
                    mIsOneCom = false;
                }
            }
        });
        holder.mIsImportantTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - mTimeLastClickOnImportant > CLICK_DELAY || mIsOneImp) {
                    mTimeLastClickOnImportant = System.currentTimeMillis();
                    mSpecialListener.onTaskSpecialClick(position, mNameFragment);
                    mIsOneImp = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFragmentTasks.size();
    }
}

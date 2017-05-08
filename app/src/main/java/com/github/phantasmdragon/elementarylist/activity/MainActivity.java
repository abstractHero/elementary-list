package com.github.phantasmdragon.elementarylist.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.phantasmdragon.elementarylist.R;
import com.github.phantasmdragon.elementarylist.activity.async.MoveAsyncTask;
import com.github.phantasmdragon.elementarylist.fragment.AddTaskDialogFragment;
import com.github.phantasmdragon.elementarylist.fragment.CompletedTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.SpecialTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.UnfulfilledTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.listener.AddTaskDialogListener;
import com.github.phantasmdragon.elementarylist.fragment.listener.OnCompletedClickListener;
import com.github.phantasmdragon.elementarylist.fragment.listener.OnSpecialClickListener;

public class MainActivity extends AppCompatActivity implements AddTaskDialogListener,
                                                               OnCompletedClickListener,
                                                               OnSpecialClickListener {

    public static final String NAME_TASK = "task_name";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private UnfulfilledTaskFragment unfulfilledTaskFragment;
    private CompletedTaskFragment completedTaskFragment;
    private SpecialTaskFragment specialTaskFragment;

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Bundle infoAboutNewTask) {
        unfulfilledTaskFragment.addTask(infoAboutNewTask);
        unfulfilledTaskFragment.updatingAdapterAfterAdd();
    }

    @Override
    public void onTaskSpecialClick(int position, String nameFragment) {
        if (nameFragment.equals(UnfulfilledTaskFragment.NAME_THIS)) {
            moveTask(unfulfilledTaskFragment, specialTaskFragment, position);
        } else if (nameFragment.equals(SpecialTaskFragment.NAME_THIS)) {
            moveTask(specialTaskFragment, unfulfilledTaskFragment, position);
        }
    }

    @Override
    public void onTaskFinishClick(int position, String nameFragment) {
        if (nameFragment.equals(UnfulfilledTaskFragment.NAME_THIS)) {
            moveTask(unfulfilledTaskFragment, completedTaskFragment, position);
        } else if (nameFragment.equals(CompletedTaskFragment.NAME_THIS)) {
            moveTask(completedTaskFragment, unfulfilledTaskFragment, position);
        } else {
            moveTask(specialTaskFragment, completedTaskFragment, position);
        }
    }

    private void moveTask(Object fromWhich, Object whither, int currentPosition) {
        new MoveAsyncTask().execute(fromWhich, whither, currentPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            unfulfilledTaskFragment = (UnfulfilledTaskFragment)getSupportFragmentManager().getFragment(savedInstanceState, "unfulfilledTaskFragment");
            completedTaskFragment = (CompletedTaskFragment)getSupportFragmentManager().getFragment(savedInstanceState, "completedTaskFragment");
            specialTaskFragment = (SpecialTaskFragment)getSupportFragmentManager().getFragment(savedInstanceState, "specialTaskFragment");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_float);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "unfulfilledTaskFragment", unfulfilledTaskFragment);
        getSupportFragmentManager().putFragment(outState, "completedTaskFragment", completedTaskFragment);
        getSupportFragmentManager().putFragment(outState, "specialTaskFragment", specialTaskFragment);
    }

    private void showAddDialog() {
        AddTaskDialogFragment dialog = new AddTaskDialogFragment();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "addTask");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                completedTaskFragment = CompletedTaskFragment.newInstance();
                return completedTaskFragment;
            } else if (position == 1) {
                unfulfilledTaskFragment = UnfulfilledTaskFragment.newInstance();
                return unfulfilledTaskFragment;
            } else {
                specialTaskFragment = SpecialTaskFragment.newInstance();
                return specialTaskFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Finished";
                case 1:
                    return "Current";
                case 2:
                    return "Special";
            }
            return null;
        }
    }
}

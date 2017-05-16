package com.github.phantasmdragon.elementarylist.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.github.phantasmdragon.elementarylist.R;
import com.github.phantasmdragon.elementarylist.activity.async.MoveAsyncTask;
import com.github.phantasmdragon.elementarylist.custom.behavior.floatingbutton.MovingUnderScreenBehavior;
import com.github.phantasmdragon.elementarylist.fragment.AddTaskDialogFragment;
import com.github.phantasmdragon.elementarylist.fragment.CompletedTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.SpecialTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.UnfulfilledTaskFragment;
import com.github.phantasmdragon.elementarylist.fragment.listener.AddTaskDialogListener;
import com.github.phantasmdragon.elementarylist.fragment.listener.OnCompletedClickListener;
import com.github.phantasmdragon.elementarylist.fragment.listener.OnSpecialClickListener;
import com.github.phantasmdragon.elementarylist.fragment.template.TemplateTaskFragment;

import org.jetbrains.annotations.Contract;

public class MainActivity extends AppCompatActivity implements AddTaskDialogListener,
                                                               OnCompletedClickListener,
                                                               OnSpecialClickListener {

    public static final String NAME_TASK = "task_name";

    private UnfulfilledTaskFragment unfulfilledTaskFragment;
    private CompletedTaskFragment completedTaskFragment;
    private SpecialTaskFragment specialTaskFragment;

    private InputMethodManager mInputManager;

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
            whitherMove(position);
        } else {
            moveTask(specialTaskFragment, completedTaskFragment, position);
        }
    }

    private <T extends TemplateTaskFragment, E extends TemplateTaskFragment> void moveTask(T fromWhich, E whither, int currentPosition) {
        new MoveAsyncTask<>(fromWhich, whither).execute(currentPosition);
    }

    private void whitherMove(int position) {
        if (completedTaskFragment.isSpecialTask(position)) {
            moveTask(completedTaskFragment, specialTaskFragment, position);
        } else {
            moveTask(completedTaskFragment, unfulfilledTaskFragment, position);
        }
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

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mInputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_float);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)fab.getLayoutParams();
        layoutParams.setBehavior(new MovingUnderScreenBehavior());
        fab.setLayoutParams(layoutParams);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(getCentralTab(sectionsPagerAdapter.getCount()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:  fab.show();
                        break;
                    default: fab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Contract(pure = true)
    private int getCentralTab(int quantityTabs) {
        int centralTab;
        if (quantityTabs%2 != 0) centralTab = (quantityTabs-1)/2;
        else centralTab = quantityTabs/2;

        return centralTab;
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
        mInputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

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
                    return getResources().getString(R.string.tab_finished);
                case 1:
                    return getResources().getString(R.string.tab_current);
                case 2:
                    return getResources().getString(R.string.tab_special);
            }
            return null;
        }
    }
}

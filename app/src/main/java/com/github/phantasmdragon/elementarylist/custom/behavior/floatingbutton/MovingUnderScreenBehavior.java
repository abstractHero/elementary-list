package com.github.phantasmdragon.elementarylist.custom.behavior.floatingbutton;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class MovingUnderScreenBehavior extends FloatingActionButton.Behavior {

    private static final int ANIMATE_DURATION = 200;

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if (dyConsumed > 0) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)child.getLayoutParams();
            int fabBottomMargin = layoutParams.bottomMargin;
            child.animate().translationY(child.getHeight() + fabBottomMargin)
                           .setInterpolator(new LinearInterpolator())
                           .setDuration(ANIMATE_DURATION)
                           .start();
        } else if (dyConsumed < 0) {
            child.animate().translationY(0)
                           .setInterpolator(new LinearInterpolator())
                           .setDuration(ANIMATE_DURATION)
                           .start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}

package com.github.phantasmdragon.elementarylist.custom.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.phantasmdragon.elementarylist.R;

public class CrossedOutItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mCrossedOut;

    public CrossedOutItemDecoration(Context context) {
        mCrossedOut = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        View child = parent.getChildAt(0);
        if (child != null) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = params.leftMargin;
            int right = parent.getWidth() - params.rightMargin;
            int beg = child.getBottom() + params.bottomMargin;
            int bottom = beg + mCrossedOut.getIntrinsicHeight();
            mCrossedOut.setBounds(left, beg/2, right, bottom/2+2);
            mCrossedOut.draw(c);
            beg /= 2;

            for (int i = 1; i < parent.getChildCount(); i++) {
                child = parent.getChildAt(i);

                params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                bottom = top + mCrossedOut.getIntrinsicHeight();

                mCrossedOut.setBounds(left, top-beg, right, bottom-beg);
                mCrossedOut.draw(c);
            }
        }
    }
}

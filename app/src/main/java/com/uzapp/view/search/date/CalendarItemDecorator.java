package com.uzapp.view.search.date;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.uzapp.util.Constants;

/**
 * Created by vika on 22.07.16.
 */
public class CalendarItemDecorator extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = {android.R.attr.listDivider};

    private Drawable mDivider;
    private int daysOffset;

    public CalendarItemDecorator(Context context, int daysOffset) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        this.daysOffset = daysOffset;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
    }

    /**
     * Draw dividers at each expected grid interval
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        if (parent.getChildCount() == 0) return;

        final int childCount = parent.getChildCount();
        int lastRowElementsCount = (daysOffset + childCount) % Constants.DAYS_IN_WEEK;
        int lastRowFirstIndex;
        if (lastRowElementsCount == 0) {
            lastRowFirstIndex = childCount - Constants.DAYS_IN_WEEK;
        } else {
            lastRowFirstIndex = childCount - lastRowElementsCount;
        }
        for (int i = 0; i < lastRowFirstIndex; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) child.getLayoutParams();

            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

}
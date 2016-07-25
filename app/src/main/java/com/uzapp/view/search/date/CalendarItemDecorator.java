package com.uzapp.view.search.date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.uzapp.R;

/**
 * Created by vika on 22.07.16.
 */
public class CalendarItemDecorator extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int firstItemPadding;

    public CalendarItemDecorator(Context context, int firstItemPadding) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.calendar_divider);
        this.firstItemPadding = firstItemPadding;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        if (parent.getAdapter().getItemCount() == 0) return;

        final int childCount = parent.getAdapter().getItemCount();
//        int lastRowElementsCount = (firstItemPadding + childCount) % Constants.DAYS_IN_WEEK;
//
//        int lastRowFirstIndex;
//        if (lastRowElementsCount == 0) {
//            lastRowFirstIndex = childCount - Constants.DAYS_IN_WEEK;
//        } else {
//            lastRowFirstIndex = childCount - lastRowElementsCount;
//        }

        for (int i = 0; i < childCount; i++) {
            View child = parent.getLayoutManager().findViewByPosition(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int left = child.getLeft() - params.leftMargin;
            if (i == 0) {
                left += firstItemPadding;
            }
            int right = child.getRight() + params.rightMargin;
            int top = child.getTop() + params.topMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }

    }

}
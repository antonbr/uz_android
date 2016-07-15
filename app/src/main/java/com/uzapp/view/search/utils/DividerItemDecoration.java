package com.uzapp.view.search.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vika on 13.07.16.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable divider;
    private int paddingLeft;

    /**
     * Default divider will be used
     */
    public DividerItemDecoration(Context context) {
        setupDefaultDivider(context);
    }

    public DividerItemDecoration(Context context, int dividerRes, int paddingLeftRes) {
        this.paddingLeft = (int) context.getResources().getDimension(paddingLeftRes);
        if (divider != null) {
            this.divider = ContextCompat.getDrawable(context, dividerRes);
        } else {
            setupDefaultDivider(context);
        }
    }

    private void setupDefaultDivider(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + paddingLeft;
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount-1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}

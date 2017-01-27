package com.uzapp.view.common;

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
public class VerticalDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable divider;
    private int paddingLeft, paddingRight;

    /**
     * Default divider will be used
     */
    public VerticalDividerItemDecoration(Context context) {
        setupDefaultDivider(context);
    }

    public VerticalDividerItemDecoration(Context context, int dividerRes, int paddingLeft, int paddingRight) {
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
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
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + paddingLeft;
        int right = parent.getWidth() - parent.getPaddingRight() - paddingRight;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}

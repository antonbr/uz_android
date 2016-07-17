package com.uzapp.view.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vika on 17.07.16.
 */
public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int itemPadding;

    public HorizontalSpaceItemDecoration(int itemPadding) {
        this.itemPadding = itemPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.left = itemPadding;
        }
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.right = itemPadding;
        }
    }
}


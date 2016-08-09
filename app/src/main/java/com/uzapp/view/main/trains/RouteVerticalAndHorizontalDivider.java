package com.uzapp.view.main.trains;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.uzapp.R;

/**
 * Created by vika on 29.07.16.
 */
public class RouteVerticalAndHorizontalDivider extends RecyclerView.ItemDecoration {
    private Drawable verticalDivider;
    private Drawable horizontalLine;

    public RouteVerticalAndHorizontalDivider(Context context) {
        setupDefaultDivider(context);
    }

    private void setupDefaultDivider(Context context) {
        verticalDivider = ContextCompat.getDrawable(context, R.drawable.divider_hint_color);
        horizontalLine = ContextCompat.getDrawable(context, R.drawable.train_route_vertical_line);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVerticalDividers(parent, c);
        drawHorizontalLine(parent, c);

        super.onDraw(c, parent, state);

    }

    private void drawVerticalDividers(RecyclerView parent, Canvas c) {
        int childCount = parent.getChildCount();
        if (childCount == 0) {
            return;
        }

        int verticalDividerLeft = parent.getChildAt(0).findViewById(R.id.stationName).getLeft();
        int verticalDividerRight = parent.getWidth();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            if (i < childCount - 1) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int verticalDividerTop = child.getBottom() + params.bottomMargin;
                int verticalDividerBottom = verticalDividerTop + verticalDivider.getIntrinsicHeight();
                verticalDivider.setBounds(verticalDividerLeft, verticalDividerTop, verticalDividerRight, verticalDividerBottom);
                verticalDivider.draw(c);
            }
        }
    }

    private void drawHorizontalLine(RecyclerView parent, Canvas c) {
        int childCount = parent.getChildCount();
        if (childCount == 0) {
            return;
        }
        View circleFirstView = parent.getChildAt(0).findViewById(R.id.circle);
        int circleCenter = circleFirstView.getLeft() + circleFirstView.getWidth() / 2;
        int horizontalDividerLeft = parent.getChildAt(0).getPaddingLeft() + circleCenter - horizontalLine.getIntrinsicWidth() / 2;
        int horizontalDividerRight = horizontalDividerLeft + horizontalLine.getIntrinsicWidth();

        RecyclerView.Adapter adapter = parent.getAdapter();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            View childView = layoutManager.findViewByPosition(i);
            if (childView != null) {
                int horizontalDividerTop = 0;
                if (i == 0) {
                    horizontalDividerTop = childView.findViewById(R.id.circle).getTop() + childView.getPaddingTop();
                } else {
                    horizontalDividerTop = childView.getTop();
                }
                int horizontalDividerBottom = 0;
                if (i == adapter.getItemCount() - 1) {
                    horizontalDividerBottom = childView.getTop() + childView.findViewById(R.id.circle).getBottom() + childView.getPaddingTop();
                } else {
                    horizontalDividerBottom = childView.getBottom();
                }
                horizontalLine.setBounds(horizontalDividerLeft, horizontalDividerTop, horizontalDividerRight, horizontalDividerBottom);
                horizontalLine.draw(c);

            }
        }
    }
}

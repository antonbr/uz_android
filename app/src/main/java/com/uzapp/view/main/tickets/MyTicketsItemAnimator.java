package com.uzapp.view.main.tickets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vika on 30.08.16.
 */
public class MyTicketsItemAnimator extends DefaultItemAnimator {
    // Maps to hold running animators.  animatorMap is used to construct
    // the new change animation based on where the previous one was at when it
    // was interrupted.
    private ArrayMap<RecyclerView.ViewHolder, ObjectAnimator> animatorMap = new ArrayMap<>();

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        // This allows our custom change animation on the contents of the holder instead
        // of the default behavior of replacing the viewHolder entirely
        return true;
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull final RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preInfo, @NonNull ItemHolderInfo postInfo) {
        final MyTicketsAdapter.TicketHolder ticketHolder = (MyTicketsAdapter.TicketHolder) newHolder;
        cancelAnimation(ticketHolder);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ticketHolder.itemView, View.ROTATION_Y, ticketHolder.itemView.getRotationY(), ticketHolder.itemView.getRotationY() - 180);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorMap.remove(ticketHolder);
                dispatchAnimationFinished(ticketHolder);
            }
        });
        objectAnimator.start();
        animatorMap.put(ticketHolder, objectAnimator);
        return false;
    }

    private void cancelAnimation(RecyclerView.ViewHolder viewHolder) {
        if (animatorMap.containsKey(viewHolder)) {
            animatorMap.get(viewHolder).end();
        }
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        cancelAnimation(item);
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
        for (ObjectAnimator animator : animatorMap.values()) {
            animator.end();
        }
    }
}

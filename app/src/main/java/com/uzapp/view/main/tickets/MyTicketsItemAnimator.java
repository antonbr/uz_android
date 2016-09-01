package com.uzapp.view.main.tickets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 30.08.16.
 */
public class MyTicketsItemAnimator extends DefaultItemAnimator {
    private static final int ROTATION_DEGREE = 90;
    private static final int ANIMATION_DURATION = 300;
    private ArrayMap<RecyclerView.ViewHolder, AnimatorSet> animatorMap = new ArrayMap<>();

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags, @NonNull List<Object> payloads) {
        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
                if (payload instanceof TicketItemHolderInfo) {
                    return (ItemHolderInfo) payload;
                }
            }
        }
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull final RecyclerView.ViewHolder newHolder, @NonNull final ItemHolderInfo preInfo, @NonNull final ItemHolderInfo postInfo) {
        final MyTicketsAdapter.TicketHolder ticketHolder = (MyTicketsAdapter.TicketHolder) newHolder;
        if (!animatorMap.containsKey(ticketHolder)) {
            float fromRotationY = ticketHolder.itemView.getRotationY();
            float middleRotationY = fromRotationY - ROTATION_DEGREE;
            float toRotationY = middleRotationY - ROTATION_DEGREE;
            ticketHolder.itemView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            ObjectAnimator oldViewRotateAnimator = ObjectAnimator.ofFloat(ticketHolder.itemView, View.ROTATION_Y, fromRotationY, middleRotationY);
            final ObjectAnimator newViewRotateAnimator = ObjectAnimator.ofFloat(ticketHolder.itemView, View.ROTATION_Y, middleRotationY, toRotationY);
            oldViewRotateAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    TicketItemHolderInfo itemHolderInfo = (TicketItemHolderInfo) preInfo;
                    if (ticketHolder.isShowingTicketInfo) {
                        ticketHolder.isShowingTicketInfo = false;
                        ticketHolder.bindBackSide(itemHolderInfo.ticket, itemHolderInfo.position);
                    } else {
                        ticketHolder.isShowingTicketInfo = true;
                        ticketHolder.bindFrontSide(itemHolderInfo.ticket, itemHolderInfo.position);
                    }
                }
            });
            newViewRotateAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animatorMap.remove(ticketHolder);
                    dispatchAnimationFinished(ticketHolder);
                    ticketHolder.itemView.setLayerType(View.LAYER_TYPE_NONE, null);
                }
            });
            AnimatorSet animatorSet = new AnimatorSet();

            animatorSet.play(newViewRotateAnimator).after(oldViewRotateAnimator);
            oldViewRotateAnimator.setDuration(ANIMATION_DURATION);
            newViewRotateAnimator.setDuration(ANIMATION_DURATION);

            animatorSet.start();
            animatorMap.put(ticketHolder, animatorSet);
        }
        return false;
    }

    private void cancelAnimation(RecyclerView.ViewHolder viewHolder) {
        if (animatorMap.containsKey(viewHolder)) {
            ArrayList<Animator> animators = animatorMap.get(viewHolder).getChildAnimations();
            for (Animator animator : animators) {
                animator.end();
            }
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
        for (AnimatorSet animator : animatorMap.values()) {
            ArrayList<Animator> childAnimators = animator.getChildAnimations();
            for (Animator childAnimation : childAnimators) {
                childAnimation.end();
            }
        }
    }

    public static class TicketItemHolderInfo extends ItemHolderInfo {
        public ShortTicket ticket;
        public int position = -1;

        public TicketItemHolderInfo(ShortTicket ticket, int position) {
            this.ticket = ticket;
            this.position = position;
        }
    }
}

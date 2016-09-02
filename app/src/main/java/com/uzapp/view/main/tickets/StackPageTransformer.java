package com.uzapp.view.main.tickets;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vika on 02.09.16.
 */
public class StackPageTransformer implements ViewPager.PageTransformer {
    private int numberOfStacked;
    private float alphaFactor;
    private float zeroPositionScale;
    private float stackedScaleFactor;
    private float marginLeft;
    private float shiftFactor;

    public StackPageTransformer(int marginLeft, int numberOfStacked, float currentPageScale, float topStackedScale, float alphaFactor, float shiftFactor) {
        this.numberOfStacked = numberOfStacked;
        this.alphaFactor = alphaFactor;
        this.shiftFactor = shiftFactor;
        zeroPositionScale = currentPageScale;
        stackedScaleFactor = (currentPageScale - topStackedScale) / this.numberOfStacked;
        this.marginLeft = marginLeft;
    }

    @Override
    public void transformPage(View view, float position) {
        int screenWidth = view.getWidth();
        view.setPivotX(0f);
        view.setPivotY(view.getHeight() / 2);

        if (position >= (numberOfStacked + 1) || position <= -1) {
            view.setAlpha(0f);
        } else if (position > 0) {
            float alpha = 1.0f - (position * alphaFactor);
            view.setAlpha(1f);
            setAlphaForChildren(alpha, view);
            float scale = zeroPositionScale - (position * stackedScaleFactor);
            float baseTranslation = -position * screenWidth;
            float shiftTranslation = screenWidth * shiftFactor * position;
            view.setTranslationX(baseTranslation + marginLeft + shiftTranslation);
            view.setScaleX(scale);
            view.setScaleY(scale);
        } else if (position <= 0) {
            view.setAlpha(1f);
            setAlphaForChildren(1f, view);
            view.setScaleX(zeroPositionScale);
            view.setScaleY(zeroPositionScale);
            view.setTranslationX(marginLeft);
        }
    }

    private void setAlphaForChildren(float alpha, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setAlphaForChildren(alpha, viewGroup.getChildAt(i));
            }
        } else {
            view.setAlpha(alpha);
        }
    }
}

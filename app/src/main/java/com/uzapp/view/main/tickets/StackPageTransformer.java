package com.uzapp.view.main.tickets;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vika on 02.09.16.
 */
public class StackPageTransformer implements ViewPager.PageTransformer {

    private int mNumberOfStacked;

    private float mAlphaFactor;
    private float mZeroPositionScale;
    private float mStackedScaleFactor;
    private float marginLeft;

    public StackPageTransformer(int marginLeft, int numberOfStacked, float currentPageScale, float topStackedScale) {
        mNumberOfStacked = numberOfStacked;
        mAlphaFactor = 1.0f / (mNumberOfStacked + 1);
        mZeroPositionScale = currentPageScale;
        mStackedScaleFactor = (currentPageScale - topStackedScale) / mNumberOfStacked;
        this.marginLeft = marginLeft;
    }

    @Override
    public void transformPage(View view, float position) {
        Log.d("TAG", "position: " + position);
        int screenWidth = view.getWidth();
        view.setPivotX(0f);
        view.setPivotY(view.getHeight() / 2f);

        if (position >= mNumberOfStacked + 1) {
            view.setAlpha(0f);
        } else if (position > 0) {
            float alpha = 1.0f - (position * mAlphaFactor);
             view.setAlpha(1f);
            setAlphaForChildren(alpha, view);
            float scale = mZeroPositionScale - (position * mStackedScaleFactor);
            float baseTranslation = -position * screenWidth;
          //  float shiftTranslation = screenWidth * scale *0.01f;
            view.setTranslationX(baseTranslation  + marginLeft);
            view.setScaleX(scale);
            view.setScaleY(scale);
        } else if (position <= 0) {
            view.setAlpha(1f);
            setAlphaForChildren(1f, view);
            view.setScaleX(mZeroPositionScale);
            view.setScaleY(mZeroPositionScale);
            view.setTranslationX(marginLeft);
        } else if (position <= -1f) {
         //   setAlphaForChildren(0f, view);
            view.setAlpha(0f);
        }
    }

    private void setAlphaForChildren(float alpha, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                childView.setAlpha(alpha);
            }
        }
    }
}

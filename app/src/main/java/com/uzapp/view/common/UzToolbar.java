package com.uzapp.view.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uzapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by viktoria on 1/27/17.
 */

public class UzToolbar extends Toolbar {
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.leftImageBtn) ImageButton leftImageBtn;
    @BindView(R.id.rightImageBtn) ImageButton rightImageBtn;
    @BindView(R.id.rightBtn) Button rightBtn;
    private Context context;

    public UzToolbar(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public UzToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public UzToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionBarHeight));
        }
        setContentInsetsAbsolute(0,0);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.uz_toolbar, this, true);
        ButterKnife.bind(this);
        initAttrs(attrs);
    }

    private void initAttrs(@Nullable AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UzToolbar, 0, 0);
        try {
            String title = ta.getString(R.styleable.UzToolbar_title);
            setTitle(title);
            int color = ta.getColor(R.styleable.UzToolbar_toolbarColor, 0);
            setColor(color);
            boolean showLeftImageBtn = ta.getBoolean(R.styleable.UzToolbar_showLeftImageBtn, false);
            setShowLeftImageBtn(showLeftImageBtn);
            if (showLeftImageBtn) {
                Drawable leftImageRes = ta.getDrawable(R.styleable.UzToolbar_leftImageRes);
                setLeftImageRes(leftImageRes);
            }
            boolean showRightImageBtn = ta.getBoolean(R.styleable.UzToolbar_showRightImageBtn, false);
            setShowRightImageBtn(showRightImageBtn);
            if (showRightImageBtn) {
                Drawable rightImageRes = ta.getDrawable(R.styleable.UzToolbar_rightImageRes);
                setRightImageRes(rightImageRes);
            }
            boolean showRightBtn = ta.getBoolean(R.styleable.UzToolbar_showRightBtn, false);
            setShowRightBtn(showRightBtn);
            if (showRightBtn) {
                String rightBtnText = ta.getString(R.styleable.UzToolbar_rightBtnText);
                setRightBtnText(rightBtnText);
            }
        } finally {
            ta.recycle();
        }
    }

    public void setTitle(String title) {
        if (title == null) return;
        toolbarTitle.setText(title);
    }

    public void setColor(int color) {
        setBackground(new ColorDrawable(color));
    }

    public void setLeftImageRes(Drawable leftImage) {
        leftImageBtn.setImageDrawable(leftImage);
    }

    public void setRightImageRes(Drawable rightImage) {
        rightImageBtn.setImageDrawable(rightImage);
    }

    public void setShowLeftImageBtn(boolean showLeftImageBtn) {
        leftImageBtn.setVisibility(showLeftImageBtn ? VISIBLE : GONE);
    }

    public void setShowRightImageBtn(boolean showRightImageBtn) {
        rightImageBtn.setVisibility(showRightImageBtn ? VISIBLE : GONE);
    }

    public void setShowRightBtn(boolean showRightBtn) {
        rightBtn.setVisibility(showRightBtn ? VISIBLE : GONE);
    }

    public void setRightBtnText(String rightBtnText) {
        if (rightBtnText == null) return;
        rightBtn.setText(rightBtnText);
    }
}

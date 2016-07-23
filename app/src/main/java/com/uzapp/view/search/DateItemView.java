package com.uzapp.view.search;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uzapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vika on 20.07.16.
 */
public class DateItemView extends RelativeLayout {
    private SimpleDateFormat dayOfMonthFormatter = new SimpleDateFormat("dd");
    private SimpleDateFormat dayOfWeekFormatter = new SimpleDateFormat("EE");

    @BindView(R.id.dayOfMonth) TextView dayOfMonth;
    @BindView(R.id.dayOfWeek) TextView dayOfWeek;
    @BindView(R.id.dateView) LinearLayout dateView;
    @BindColor(R.color.textColorHint) int unavailableTextColor;
    @BindColor(R.color.textColor) int monthColor;
    @BindColor(R.color.dayOfWeekTextColor) int dayOfWeekColor;
    private int viewWidth;

    public DateItemView(Context context) {
        super(context);
        init();
    }

    public DateItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.date_list_item, this, true);
        ButterKnife.bind(this);
    }

    public void bindDate(Date date) {
        dayOfMonth.setText(dayOfMonthFormatter.format(date));
        dayOfWeek.setText(dayOfWeekFormatter.format(date));
    }

    public void setBackgroundToday() {
        dateView.setBackgroundResource(R.drawable.today_background);
    }

    public void setSelectedDayBackground() {
        dateView.setBackgroundResource(R.drawable.selected_day_background);
    }

    public void setUnavailableTextColor() {
        dayOfMonth.setTextColor(unavailableTextColor);
        dayOfWeek.setTextColor(unavailableTextColor);
    }

    public void setAvailableTextColor() {
        dayOfMonth.setTextColor(monthColor);
        dayOfWeek.setTextColor(dayOfWeekColor);
    }

    public void clearBackground() {
        dateView.setBackgroundColor(Color.TRANSPARENT);
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = viewWidth;
        setLayoutParams(layoutParams);
        invalidate();
    }
}

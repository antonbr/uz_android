package com.uzapp.view.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
    @BindView(R.id.dateContainer) LinearLayout dateContainer;
    @BindView(R.id.dateArrowContainer) LinearLayout dateArrowContainer;
    @BindView(R.id.selectedDateArrow) TextView selectedDateArrow;
    @BindColor(R.color.textColorHint) int unavailableTextColor;
    @BindColor(R.color.textColor) int monthColor;
    @BindColor(R.color.accentColor) int selectedColor;
    @BindColor(R.color.textColorDark) int dayOfWeekColor;

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
        inflater.inflate(R.layout.date_list_item, this, true);
        ButterKnife.bind(this);
    }

    public void bindDate(Date date) {
        dayOfMonth.setText(dayOfMonthFormatter.format(date));
        dayOfWeek.setText(dayOfWeekFormatter.format(date));
        selectedDateArrow.setVisibility(INVISIBLE);
    }

    public void setBackgroundToday() {
        dateContainer.setBackgroundResource(R.drawable.today_background);
        dateArrowContainer.setBackgroundResource(0);
        selectedDateArrow.setVisibility(GONE);
    }

    public void clearBackground() {
        dateContainer.setBackgroundResource(0);
        dateArrowContainer.setBackgroundResource(0);
        selectedDateArrow.setVisibility(INVISIBLE);
    }

    public void setSelectedDayBackground(boolean isFirstDay) {
        dateContainer.setBackgroundResource(0);
        dateArrowContainer.setBackgroundResource(R.drawable.selected_day_background);
        selectedDateArrow.setVisibility(VISIBLE);
        selectedDateArrow.setText(getContext().getString(isFirstDay ? R.string.search_first_date_arrow :
                R.string.search_second_date_arrow));
    }

    public void setUnavailableTextColor() {
        dayOfMonth.setTextColor(unavailableTextColor);
        dayOfWeek.setTextColor(unavailableTextColor);
    }

    public void setAvailableTextColor() {
        dayOfMonth.setTextColor(monthColor);
        dayOfWeek.setTextColor(dayOfWeekColor);
    }

    public void setSelectedTextColor() {
        dayOfMonth.setTextColor(selectedColor);
        dayOfWeek.setTextColor(selectedColor);
    }
    public void setViewWidth(int viewWidth) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = viewWidth;
        setLayoutParams(layoutParams);
        invalidate();
    }
}

package com.uzapp.view.main.search.date;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by vika on 22.07.16.
 */
public class CalendarDaysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Date> dateList = new ArrayList<>();
    private Calendar calendar;
    private Date firstAvailableDate;
    private Date lastAvailableDate;
    private int textColor, textColorPast;
    private int selectedPosition = -1;
    private OnDateSelectedListener listener;
    private int pagePosition;

    public interface OnDateSelectedListener {
        void onDateSelected(int pagePosition, int dayPosition);
    }

    public CalendarDaysAdapter(List<Date> dateList, OnDateSelectedListener listener, int pagePosition, Context context) {
        this.listener = listener;
        this.pagePosition = pagePosition;
        this.dateList.clear();
        this.dateList.addAll(dateList);
        calendar = CommonUtils.getCalendar();
        firstAvailableDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, Constants.MAX_DAYS);
        lastAvailableDate = calendar.getTime();
        textColor = ContextCompat.getColor(context, R.color.textColor);
        textColorPast = ContextCompat.getColor(context, R.color.textColorHint);
    }

    public void setFirstAvailableDate(Date firstAvailableDate) {
        this.firstAvailableDate = firstAvailableDate;
    }

    public void updateSelection(int pagePosition, int dayPosition) {
        if (pagePosition == this.pagePosition) {
            int oldSelectedPosition = selectedPosition;
            selectedPosition = dayPosition;
            notifyItemChanged(selectedPosition);
            if (oldSelectedPosition != -1) {
                notifyItemChanged(oldSelectedPosition);
            }
        } else if (selectedPosition != -1) {
            int oldSelectedPosition = selectedPosition;
            selectedPosition = -1;
            notifyItemChanged(oldSelectedPosition);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.calendar_day_item, parent, false);
        return new DayHolder(itemView, parent.getWidth() / Constants.DAYS_IN_WEEK, parent.getHeight() / (Constants.MAX_WEEKS_IN_CALENDAR));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Date date = dateList.get(position);
        calendar.setTime(date);
        ((DayHolder) holder).day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        if (date.before(firstAvailableDate) || date.after(lastAvailableDate)) {
            ((DayHolder) holder).day.setTextColor(textColorPast);
            ((DayHolder) holder).day.setBackgroundColor(Color.TRANSPARENT);
        } else if (position == selectedPosition) {
            ((DayHolder) holder).day.setTextColor(Color.WHITE);
            ((DayHolder) holder).day.setBackgroundResource(R.drawable.blue_button_background);
        } else {
            ((DayHolder) holder).day.setTextColor(textColor);
            ((DayHolder) holder).day.setBackgroundResource(0);
            ((DayHolder) holder).day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDateSelected(pagePosition, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }


    public class DayHolder extends RecyclerView.ViewHolder {
        public TextView day;

        public DayHolder(View view, int width, int height) {
            super(view);
            day = (TextView) view.findViewById(R.id.day);

            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.height = height;
            itemView.setLayoutParams(lp);

            RelativeLayout.LayoutParams lpText = (RelativeLayout.LayoutParams) day.getLayoutParams();
            lpText.width = width - lpText.leftMargin - lpText.rightMargin;
            day.setLayoutParams(lpText);
        }
    }

}

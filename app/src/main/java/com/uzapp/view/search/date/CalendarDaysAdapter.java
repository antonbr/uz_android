package com.uzapp.view.search.date;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by vika on 22.07.16.
 */
public class CalendarDaysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SimpleDateFormat monthFormatter = new SimpleDateFormat("LLLL", Locale.getDefault());
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_DATE = 2;
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
        calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        firstAvailableDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, Constants.MAX_DAYS - 1);
        lastAvailableDate = calendar.getTime();
        textColor = ContextCompat.getColor(context, R.color.textColor);
        textColorPast = ContextCompat.getColor(context, R.color.textColorHint);
    }

    public void updateSelection(int pagePosition, int dayPosition) {
        if (pagePosition == this.pagePosition) {
            int oldSelectedPosition = selectedPosition;
            selectedPosition = dayPosition + 1; //add 1 because days are shifted with month name
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
        if (viewType == VIEW_TYPE_HEADER) {
            View itemView = layoutInflater.inflate(R.layout.calendar_month_header, parent, false);
            return new MonthHeaderHolder(itemView, parent.getHeight() / (Constants.MAX_WEEKS_IN_CALENDAR + 1));
        } else {
            View itemView = layoutInflater.inflate(R.layout.calendar_day_item, parent, false);
            return new DayHolder(itemView, parent.getWidth() / Constants.DAYS_IN_WEEK, parent.getHeight() / (Constants.MAX_WEEKS_IN_CALENDAR + 1));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MonthHeaderHolder) {
            String monthName = monthFormatter.format(dateList.get(0));
            ((MonthHeaderHolder) holder).month.setText(monthName);
        } else {
            final Date date = dateList.get(position - 1);
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
                        listener.onDateSelected(pagePosition, position - 1); //use position-1 because dates start from position==1
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (dateList.size() > 0) {
            return dateList.size() + 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_DATE;
    }

    public class DayHolder extends RecyclerView.ViewHolder {
        public TextView day;

        public DayHolder(View view, int width, int height) {
            super(view);
            day = (TextView) view.findViewById(R.id.day);

            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.height = height;
            itemView.setLayoutParams(lp);

            ViewGroup.LayoutParams lpText = day.getLayoutParams();
            lpText.width = width;
            day.setLayoutParams(lpText);
        }
    }

    public class MonthHeaderHolder extends RecyclerView.ViewHolder {
        public TextView month;

        public MonthHeaderHolder(View view, int height) {
            super(view);
            month = (TextView) view;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.height = height;
            itemView.setLayoutParams(lp);

        }
    }

}

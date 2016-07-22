package com.uzapp.view.search.date;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by vika on 22.07.16.
 */
public class CalendarDaysAdapter extends RecyclerView.Adapter<CalendarDaysAdapter.DayHolder> {
    private List<Date> dateList = new ArrayList<>();
    private Calendar calendar;

    public CalendarDaysAdapter(List<Date> dateList) {
        this.dateList.clear();
        this.dateList.addAll(dateList);
        calendar = Calendar.getInstance();
    }

    @Override
    public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_day_item, parent, false);
        return new DayHolder(itemView, parent.getWidth() / Constants.DAYS_IN_WEEK, parent.getHeight() / Constants.MAX_WEEKS_IN_CALENDAR);
    }

    @Override
    public void onBindViewHolder(DayHolder holder, int position) {
        Date date = dateList.get(position);
        calendar.setTime(date);
        holder.day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
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

            ViewGroup.LayoutParams lpText = day.getLayoutParams();
            lpText.width = width;
            day.setLayoutParams(lpText);
        }
    }
}

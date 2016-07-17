package com.uzapp.view.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vika on 17.07.16.
 */
public class DatePickerAdapter extends RecyclerView.Adapter<DatePickerAdapter.DateHolder> {
    private List<Date> dateList;
    private SimpleDateFormat dayOfMonthFormatter = new SimpleDateFormat("dd");
    private SimpleDateFormat dayOfWeekFormatter = new SimpleDateFormat("EE");

    public DatePickerAdapter(List<Date> dateList) {
        this.dateList = dateList;
    }

    @Override
    public DateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_list_item, parent, false);
        return new DateHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DateHolder holder, int position) {
        Date date = dateList.get(position);
        holder.dayOfMonth.setText(dayOfMonthFormatter.format(date));
        holder.dayOfWeek.setText(dayOfWeekFormatter.format(date));
//        if (position == 0) {
//            holder.itemView.setBackgroundResource(R.drawable.today_background);
//        } else {
//            holder.itemView.setBackgroundResource(0);
//        }
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public class DateHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dayOfMonth) TextView dayOfMonth;
        @BindView(R.id.dayOfWeek) TextView dayOfWeek;

        public DateHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

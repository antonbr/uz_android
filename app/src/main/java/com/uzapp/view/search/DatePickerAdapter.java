package com.uzapp.view.search;

import android.graphics.Color;
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
    private int selectedFirstPosition = -1, selectedSecondPosition = -1;
    private OnDateClickListener clickListener;

    public interface OnDateClickListener {
        void onDateItemClick(int position, Date date);
    }

    public DatePickerAdapter(List<Date> dateList, OnDateClickListener clickListener) {
        this.dateList = dateList;
        this.clickListener = clickListener;
    }

    public void setSelectedFirstPosition(int selectedFirstPosition) {
        int oldSelection = this.selectedFirstPosition;
        this.selectedFirstPosition = selectedFirstPosition;
        if (selectedFirstPosition >= 0 && selectedFirstPosition < getItemCount()) {
            notifyItemChanged(selectedFirstPosition);
            notifyItemChanged(oldSelection);
        }
    }

    public void setSelectedSecondPosition(int selectedSecondPosition) {
        int oldSelection = this.selectedSecondPosition;
        this.selectedSecondPosition = selectedSecondPosition;
        if (selectedSecondPosition >=0 && selectedSecondPosition < getItemCount()) {
            notifyItemChanged(selectedSecondPosition);
            notifyItemChanged(oldSelection);
        }
    }

    @Override
    public DateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_list_item, parent, false);
        return new DateHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DateHolder holder, final int position) {
        final Date date = dateList.get(position);
        holder.dayOfMonth.setText(dayOfMonthFormatter.format(date));
        holder.dayOfWeek.setText(dayOfWeekFormatter.format(date));
        if (position == selectedFirstPosition) {
            holder.itemView.setBackgroundResource(R.drawable.selected_day_background);
        } else if (position == selectedSecondPosition) {
            holder.itemView.setBackgroundResource(R.drawable.selected_day_background);
        } else if (position == 0 && position!=selectedFirstPosition && position!=selectedSecondPosition) {
            holder.itemView.setBackgroundResource(R.drawable.today_background);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onDateItemClick(position, date);
                }
            }
        });
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

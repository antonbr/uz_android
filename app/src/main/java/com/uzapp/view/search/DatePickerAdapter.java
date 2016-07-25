package com.uzapp.view.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

/**
 * Created by vika on 17.07.16.
 */
public class DatePickerAdapter extends RecyclerView.Adapter<DatePickerAdapter.DateHolder> {
    private List<Date> dateList;
    private int selectedFirstPosition = -1, selectedSecondPosition = -1;
    private OnDateClickListener clickListener;
    private int itemWidth;
    private boolean isSelectingSecondDate = false;

    public interface OnDateClickListener {
        void onDateItemClick(int position, Date date);
    }

    public DatePickerAdapter(List<Date> dateList, int itemWidth, OnDateClickListener clickListener) {
        this.dateList = dateList;
        this.itemWidth = itemWidth;
        this.clickListener = clickListener;
    }

    public void setSelectedFirstPosition(int selectedFirstPosition) {
        int oldSelection = this.selectedFirstPosition;
        this.selectedFirstPosition = selectedFirstPosition;
        notifyItemChanged(oldSelection);
        if (selectedFirstPosition >= 0 && selectedFirstPosition < getItemCount()) {
            notifyItemChanged(selectedFirstPosition);
        }
    }

    public void setSelectedSecondPosition(int selectedSecondPosition) {
        int oldSelection = this.selectedSecondPosition;
        this.selectedSecondPosition = selectedSecondPosition;
        notifyItemChanged(oldSelection);
        if (selectedSecondPosition >= 0 && selectedSecondPosition < getItemCount()) {
            notifyItemChanged(selectedSecondPosition);
        }
    }

    public void setSelectingSecondDate(boolean selectingSecondDate) {
        isSelectingSecondDate = selectingSecondDate;
        notifyDataSetChanged();
    }

    @Override
    public DateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DateItemView itemView = new DateItemView(parent.getContext());
        parent.addView(itemView);
        itemView.setViewWidth(itemWidth);
        return new DateHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DateHolder holder, final int position) {
        final Date date = dateList.get(position);
        holder.dateItemView.bindDate(date);
        if (position == selectedFirstPosition) {
            holder.dateItemView.setSelectedDayBackground(true);
        } else if (position == selectedSecondPosition) {
            holder.dateItemView.setSelectedDayBackground(false);
        } else if (position == 0 && position != selectedFirstPosition && position != selectedSecondPosition) {
            holder.dateItemView.setBackgroundToday();
        } else {
            holder.dateItemView.clearBackground();
        }
        if (position == selectedFirstPosition || position == selectedSecondPosition) {
            holder.dateItemView.setSelectedTextColor();
        } else if (position < selectedFirstPosition && isSelectingSecondDate) {
            holder.dateItemView.setUnavailableTextColor();
        } else {
            holder.dateItemView.setAvailableTextColor();
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
        DateItemView dateItemView;

        public DateHolder(View view) {
            super(view);
            dateItemView = (DateItemView) view;
        }
    }
}

package com.uzapp.view.main.search.date;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by vika on 23.07.16.
 */
public class MonthPagerAdapter extends PagerAdapter {
    private SimpleDateFormat monthFormatter = new SimpleDateFormat("LLLL", Locale.getDefault());
    private Context context;
    private List<List<Date>> dateListsForPages;
    private List<CalendarDaysAdapter> daysAdapters = new ArrayList<>();
    private Calendar calendar = CommonUtils.getCalendar();
    private CalendarDaysAdapter.OnDateSelectedListener listener;
    private Date minDate;

    public MonthPagerAdapter(Context context, List<List<Date>> dateListsForPages, Date minDate,
                             CalendarDaysAdapter.OnDateSelectedListener listener) {
        this.context = context;
        this.dateListsForPages = dateListsForPages;
        this.minDate = minDate;
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        List<Date> monthDays = dateListsForPages.get(position);
        if (monthDays.size() == 0) {
            return null;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        View monthView = layoutInflater.inflate(R.layout.calendar_month_view, container, false);
        RecyclerView monthGridView = (RecyclerView) monthView.findViewById(R.id.month);
        TextView monthNameView = (TextView) monthView.findViewById(R.id.monthName);

        GridLayoutManager layoutManager = new GridLayoutManager(context, Constants.DAYS_IN_WEEK);
        monthGridView.setLayoutManager(layoutManager);
        monthGridView.setHasFixedSize(true);
        final int daysOffset = getDaysOffset(monthDays.get(0));
        layoutManager.setSpanSizeLookup(new MonthSpanSizeLookup(daysOffset));

        CalendarDaysAdapter adapter = new CalendarDaysAdapter(monthDays, listener, position, context);
        adapter.setFirstAvailableDate(minDate);
        monthGridView.setAdapter(adapter);
        daysAdapters.add(adapter);

        monthNameView.setText(monthFormatter.format(monthDays.get(0)));
        alignMonthName(monthNameView, monthGridView, daysOffset);

        container.addView(monthView);
        return monthView;
    }

    private void alignMonthName(final TextView monthNameView, final RecyclerView monthGridView, final int daysOffset) {
        monthGridView.post(new Runnable() {
            @Override
            public void run() {
                int monthNameViewPadding, dividerPadding;
                int padding = (int) context.getResources().getDimension(R.dimen.medium_padding);
                float textWidth = monthNameView.getWidth();
                monthNameViewPadding = dividerPadding = monthGridView.getWidth() / Constants.DAYS_IN_WEEK * daysOffset;
                if (monthNameViewPadding + textWidth + padding >= monthGridView.getWidth()) {
                    monthNameViewPadding = (int) (monthGridView.getWidth() - textWidth - padding);
                    dividerPadding = monthNameViewPadding;
                } else if (monthNameViewPadding == 0) {
                    monthNameViewPadding = padding;
                }
                monthNameView.setPadding(monthNameViewPadding, 0, 0, 0);
                monthGridView.addItemDecoration(new CalendarItemDecorator(context, dividerPadding));
            }
        });
    }

    private int getDaysOffset(Date firstDate) {
        int daysOffset;
        calendar.setTime(firstDate);
        daysOffset = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        if (daysOffset == -1) {
            daysOffset = 6; //if week starts from monday and first day of month is sunday, than days offset would be -1
        }
        return daysOffset;
    }

    public void updateSelection(int pagePosition, int monthPosition) {
        for (CalendarDaysAdapter adapter : daysAdapters) {
            adapter.updateSelection(pagePosition, monthPosition);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        RecyclerView recyclerView = (RecyclerView) ((View) object).findViewById(R.id.month);
        daysAdapters.remove(recyclerView.getAdapter());
    }

    @Override
    public int getCount() {
        return dateListsForPages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private class MonthSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        int daysOffset;

        public MonthSpanSizeLookup(int daysOffset) {
            this.daysOffset = daysOffset;
        }

        @Override
        public int getSpanSize(int position) {
            if (position == 0) {
                return daysOffset + 1;
            }
            return 1;
        }
    }
}

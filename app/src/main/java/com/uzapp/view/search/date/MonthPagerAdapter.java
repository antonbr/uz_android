package com.uzapp.view.search.date;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.uzapp.util.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by vika on 23.07.16.
 */
public class MonthPagerAdapter extends PagerAdapter {
    private Context context;
    private List<List<Date>> dateListsForPages;
    private Calendar calendar = Calendar.getInstance();

    public MonthPagerAdapter(Context context, List<List<Date>> dateListsForPages) {
        this.context = context;
        this.dateListsForPages = dateListsForPages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        List<Date> monthDays = dateListsForPages.get(position);
        int daysOffset = 0;
        if (monthDays.size() > 0) {
            calendar.setTime(monthDays.get(0));
            daysOffset = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
            if (daysOffset == -1) {
                daysOffset = 6; //if week starts from monday and first day of month is sunday, than days offset would be -1
            }
        }

        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        container.addView(recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(context, Constants.DAYS_IN_WEEK);
        recyclerView.setLayoutManager(layoutManager);

        final int finalDaysOffset = daysOffset;
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return Constants.DAYS_IN_WEEK;
                } else if (position == 1) {
                    return finalDaysOffset + 1;
                }
                return 1;
            }
        });
        recyclerView.setHasFixedSize(true);
        CalendarDaysAdapter adapter = new CalendarDaysAdapter(monthDays, context);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new CalendarItemDecorator(context, daysOffset));
        return recyclerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return dateListsForPages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

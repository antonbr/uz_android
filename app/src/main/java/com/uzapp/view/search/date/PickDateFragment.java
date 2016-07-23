package com.uzapp.view.search.date;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vika on 19.07.16.
 */
public class PickDateFragment extends Fragment {
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.calendarMonthView) RecyclerView calendarMonthView;
    private Unbinder unbinder;
    private CalendarDaysAdapter adapter;
    private int daysOffset;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pick_date_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.calendar_when);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), Constants.DAYS_IN_WEEK);
        calendarMonthView.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return Constants.DAYS_IN_WEEK;
                } else if (position == 1) {
                    return daysOffset+1;
                }
                return 1;
            }
        });
        calendarMonthView.setHasFixedSize(true);
        adapter = new CalendarDaysAdapter(getDays(), getContext());
        calendarMonthView.setAdapter(adapter);
        calendarMonthView.addItemDecoration(new CalendarItemDecorator(getContext(), daysOffset));
        return view;
    }

    private ArrayList<Date> getDays() {

        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JULY);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        daysOffset = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        if (daysOffset == -1) {
            daysOffset = 6;
        }
        while (cells.size() < daysInMonth) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return cells;
    }

    @OnClick(R.id.closeBtn)
    void onCloseBtnClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.okBtn)
    void onOkBtnClick() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

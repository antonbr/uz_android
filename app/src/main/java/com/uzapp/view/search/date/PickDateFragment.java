package com.uzapp.view.search.date;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vika on 19.07.16.
 */
public class PickDateFragment extends Fragment {
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.monthPager) ViewPager monthPager;
    private Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pick_date_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.calendar_when);
        MonthPagerAdapter monthPagerAdapter = new MonthPagerAdapter(getContext(), getDaysByMonths());
        monthPager.setAdapter(monthPagerAdapter);
        return view;
    }

    private List<List<Date>> getDaysByMonths() {
        List<List<Date>> daysByMonths = new ArrayList<>();

        Calendar calendar = GregorianCalendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, Constants.MAX_DAYS);
        int monthsCount = CommonUtils.getMonthDifference(today, calendar.getTime());

        for (int i = 0; i <= monthsCount; i++) {
            calendar = GregorianCalendar.getInstance();
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            calendar.add(Calendar.MONTH, i);
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            ArrayList<Date> days = new ArrayList<>();
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            while (days.size() < daysInMonth) {
                days.add(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            daysByMonths.add(days);
        }
        return daysByMonths;
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

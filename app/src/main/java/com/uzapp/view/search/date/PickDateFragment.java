package com.uzapp.view.search.date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.uzapp.R;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by vika on 19.07.16.
 */
public class PickDateFragment extends Fragment implements CalendarDaysAdapter.OnDateSelectedListener {
    private SimpleDateFormat selectedDateFormat = new SimpleDateFormat("d MMMM yyyy, EE");
    private SimpleDateFormat nearestDateFormat = new SimpleDateFormat("d MMMM, EE");
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.dateTextField) EditText dateTextField;
    @BindView(R.id.tomorrowDate) TextView tomorrowDateView;
    @BindView(R.id.tomorrowBtn) ViewGroup tomorrowBtn;
    @BindView(R.id.dayAfterTomorrowBtn) ViewGroup dayAfterTomorrowBtn;
    @BindView(R.id.dayAfterTomorrowDate) TextView dayAfterTomorrowDateView;
    @BindView(R.id.todayBtn) ToggleButton todayBtn;
    @BindView(R.id.monthPager) VerticalViewPager monthPager;
    private Date today, tomorrow, dayAfterTomorrow, minDate, selectedDate;
    private Unbinder unbinder;
    private MonthPagerAdapter monthPagerAdapter;
    private List<List<Date>> allDaysByMonths;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pick_date_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.calendar_when);
        if (getArguments() != null && getArguments().containsKey("minDate")) {
            minDate = (Date) getArguments().getSerializable("minDate");
        }
        initNearestDates();
        allDaysByMonths = getDaysByMonths();
        monthPagerAdapter = new MonthPagerAdapter(getContext(), allDaysByMonths, minDate, this);
        monthPager.setOffscreenPageLimit(2);
        monthPager.setAdapter(monthPagerAdapter);
        return view;
    }

    public static PickDateFragment getInstance(Date date) {
        PickDateFragment fragment = new PickDateFragment();
        Bundle args = new Bundle();
        args.putSerializable("minDate", date);
        fragment.setArguments(args);
        return fragment;
    }

    private void initNearestDates() {
        Calendar calendar = CommonUtils.getCalendar();
        today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        tomorrow = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dayAfterTomorrow = calendar.getTime();
        tomorrowDateView.setText(nearestDateFormat.format(tomorrow));
        dayAfterTomorrowDateView.setText(nearestDateFormat.format(dayAfterTomorrow));
        todayBtn.setEnabled(today.after(minDate) || today.equals(minDate));
        tomorrowBtn.setEnabled(tomorrow.after(minDate) || tomorrow.equals(minDate));
        dayAfterTomorrowBtn.setEnabled(dayAfterTomorrow.after(minDate) || dayAfterTomorrow.equals(minDate));
    }

    private List<List<Date>> getDaysByMonths() {
        List<List<Date>> daysByMonths = new ArrayList<>();

        Calendar calendar = GregorianCalendar.getInstance();

        int maxDays = Constants.MAX_DAYS;
        if (!minDate.equals(today)) {
            calendar.setTime(minDate);
            maxDays -= CommonUtils.getDaysDifference(minDate, today);
        }
        calendar.add(Calendar.DAY_OF_MONTH, maxDays);
        int monthsCount = CommonUtils.getMonthDifference(minDate, calendar.getTime());

        for (int i = 0; i <= monthsCount; i++) {
            calendar =CommonUtils.getCalendar();
            calendar.setTime(minDate);

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
        if (selectedDate != null) {
            Fragment targetFragment = getTargetFragment();
            if (targetFragment != null) {
                Intent i = new Intent();
                i.putExtra("date", selectedDate);
                targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
            }
            getActivity().onBackPressed();
        } else {
            //todo show message
        }
    }

    @OnCheckedChanged(R.id.todayBtn)
    void onTodayBtnClicked(boolean checked) {
        if (checked) {
            selectedDate = today;
            selectDateInCalendarView(selectedDate);
            showSelectedDateText();
        }
    }

    @OnClick(R.id.tomorrowBtn)
    void onTomorrowBtnClicked() {
        selectedDate = tomorrow;
        selectDateInCalendarView(selectedDate);
        showSelectedDateText();
    }

    @OnClick(R.id.dayAfterTomorrowBtn)
    void onDayAfterTomorrowBtnClicked() {
        selectedDate = dayAfterTomorrow;
        selectDateInCalendarView(selectedDate);
        showSelectedDateText();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void selectDateInCalendarView(Date selectedDate) {
        for (int i = 0; i < allDaysByMonths.size(); i++) {
            List<Date> month = allDaysByMonths.get(i);
            for (int j = 0; j < month.size(); j++) {
                if (month.get(j).equals(selectedDate)) {
                    monthPagerAdapter.updateSelection(i, j);
                    monthPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onDateSelected(int pagePosition, int dayPosition) {
        monthPagerAdapter.updateSelection(pagePosition, dayPosition);
        selectedDate = allDaysByMonths.get(pagePosition).get(dayPosition);
        showSelectedDateText();
        if (todayBtn.isChecked() && !selectedDate.equals(today)) {
            todayBtn.setChecked(false);
        }
    }

    private void showSelectedDateText() {
        if (selectedDate != null) {
            dateTextField.setText(selectedDateFormat.format(selectedDate));
        }
    }


}

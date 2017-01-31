package com.uzapp.view.main.search.date;

import android.os.Bundle;
import android.util.Log;

import com.uzapp.util.CommonUtils;
import com.uzapp.util.Constants;
import com.uzapp.view.BaseView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by viktoria on 1/31/17.
 */

public class PickDatePresenter {
    private SimpleDateFormat selectedDateFormat = new SimpleDateFormat("d MMMM yyyy, EE");
    private SimpleDateFormat nearestDateFormat = new SimpleDateFormat("d MMMM, EE");
    private SimpleDateFormat availableDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Date today, tomorrow, dayAfterTomorrow, minDate, selectedDate;
    private List<List<Date>> allDaysByMonths;
    private List<Date> availableDates = new ArrayList<>();
    private PickDateView view;

    public PickDatePresenter(PickDateView view, Bundle args) {
        this.view = view;
        if (args != null && args.containsKey("minDate")) {
            minDate = (Date) args.getSerializable("minDate");
        } else if (args != null && args.containsKey("availableDates")) {
            view.showBottomBar(false);
            availableDates = getAvailableDatesFromBundle(args);
            if (availableDates != null && availableDates.size() > 0) {
                minDate = availableDates.get(0);
            }
        }
        initNearestDates();
        allDaysByMonths = getDaysByMonths();
        view.initView(allDaysByMonths, availableDates);
        view.setOkBtnEnabled(selectedDate != null);
    }

    private List<Date> getAvailableDatesFromBundle(Bundle bundle) {
        List<String> stringDatesList = bundle.getStringArrayList("availableDates");
        List<Date> dateList = new ArrayList<>();
        if (stringDatesList == null) return dateList;
        for (String s : stringDatesList) {
            try {
                Date date = availableDateFormat.parse(s);
                dateList.add(date);
            } catch (ParseException e) {
                Log.e(PickDateFragment.class.getName(), e.getMessage());
            }
        }
        return dateList;
    }

    void onOkBtnClick() {
        if (selectedDate != null) {
            view.returnResult(selectedDate);
        }
    }

    void onTomorrowBtnClicked() {
        selectedDate = tomorrow;
        view.setOkBtnEnabled(true);
        selectDateInCalendarView(selectedDate);
        if (selectedDate != null) {
            view.showSelectedDateText(selectedDateFormat.format(selectedDate));
        }
    }

    void onDayAfterTomorrowBtnClicked() {
        selectedDate = dayAfterTomorrow;
        view.setOkBtnEnabled(true);
        selectDateInCalendarView(selectedDate);
        if (selectedDate != null) {
            view.showSelectedDateText(selectedDateFormat.format(selectedDate));
        }
    }

    void onDateSelected(int pagePosition, int dayPosition) {
        view.selectDateInCalendarView(pagePosition, dayPosition);
        selectedDate = allDaysByMonths.get(pagePosition).get(dayPosition);
        view.setOkBtnEnabled(true);
        if (selectedDate != null) {
            view.showSelectedDateText(selectedDateFormat.format(selectedDate));
        }
        if (view.isTodayBtnChecked() && !selectedDate.equals(today)) {
            view.setTodayBtnCheckedState(false);
        }
    }

    void onTodayBtnClicked(boolean checked) {
        if (checked) {
            selectedDate = today;
            view.setOkBtnEnabled(true);
            selectDateInCalendarView(selectedDate);
            if (selectedDate != null) {
                view.showSelectedDateText(selectedDateFormat.format(selectedDate));
            }
        }
    }

    private void selectDateInCalendarView(Date selectedDate) {
        for (int i = 0; i < allDaysByMonths.size(); i++) {
            List<Date> month = allDaysByMonths.get(i);
            for (int j = 0; j < month.size(); j++) {
                if (month.get(j).equals(selectedDate)) {
                    view.selectDateInCalendarView(i, j);
                    break;
                }
            }
        }
    }

    private void initNearestDates() {
        Calendar calendar = CommonUtils.getCalendar();
        today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        tomorrow = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dayAfterTomorrow = calendar.getTime();
        view.setTomorrowBtnText(nearestDateFormat.format(tomorrow));
        view.setDayAfterTomorrowBtnText(nearestDateFormat.format(dayAfterTomorrow));
        view.setTodayBtnEnabled(today.after(minDate) || today.equals(minDate));
        view.setTomorrowBtnEnabled(tomorrow.after(minDate) || tomorrow.equals(minDate));
        view.setDayAfterTomorrowBtnEnabled(dayAfterTomorrow.after(minDate) || dayAfterTomorrow.equals(minDate));
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
        Date lastAvailableDate = calendar.getTime();
        int monthsCount = CommonUtils.getMonthDifference(minDate, calendar.getTime());
        boolean needPopulateAvailableDays = availableDates.isEmpty();
        for (int i = 0; i <= monthsCount; i++) {
            calendar = CommonUtils.getCalendar();
            calendar.setTime(minDate);
            calendar.add(Calendar.MONTH, i);
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            ArrayList<Date> days = new ArrayList<>();
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            while (days.size() < daysInMonth) {
                days.add(calendar.getTime());
                if (needPopulateAvailableDays && !calendar.getTime().before(minDate) && !calendar.getTime().after(lastAvailableDate)) {
                    availableDates.add(calendar.getTime());
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            daysByMonths.add(days);
        }
        return daysByMonths;
    }

    interface PickDateView extends BaseView {
        void showBottomBar(boolean isVisible);

        void setTomorrowBtnEnabled(boolean isEnabled);

        void setDayAfterTomorrowBtnEnabled(boolean isEnabled);

        void setTomorrowBtnText(String text);

        void setDayAfterTomorrowBtnText(String text);

        void setTodayBtnEnabled(boolean isEnabled);

        void initView(List<List<Date>> allDaysByMonths, List<Date> availableDates);

        void returnResult(Date selectedDate);

        void selectDateInCalendarView(int pagePosition, int monthPosition);

        void showSelectedDateText(String text);

        boolean isTodayBtnChecked();

        void setTodayBtnCheckedState(boolean isChecked);

        void setOkBtnEnabled(boolean isEnabled);
    }
}

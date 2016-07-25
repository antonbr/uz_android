package com.uzapp.util;

import com.uzapp.pojo.Languages;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by vika on 17.07.16.
 */
public class CommonUtils {
    public static String getLanguage() {
        String lang = Locale.getDefault().getLanguage();
        boolean isLanguageSupported = false;
        for (Languages language : Languages.values()) {
            if (language.name().equalsIgnoreCase(lang)) {
                isLanguageSupported = true;
                break;
            }
        }
        if (!isLanguageSupported) {
            lang = Languages.UA.name().toLowerCase();
        }
        return lang;
    }

    public static final int getDaysDifference(Date firstDate, Date secondDate) {
        long diff = firstDate.getTime() - secondDate.getTime();
        int days = (int) (diff / (Constants.HOURS_IN_DAY * Constants.MINUTES_IN_HOUR * Constants.SECONDS_IN_MINUTE
                * Constants.MILLISECONDS_IN_SECOND));
        return days;
    }

    public static final int getMonthDifference(Date firstDate, Date secondDate) {
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(firstDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(secondDate);
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        return diffMonth;
    }

    public static Calendar getCalendar() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}

package com.uzapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.pojo.Languages;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public static long getDaysDifference(Date firstDate, Date secondDate) {
        long diff = firstDate.getTime() - secondDate.getTime();
//        int days = (int) (diff / (Constants.HOURS_IN_DAY * Constants.MINUTES_IN_HOUR * Constants.SECONDS_IN_MINUTE
//                * Constants.MILLISECONDS_IN_SECOND));
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        return days;
    }

    public static int getMonthDifference(Date firstDate, Date secondDate) {
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(firstDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(secondDate);
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        return diffMonth;
    }

    public static long getMinutesDifference(Date firstDate, Date secondDate) {
        long diff = secondDate.getTime() - firstDate.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }

    public static Calendar getCalendar() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static boolean isOdd(int value) {
        return (value & 0x01) != 0;
    }

    @SuppressLint("NewApi")
    public static Drawable changeBackgroundPlace(Context context, Button button) {
        return (isSelectedPlace(context, button)) ? context.getDrawable(R.drawable.border_button_place) :
                context.getDrawable(R.drawable.border_button_place_selected);
    }

    @SuppressLint("NewApi")
    public static int changeTextColorPlace(Context context, Button button, int color) {
        return (isSelectedPlace(context, button)) ?
                context.getColor(android.R.color.white) : context.getColor(color);
    }

    @SuppressLint("NewApi")
    public static boolean isSelectedPlace(Context context, Button button) {
        return (button.getBackground().getConstantState().equals(context.getDrawable(
                R.drawable.border_button_place_selected).getConstantState()));
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= Constants.MIN_PASSWORD_LENGTH;
    }

    public static void showMessage(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(Color.LTGRAY);
        snackbar.show();
    }
    public static void showMessage(View view, int textRes){
        showMessage(view, view.getContext().getString(textRes));
    }
}

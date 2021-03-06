package com.uzapp.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * @param value
     * @return Is odd place
     */
    public static boolean isOdd(int value) {
        return (value & 0x01) != 0;
    }

    public static Drawable changeBackgroundPlace(Context context, Button button) {
        return (isSelectedPlace(button, ContextCompat.getDrawable(context, R.drawable.border_button_place_selected))) ?
                ContextCompat.getDrawable(context, R.drawable.border_button_place) :
                ContextCompat.getDrawable(context, R.drawable.border_button_place_selected);
    }

    /**
     * @param context
     * @param button
     * @param color
     * @return color
     * <p>
     * Change text color
     */
    public static int changeTextColorPlace(Context context, Button button, int color) {
        return (isSelectedPlace(button, ContextCompat.getDrawable(context, R.drawable.border_button_place_selected))) ?
                ContextCompat.getColor(context, android.R.color.white) : ContextCompat.getColor(context, color);
    }

    /**
     * @param button
     * @return selected place
     * <p>
     * Is selected place
     */
    public static boolean isSelectedPlace(Button button, Drawable drawable) {
        return (button.getBackground().getConstantState().equals(drawable.getConstantState()));
    }

    public static boolean isFirstNameValid(String firstName) {
        return firstName != null && (firstName.length() == 0 || firstName.length() >= Constants.FIRST_NAME_MIN_LENGTH);
    }

    public static boolean isLastNameValid(String lastName) {
        return lastName != null && (lastName.length() == 0 || lastName.length() >= Constants.LAST_NAME_MIN_LENGTH);
    }

    public static boolean isMiddleNameValid(String middleName) {
        return middleName != null && (middleName.length() == 0 || middleName.length() >= Constants.MIDDLE_NAME_MIN_LENGTH);
    }

    public static boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password != null && password.length() >= Constants.MIN_PASSWORD_LENGTH;
    }

    public static void showSnackbar(View view, String text) {
        if (!TextUtils.isEmpty(text)) {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            int snackbarTextId = android.support.design.R.id.snackbar_text;
            TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
            textView.setTextColor(Color.LTGRAY);
            snackbar.show();
        }
    }

    public static void showSnackbar(View view, int textRes) {
        showSnackbar(view, view.getContext().getString(textRes));
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    public static String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    public static char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    public static boolean isVisa(String str) {
        String visa = "^4[0-9]{6,}$";
        return str.matches(visa);
    }

    public static boolean isMasterCard(String str) {
        String masterCard = "^5[1-5][0-9]{5,}$";
        return str.matches(masterCard);
    }

    public static boolean isFullName(String str) {
        String expression = " ^[a-zA-Z0-9._-]{3,}$";
        return str.matches(expression);
    }

    public static int convertDpFromPx(Context context, int pixel) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel,
                resources.getDisplayMetrics());
    }


    public static boolean isStudentIdValid(String studentId) {
        if (studentId == null) return false;
        if (studentId.length() == 0) return true;
        Pattern p = Pattern.compile(Constants.STUDENT_ID_REGEX_PATTERN);
        Matcher m = p.matcher(studentId.replaceAll("\\s+", ""));
        return m.matches();
    }

}

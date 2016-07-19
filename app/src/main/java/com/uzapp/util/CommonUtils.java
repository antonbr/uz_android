package com.uzapp.util;

import com.uzapp.pojo.Languages;

import java.util.Date;
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
}

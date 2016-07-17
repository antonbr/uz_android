package com.uzapp.util;

import com.uzapp.pojo.Languages;

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
        if(!isLanguageSupported){
            lang = Languages.UA.name().toLowerCase();
        }
        return lang;
    }
}

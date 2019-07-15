package com.sklk.ticket.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageUtil {
    public static void applyLanguage(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = SupportLanguageUtil.getSupportLanguage(newLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
            String s = locale.toLanguageTag();
            if (s.contains("en")) {
                configuration.setLocale(Locale.UK);
            } else {
                configuration.setLocale(Locale.SIMPLIFIED_CHINESE);
            }

        } else {
            // updateConfiguration
            configuration.locale = locale;
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
        }
    }

    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context, language);
        } else {
            applyLanguage(context, language);
            return context;
        }
    }

    public static String getCurrentLanguage(Context context) {
        String languageStr = SPUtil.getString(context, "language");
        if (!TextUtils.isEmpty(languageStr)) {
            return languageStr;
        }
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("en")) {
            return "en";
        } else {
            return "zh";
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, String language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale;
        locale = SupportLanguageUtil.getSupportLanguage(language);
        String s = locale.toLanguageTag();
        if (s.contains("en")) {
            configuration.setLocale(Locale.UK);
        } else {
            configuration.setLocale(Locale.SIMPLIFIED_CHINESE);
        }
        return context.createConfigurationContext(configuration);
    }
}

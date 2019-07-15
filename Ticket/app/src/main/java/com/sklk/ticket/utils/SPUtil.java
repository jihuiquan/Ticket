package com.sklk.ticket.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * author：${JHuiQ} on 2018/4/14 14:57
 */
public class SPUtil {

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    public static final String STRING_UUID = "string_uuid";

    /**
     * 账号
     */
    public static final String STRING_ACCOUNT = "string_account";
    public static final String STRING_COOKIES = "string_cookies";
    public static final String COOKIE_DOMAIN = "domain_cookies";
    public static final String COOKIE_EXPIRESAT = "expiresat_cookies";
    public static final String COOKIE_NAME = "name_cookies";
    public static final String COOKIE_PATH = "path_cookies";
    public static final String COOKIE_VALUE = "value_cookies";
    public static final String PAY_INSTALLATIONID = "installationID";
    public static final String INT_ISNEW = "is_new";

    public static void putInt(Context context, String key, int valueInt) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, valueInt);
        editor.apply();
    }

    public static void putLong(Context context, String key, long valueLong) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, valueLong);
        editor.commit();
    }

    public static void putFloat(Context context, String key, float valueFloat) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, valueFloat);
        editor.apply();
    }

    public static void putBoolean(Context context, String key, boolean valueBoolean) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, valueBoolean);
        editor.commit();
    }

    public static void putString(Context context, String key, String valueString) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, valueString);
        editor.commit();
    }

    public static int getInt(Context context, String key, int defaultInt) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defaultInt);
    }

    public static int getInt(Context context, String key) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

    public static long getLong(Context context, String key, long defaultLong) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getLong(key, defaultLong);
    }

    public static float getFloat(Context context, String key, float defaultFloat) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultFloat);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultBoolean) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultBoolean);
    }

    public static String getString(Context context, String key) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }


    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }
}

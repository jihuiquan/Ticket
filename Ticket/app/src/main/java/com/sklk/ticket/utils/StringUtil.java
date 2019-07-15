package com.sklk.ticket.utils;

import android.util.Base64;

import com.google.gson.JsonParser;

/**
 * @package: com.ssic.yytc.utils
 * @author：JHQ
 * @date： 2018/4/28  13:45
 */
public class StringUtil {

    //判断是否是正确的json字符串
    public static boolean assertJson(String json) {

        if (null == json || "".equals(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //判断字符串是否为null和""
    public static boolean isEmptyWithString(String str) {
        if (null == str || "".equals(str)) {
            return true;
        }
        return false;
    }

    public static String stringToBase64(String string) {
        return Base64.encodeToString(string.getBytes(), Base64.NO_WRAP);
    }

    public static String byteToBase64(byte[] bytes) {
        //java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return Base64.encodeToString(bytes, Base64.NO_PADDING| Base64.URL_SAFE| Base64.NO_WRAP);
    }
}

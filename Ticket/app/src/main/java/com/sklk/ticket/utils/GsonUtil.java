package com.sklk.ticket.utils;

import com.google.gson.Gson;

/**
 * @package: com.ssic.yytc.utils
 * @author：JHQ
 * @date： 2018/5/3  11:07
 */
public class GsonUtil {

    public static String toJson(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

}

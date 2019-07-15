package com.sklk.ticket.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @package: com.ssic.yytc.utils
 * @author：JHQ
 * @date： 2018/6/6  10:40
 */
public class DateUtil {

    public static String dateToStr(String typeStr, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(typeStr);
        return formatter.format(date);
    }
}

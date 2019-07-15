package com.sklk.ticket.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @package: com.ssic.yytc.utils
 * @author：JHQ
 * @date： 2018/9/25  16:36
 */
public class PhoneUtil {

    public static void call(Context context, String num) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

package com.sklk.ticket.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * @package: com.ssic.yytc.utils
 * @author：JHQ
 * @date： 2018/5/4  16:42
 */
public class PermissionUtil {

    //第一次进入app申请权限（可申请多个）
    public static void requestPermissions(Activity activity, String[] strings) {
        ActivityCompat.requestPermissions(activity, strings, 0);
    }

    //获取单一权限
    public static boolean getSinglePermission(Context context, String permissionStr, String name) {
        if (hasPermission(permissionStr, context)) {
            if (context instanceof Activity) {
                PermissionUtil.requestPermissions((Activity) context, new String[]{
                        permissionStr
                });
            }
            return true;
        }
        return false;
    }

    //判断权限是否打开
    public static boolean hasPermission(String permissionName, Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
}

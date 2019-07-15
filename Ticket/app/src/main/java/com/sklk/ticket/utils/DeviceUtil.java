package com.sklk.ticket.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.telephony.TelephonyManager;

import com.mob.MobSDK;
import com.sklk.ticket.Constant;
import com.sklk.ticket.R;


/**
 * author：${JHuiQ} on 2018/4/14 14:30
 */
public class DeviceUtil {

    /**
     * 获取手机IMSI号，必须先有READ_PHONE_STATE权限才可调用
     * （国际移动用户识别码)：是区别移动用户的标志，储存在SIM卡中，
     * 可用于区别移动用户的有效信息。其总长度不超过15位
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imsi = mTelephonyMgr.getSubscriberId();
        return imsi;
    }

    public static String getVersionStr(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String versionName = null;
        try {
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getDeviceInfo(Context context) {
        String imsi = getIMSI(context);
        if (Constant.NO_PERMISSION.equals(imsi)) {
            return Constant.NO_PERMISSION;
        }
        String deviceJson = "{\"deviceType\":\"Mobile\",\"osType\":\"android\",\"agentType\":\"Native\",\"hardwareUid\":{\"mobile\":{\"IMSI\":\"" + imsi + "\"}}}";
        return deviceJson;
    }

    /**
     * Return the status bar's height.
     * 获取状态栏高度
     *
     * @return the status bar's height
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    @SuppressLint("WrongConstant")
    public static boolean hasInstallationWechat(Context context) {
        PackageInfo pi;
        try {
            pi = MobSDK.getContext().getPackageManager().getPackageInfo(
                    "com.tencent.mm", PackageManager.GET_RESOLVED_FILTER);
            return true;
        } catch (Throwable t) {
            ToastCommon.toast(context, R.string.wechat_uninstalled);
        }
        return false;
    }
}

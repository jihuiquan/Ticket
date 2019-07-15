package com.sklk.ticket.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sklk.ticket.base.MyApplication;

import java.net.HttpURLConnection;
import java.net.URL;



/**
 * @package: com.ssic.yytc.utils
 * @author：JHQ
 * @date： 2018/7/9  9:58
 */
public class NetWorkUtil {

    public static boolean hasNetWork() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager)
                MyApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean alertImageUri(final String URLName) {
        boolean URL_exists = true;
        try {
//设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
            HttpURLConnection.setFollowRedirects(false);
//到 URL 所引用的远程对象的连接
            HttpURLConnection con = (HttpURLConnection) new URL(URLName)
                    .openConnection();
            /* 设置 URL 请求的方法， GET POST HEAD OPTIONS PUT DELETE TRACE 以上方法之一是合法的，具体取决于协议的限制。*/
            con.setRequestMethod("HEAD");
//从 HTTP 响应消息获取状态码
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                URL_exists = true;
            } else {
                URL_exists = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return URL_exists;
    }
}

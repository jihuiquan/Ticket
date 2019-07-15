package com.sklk.ticket.utils;

import android.app.Activity;
import android.content.Intent;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.sklk.ticket.R;
import com.sklk.ticket.base.MyApplication;
import com.sklk.ticket.base.mvp.MVPBaseActivity;
import com.sklk.ticket.listener.OnClickCommonDialogListener;
import com.sklk.ticket.mui.SingleDialog;
import com.sklk.ticket.network.HttpResponseCode;
import com.sklk.ticket.network.RequestInterface;


/**
 * @package: cn.ssic.tianfangcatering.utils
 * @author：JHQ
 * @date： 2018/11/20  10:20
 */
public class StatusCodeUtil {

    public static void alertStatusCode(RefreshLayout refreshLayout, Activity activity, String methodName, int code) {
        if (activity instanceof MVPBaseActivity) {
            MVPBaseActivity baseActivity = (MVPBaseActivity) activity;
            baseActivity.onRefreshAndLoadMoreFailure(refreshLayout);
        }
        if (code == HttpResponseCode.STATUS_TOKEN) {
            logout(activity);
            return;
        }
    }

    private static void logout(final Activity activity) {
        SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_DOMAIN);
        SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_EXPIRESAT);
        SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_NAME);
        SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_PATH);
        SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_VALUE);
        SPUtil.remove(MyApplication.getApplication(), SPUtil.STRING_COOKIES);
        SingleDialog singleDialog = new SingleDialog(activity, activity.getString(R.string.login_invalid), activity.getString(R.string.please_login_again));
        singleDialog.setOnClickListenerWithCancelAndConfirm(new OnClickCommonDialogListener() {
            @Override
            public void onConfirmListener() {
//                Intent intent = new Intent(activity, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                activity.startActivity(intent);
            }
        });
        singleDialog.show();
    }
}

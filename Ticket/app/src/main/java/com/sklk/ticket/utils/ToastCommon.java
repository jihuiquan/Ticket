package com.sklk.ticket.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sklk.ticket.R;


/**
 * @Author: jHuiQ
 * @Date:on 2018/04/11.
 * @E-mail:1395320136@qq.com
 */

public class ToastCommon {

    private static Toast sToast;
    private static Toast sToastSuccess;
    private static Toast sToastLongSuccess;

    public static void toast(Context context, String content) {
        if (null == sToast) {
            //创建一个toast
            sToast = new Toast(context);
            //设置toast显示时间，整数值
            sToast.setDuration(Toast.LENGTH_SHORT);
            //toast的显示位置，这里居中显示
            sToast.setGravity(Gravity.CENTER, 0, 0);
            //加载布局文件
            View view = View.inflate(context, R.layout.toast, null);
            // 得到textview
            TextView textView = (TextView) view.findViewById(R.id.toast_text);
            //设置文本类荣，就是你想给用户看的提示数据
            textView.setText(content);
            //設置其显示的view
            sToast.setView(view);
        } else {
            View sToastView = sToast.getView();
            TextView toastTv = (TextView) sToastView.findViewById(R.id.toast_text);
            toastTv.setText(content);
        }
        sToast.show();
    }

    public static void toast(Context context, int content) {
        if (null == sToast) {
            //创建一个toast
            sToast = new Toast(context);
            //设置toast显示时间，整数值
            sToast.setDuration(Toast.LENGTH_SHORT);
            //toast的显示位置，这里居中显示
            sToast.setGravity(Gravity.CENTER, 0, 0);
            //加载布局文件
            View view = View.inflate(context, R.layout.toast, null);
            // 得到textview
            TextView textView = (TextView) view.findViewById(R.id.toast_text);
            //设置文本类荣，就是你想给用户看的提示数据
            textView.setText(context.getResources().getString(content));
            //設置其显示的view
            sToast.setView(view);
        } else {
            View sToastView = sToast.getView();
            TextView toastTv = (TextView) sToastView.findViewById(R.id.toast_text);
            toastTv.setText(context.getResources().getString(content));
        }
        sToast.show();
    }

    public static void toastSuccess(Context context, String content) {
        if (null == sToastSuccess) {
            //创建一个toast
            sToastSuccess = new Toast(context);
            //设置toast显示时间，整数值
            sToastSuccess.setDuration(Toast.LENGTH_SHORT);
            //toast的显示位置，这里居中显示
            sToastSuccess.setGravity(Gravity.CENTER, 0, 0);
            //加载布局文件
            View view = View.inflate(context, R.layout.toast, null);
            ImageView toaIv = (ImageView) view.findViewById(R.id.toast_iv);
            toaIv.setBackgroundResource(R.mipmap.toast_person_success);
            // 得到textview
            TextView textView = (TextView) view.findViewById(R.id.toast_text);
            textView.setText(content);     //设置文本类荣，就是你想给用户看的提示数据
            sToastSuccess.setView(view);     //設置其显示的view,
        } else {
            View sToastSuccessView = sToastSuccess.getView();
            TextView successTv = (TextView) sToastSuccessView.findViewById(R.id.toast_text);
            successTv.setText(content);
        }
        sToastSuccess.show();             //显示toast
    }

    public static void toastSuccessLong(Context context, String content) {
        if (null == sToastLongSuccess) {
            //创建一个toast
            sToastLongSuccess = new Toast(context);
            //设置toast显示时间，整数值
            sToastLongSuccess.setDuration(Toast.LENGTH_LONG);
            //toast的显示位置，这里居中显示
            sToastLongSuccess.setGravity(Gravity.CENTER, 0, 0);
            //加载布局文件
            View view = View.inflate(context, R.layout.toast, null);
            ImageView toaIv = (ImageView) view.findViewById(R.id.toast_iv);
            toaIv.setBackgroundResource(R.mipmap.toast_person_success);
            // 得到textview
            TextView textView = (TextView) view.findViewById(R.id.toast_text);
            textView.setText(content);     //设置文本类荣，就是你想给用户看的提示数据
            sToastLongSuccess.setView(view);     //設置其显示的view,
        } else {
            View sToastSuccessView = sToastLongSuccess.getView();
            TextView successTv = (TextView) sToastSuccessView.findViewById(R.id.toast_text);
            successTv.setText(content);
        }
        sToastLongSuccess.show();             //显示toast
    }

    public static void toastSuccess(Context context, int content) {
        if (null == sToastSuccess) {
            //创建一个toast
            sToastSuccess = new Toast(context);
            //设置toast显示时间，整数值
            sToastSuccess.setDuration(Toast.LENGTH_SHORT);
            //toast的显示位置，这里居中显示
            sToastSuccess.setGravity(Gravity.CENTER, 0, 0);
            //加载布局文件
            View view = View.inflate(context, R.layout.toast, null);
            ImageView toaIv = (ImageView) view.findViewById(R.id.toast_iv);
            toaIv.setBackgroundResource(R.mipmap.toast_person_success);
            // 得到textview
            TextView textView = (TextView) view.findViewById(R.id.toast_text);
            textView.setText(content+"");     //设置文本类荣，就是你想给用户看的提示数据
            sToastSuccess.setView(view);     //設置其显示的view,
        } else {
            View sToastSuccessView = sToastSuccess.getView();
            TextView successTv = (TextView) sToastSuccessView.findViewById(R.id.toast_text);
            successTv.setText(content+"");
        }
        sToastSuccess.show();             //显示toast
    }

    public static void nativityToast(Context context, int msg) {
        Toast.makeText(context, context.getResources().getString(msg), Toast.LENGTH_SHORT).show();
    }

    public static void nativityToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

package com.sklk.ticket.utils;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

/**
 * @package: com.ssic.yytc.utils
 * @author：JHQ
 * @date： 2018/6/29  16:45
 */
public class ToolbarUtil {
    //设置toolbar marginTop （主要用于沉浸页面）
    public static void setToolbarMarginTop(Context context, Toolbar toolbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams params = toolbar.getLayoutParams();
            ViewGroup.MarginLayoutParams marginParams = null;
            //获取view的margin设置参数
            if (params instanceof ViewGroup.MarginLayoutParams) {
                marginParams = (ViewGroup.MarginLayoutParams) params;
            } else {
                //不存在时创建一个新的参数
                //基于View本身原有的布局参数对象
                marginParams = new ViewGroup.MarginLayoutParams(params);
            }
            marginParams.topMargin = DeviceUtil.getStatusBarHeight(context);
            toolbar.setLayoutParams(marginParams);
        }
    }
}

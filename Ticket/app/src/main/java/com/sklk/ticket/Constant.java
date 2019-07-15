package com.sklk.ticket;

/**
 * @package: com.ssic.yytc
 * @author：JHQ
 * @date： 2018/4/19  9:15
 */
public class Constant {

    //PermissionUtil无对应权限返回此字符串
    public static final String NO_PERMISSION = "MissingPermission";
    //---上拉加载更多和下拉刷新---
    //自动刷新延迟加载时间（毫秒）
    public static final int AUTOREFRESH_DELAYED = 0;
    //拖拽动画持续时间（毫秒）
    public static final int AUTOREFRESH_DURATION = 200;
    //拉拽的高度比率（要求 ≥ 1 ）
    public static final int AUTOREFRESH_DRAGRATE = 1;
    //---MenuActivity---
    //双击时间段可以退出应用（毫秒）
    public static final int EXIT_APP = 2000;
    /**
     * 网络请求超时时长
     */
    public static final int CONNECT_TIMEOUT = 20;

}

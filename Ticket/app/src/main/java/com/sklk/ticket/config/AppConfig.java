package com.sklk.ticket.config;


/**
 * author：${JHuiQ} on 2018/4/11 09:51
 */
public class AppConfig {

    /**
     * 线上
     */
    public static String BASE_IP = "http://coolcuii.eicp.net";
    /**
     * uat
     */
//    public static String BASE_IP = "http://uat.parent.sunshinelunch.com";
    /**
     * 测试
     */
//    public static String BASE_IP = "http://h5.tfsitest.com";
    public static String UPDATE_IP = "/public_resource/parent_app_client_metadata_v1.0.json";

    /**
     * 环境切换(true 打开，false 关闭) ps:上线需关闭
     */
//    public static boolean ONLINE = true;
    public static boolean ONLINE = false;

    //版本更新点击取消，第二次提示时间
    public static final int SECOND_UPDATE = 8;

}

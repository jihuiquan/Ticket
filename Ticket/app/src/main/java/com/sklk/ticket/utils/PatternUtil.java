package com.sklk.ticket.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 常量类
 * 作者：he.zhao on 2016/7/20 10:21
 * 邮箱：15010325821@163.com
 */
public class PatternUtil {
    /**
     * 手机号的正则表达式
     */
    public static final String PHONE_PATTERN = "^1(3|4|5|7|8)\\d{9}$";
    /**
     * 6到12位字母加数字组合的密码
     */
    public static final String REGISTER_PWD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";


    /**
     * 6-20位字符；数字、字母两种组合 这个密码验证的正则表达式，区分大小写
     *
     */
//    public static final String REST_PWD= "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";

    /**
     * 6-20位字符；数字、字母、特殊字符（除空格），起码其中两种组合 这个密码验证的正则表达式，区分大小写
     * 特殊字符为 ，~ ` ! @ # $ % ^ & * ( ) _ - + = | \ } ] { [ : ; " ' ? / > . <
     */
    public static final String REST_PWD = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![~`,!@#\\$%\\^&\\*\\(\\)_\\-\\+=\\|\\\\\\\\\\}\\]\\{\\[:;\"'\\?\\/>\\.\\<]+$)[0-9A-Za-z~`,!@#\\$%\\^&\\*\\(\\)_\\-\\+=\\|\\\\\\\\\\}\\]\\{\\[:;\"'\\?\\/>\\.\\<]{6,20}$";
    /**
     * 身份证的正则表达式
     */
    public static final String CARD_PATTERN = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 6位数字的正则表达式
     */
    public static final String DEAL_PWD_PATTERN = "^\\d{6}$";


    /*验证密码**/
    public static int regex_pwd(String pwd) {
        int errorcode = 0;
        Pattern p = Pattern.compile(REGISTER_PWD);
        Matcher m = p.matcher(pwd);
        if (!m.find()) {
            errorcode = -1;
        }
        return errorcode;
    }

    /*验证手机号**/
    public static int regex_phone(String phone) {
        int errorcode = 0;
        Pattern p = Pattern.compile(PHONE_PATTERN);
        Matcher m = p.matcher(phone);
        if (!m.find()) {
            errorcode = -1;
        }
        return errorcode;
    }

    /**
     * 一、15位身份证号
     * <p>
     * 二、18位身份证号（前17位位数字，最后一位为字母x）
     * <p>
     * 三、18为身份证号（18位都是数字）
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    public static boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    /*验证交易密码**/
    public static int regex_dealPwd(String card) {
        int errorcode = 0;
        Pattern p = Pattern.compile(DEAL_PWD_PATTERN);
        Matcher m = p.matcher(card);
        if (!m.find()) {
            errorcode = -1;
        }
        return errorcode;
    }


    /*验证交易密码**/
    public static int reset_pwd(String card) {
        int errorcode = 0;
        Pattern p = Pattern.compile(REST_PWD);
        Matcher m = p.matcher(card);
        if (!m.find()) {
            errorcode = -1;
        }
        return errorcode;
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    //通过正則表達式来推断。以下的样例仅仅同意显示字母、数字和汉字。

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 仅仅同意字母、数字和汉字
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


}

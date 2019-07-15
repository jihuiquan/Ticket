package com.sklk.ticket.base;

/**
 * @package: com.ssic.yytc.base
 * @author：JHQ
 * @date： 2018/4/25  10:57
 */
public class BaseBean<T> {

    /**
     * retCode : 100000
     * version : 1.0
     * timestamp : 1524624810770
     * data : {"loginURL":"http://192.168.1.243:8010/oauth/authorize?response_type=code&installation_id=eyJkZXZpY2VfdHlwZSI6Ik1vYmlsZSIsIm9zX3R5cGUiOiJhbmRyb2lkIiwiYWdlbnRfdHlwZSI6Ik5hdGl2ZSIsImhhcmR3YXJlX3VpZCI6eyJtb2JpbGUiOnsiSU1TSSI6Im51bGwifX19&device_info=eyJkZXZpY2VUeXBlIjoiTW9iaWxlIiwib3NUeXBlIjoid2luZG93cyIsImFnZW50VHlwZSI6IkJyb3dzZXIifQ&client_id=1&state=1&redirect_url=http://192.168.1.243:8008/schooldinner/application/login","graphicCodeURL":"http://192.168.1.243:8010/oauth/vcode/gen_graphic_vcode_image","shortMessageCodeURL":"http://192.168.1.243:8010/oauth/vcode/gen_short_message_vcode","state":"2"}
     */

    private int code;
    private String alert;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

package com.sklk.ticket.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @package: com.ssic.yytc.utils
 * @author：JHQ
 * @date： 2018/4/28  13:47
 */
public class KeyBoardUtil {

    //点击其它区域隐藏软键盘
    public static <E extends EditText> void hideKey(E e, Context context) {
        if (null != e) {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
        }
    }
}

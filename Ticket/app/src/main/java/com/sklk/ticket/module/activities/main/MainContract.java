package com.sklk.ticket.module.activities.main;

import android.content.Context;

import com.sklk.ticket.base.mvp.BasePresenter;
import com.sklk.ticket.base.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class MainContract {
    interface View extends BaseView {
    }

    interface  Presenter extends BasePresenter<View> {
        
    }
}

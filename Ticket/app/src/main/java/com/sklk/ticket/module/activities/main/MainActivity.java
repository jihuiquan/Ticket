package com.sklk.ticket.module.activities.main;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sklk.ticket.R;
import com.sklk.ticket.base.mvp.MVPBaseActivity;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class MainActivity extends MVPBaseActivity<MainContract.View, MainPresenter> implements MainContract.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

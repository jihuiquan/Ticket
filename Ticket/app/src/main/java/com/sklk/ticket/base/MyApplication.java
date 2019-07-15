package com.sklk.ticket.base;

import android.app.Application;

import com.mob.MobSDK;
import com.umeng.commonsdk.UMConfigure;


public class MyApplication extends Application {

    private static MyApplication sBaseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sBaseApplication = this;
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobSDK.init(this);
    }

    public static MyApplication getApplication() {
        return sBaseApplication;
    }
}

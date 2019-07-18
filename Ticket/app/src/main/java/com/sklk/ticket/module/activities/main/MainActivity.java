package com.sklk.ticket.module.activities.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.MobSDK;
import com.sklk.ticket.R;
import com.sklk.ticket.base.mvp.MVPBaseActivity;
import com.sklk.ticket.config.AppConfig;
import com.sklk.ticket.listener.OnClickShareDialogListener;
import com.sklk.ticket.mui.ProgressBarWebView;
import com.sklk.ticket.mui.SelectShareDialog;
import com.sklk.ticket.network.ExecuteHttpManger;
import com.sklk.ticket.network.HttpResponseCode;
import com.sklk.ticket.network.RequestInterface;
import com.sklk.ticket.utils.LanguageUtil;
import com.sklk.ticket.utils.NetWorkUtil;
import com.sklk.ticket.utils.StatusBUtil;
import com.sklk.ticket.utils.StatusCodeUtil;
import com.sklk.ticket.utils.ToastCommon;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class MainActivity extends MVPBaseActivity<MainContract.View, MainPresenter> implements MainContract.View {

    //    @InjectView(R.id.title_tv)
//    TextView mTitleTv;
//    @InjectView(R.id.share_iv)
//    ImageView mShareIv;
    @InjectView(R.id.pbwv)
    ProgressBarWebView mPbwv;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mUrl = AppConfig.BASE_IP /*+ "?articleId=" + mArticleID*/;
        mPbwv.loadUrl(this, mUrl);
    }

    @Override
    public void finish() {
        if (mPbwv.canGoBack()) {
            mPbwv.goBack();
        } else {
            super.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPbwv != null) {
            mPbwv.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPbwv != null) {
            mPbwv.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPbwv != null) {
            mPbwv.onDestroy();
        }
    }
}

package com.sklk.ticket.base.mvp;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.igexin.sdk.PushManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.sklk.ticket.mui.LoadDialog;
import com.sklk.ticket.network.RequestInterface;
import com.sklk.ticket.network.RetroftServiceManager;
import com.sklk.ticket.service.ReceivePushService;
import com.sklk.ticket.service.SinglePushService;
import com.sklk.ticket.utils.LanguageUtil;
import com.sklk.ticket.utils.SPUtil;
import com.sklk.ticket.utils.StatusBUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.ParameterizedType;
import java.util.Locale;

import rx.Observable;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public abstract class MVPBaseActivity<V extends BaseView, T extends BasePresenterImpl<V>> extends RxAppCompatActivity implements BaseView {
    public T mPresenter;
    private RequestInterface mService;
    //此Activity需不需要右滑出动画
    private boolean mEndActivityAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getInstance(this, 1);
        mPresenter.attachView((V) this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //修改状态字体颜色
        StatusBUtil.StatusBarLightMode(this);
    }

    public RequestInterface getRequestService() {
        if (null == mService) {
            mService = RetroftServiceManager.getService(RequestInterface.class);
        }
        return mService;
    }

    public void setRequestNull() {
        mService = null;
    }

    @Override
    public Context getContext() {
        return this;
    }

    public <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    //防止Rxjava由于订阅者内存泄漏，每个网络请求必须调用
    public <B> Observable<B> bindObs(Observable<B> observable) {
        return observable.compose(this.<B>bindToLifecycle());
    }


    public void onRefreshAndLoadMoreSuccess(RefreshLayout refreshLayout) {
        if (null != refreshLayout) {
            refreshLayout.finishRefresh(true);
            refreshLayout.finishLoadMore(true);
        }
    }

    //关闭上拉加载更多和下拉刷新，true为成功，false为失败
    public void onRefreshAndLoadMoreFailure(RefreshLayout refreshLayout) {
        if (null != refreshLayout) {
            refreshLayout.finishRefresh(false);
            refreshLayout.finishLoadMore(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(new Bundle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        PushManager.getInstance().initialize(getApplicationContext(), SinglePushService.class);
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), ReceivePushService.class);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消掉loading
        LoadDialog.destoryLoading();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}

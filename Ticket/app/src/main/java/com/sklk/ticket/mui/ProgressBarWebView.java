package com.sklk.ticket.mui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mob.MobSDK;
import com.sklk.ticket.R;
import com.sklk.ticket.base.MyApplication;
import com.sklk.ticket.config.AppConfig;
import com.sklk.ticket.listener.OnClickShareDialogListener;
import com.sklk.ticket.module.activities.main.MainActivity;
import com.sklk.ticket.module.activities.main.ShareBean;
import com.sklk.ticket.utils.DeviceUtil;
import com.sklk.ticket.utils.GsonUtil;
import com.sklk.ticket.utils.NetWorkUtil;
import com.sklk.ticket.utils.SPUtil;
import com.sklk.ticket.utils.ToastCommon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

/**
 * @package: cn.ssic.tianfangcatering.view
 * @author：JHQ
 * @date： 2018/10/9  16:46
 */
public class ProgressBarWebView extends LinearLayout {

    private WebView mWebView;
    private LinearLayout mLl;

    public ProgressBarWebView(Context context) {
        super(context);
        initView(context);
    }

    public ProgressBarWebView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ProgressBarWebView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView(final Context context) {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        View view = inflate(context, R.layout.progressbar_webview, null);
        mWebView = (WebView) view.findViewById(R.id.wv);
        mLl = (LinearLayout) view.findViewById(R.id.ll);
        final TextView errorTv = (TextView) view.findViewById(R.id.error_tv);
        mLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.reload();
            }
        });
        //获取webSettings
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);//关键点
        //让webView支持JS
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //设置支持缩放
        settings.setBuiltInZoomControls(false);
        settings.setCacheMode(LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.addJavascriptInterface(new JavaScriptinterface(context),
                "android");
        String ua = mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setUserAgentString(ua + "Android/luhe/60/0");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            //当页面开始加载的时候调用此方法
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            /**
             * 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
             *
             * @param view
             */
            // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (NetWorkUtil.hasNetWork()) {
                    errorTv.setText(getResources().getString(R.string.error));
                } else {
                    errorTv.setText(getResources().getString(R.string.error_network));
                }
                // 在这里显示自定义错误页
                mLl.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
            }
        });
        addView(view, lp);
    }

    private boolean syncCookie(Context context, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        String string = SPUtil.getString(context, SPUtil.STRING_COOKIES);
        if (!TextUtils.isEmpty(string)) {
            String substring = string.substring(1, string.length() - 1);
            String[] split = substring.split(",");
            for (int i = 0; i < split.length; i++) {
                cookieManager.setCookie(url, split[i]);
            }
            String newCookie = cookieManager.getCookie(url);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
                cookieSyncManager.sync();
            }
            return TextUtils.isEmpty(newCookie) ? false : true;
        }
        return false;

    }

    public void loadUrl(MainActivity mainActivity, String url) {
        syncCookie(mainActivity, url);
        Map<String, String> map = new HashMap<>();
        map.put("jsessionid", SPUtil.getString(MyApplication.getApplication(), "sessionId"));
        map.put("appChannel", "luhe");
        map.put("OSType", "Android");
        map.put("deviceModel", Build.MODEL);
        map.put("OSVersion", android.os.Build.VERSION.RELEASE);
        map.put("appVersion", DeviceUtil.getVersionStr(MyApplication.getApplication()));
        mWebView.loadUrl(url, map);
//        mWebView.loadUrl(url);
    }

    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    public void goBack() {
        mWebView.goBack();
    }

    public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context c) {
            context = c;
        }

        @JavascriptInterface
        public void toShare(String list) {
            ShareBean shareBean = new Gson().fromJson(list, ShareBean.class);
            shareWebPage(shareBean.getUrl(), shareBean.getTitle(), shareBean.getDescription(), shareBean.getIcon());

        }

        @JavascriptInterface
        public void toLogin(String sessionId) {
            SPUtil.putString(MyApplication.getApplication(), "sessionId", sessionId);
        }

        public void shareWebPage(final String url, final String title, final String content, final String imageUrl) {
            SelectShareDialog shareDialog = new SelectShareDialog(context);
            shareDialog.setOnClickListenerWithCancelAndConfirm(new OnClickShareDialogListener() {
                @Override
                public void onConfirmListener(int type) {
                    switch (type) {
                        case 0:
                            //微信好友  text
                            if (isValidClient("com.tencent.mm")) {
                                Platform platform = ShareSDK.getPlatform(Wechat.NAME);
                                Platform.ShareParams shareParams = new Platform.ShareParams();
                                shareParams.setText(content);
                                shareParams.setTitle(title);
//                                if (NetWorkUtil.alertImageUri(imageUrl)) {
                                shareParams.setImageUrl(imageUrl);
//                                }
                                shareParams.setUrl(url);
                                shareParams.setShareType(Platform.SHARE_WEBPAGE);
                                platform.setPlatformActionListener(new PlatformActionListener() {
                                    @Override
                                    public void onComplete(Platform platform, int i, HashMap<String, Object> map) {
                                        if (context instanceof MainActivity) {
                                            MainActivity articleDetailActivity = (MainActivity) context;
//                                            ExecuteHttpManger.executeHttp(articleDetailActivity, articleDetailActivity.bindObs(articleDetailActivity.getRequestService().gShareLog(mArticleID, 1)), null);
                                        }
                                    }

                                    @Override
                                    public void onError(Platform platform, int i, Throwable throwable) {
                                    }

                                    @Override
                                    public void onCancel(Platform platform, int i) {
                                    }
                                });
                                platform.share(shareParams);
                            }
                            break;
                        case 1:
                            if (isValidClient("com.tencent.mm")) {
                                Platform wechatMomentsPlatform = ShareSDK.getPlatform(WechatMoments.NAME);
                                Platform.ShareParams wechatMomentsShareParams = new Platform.ShareParams();
                                wechatMomentsShareParams.setText(content);
                                wechatMomentsShareParams.setTitle(title);
                                wechatMomentsShareParams.setUrl(url);//ResourcesManager.getInstace(MobSDK.getContext()).getUrl()
//                                if (NetWorkUtil.alertImageUri(imageUrl)) {
                                wechatMomentsShareParams.setImageUrl(imageUrl);
//                                }
                                wechatMomentsShareParams.setShareType(Platform.SHARE_WEBPAGE);
                                wechatMomentsPlatform.setPlatformActionListener(new PlatformActionListener() {
                                    @Override
                                    public void onComplete(Platform platform, int i, HashMap<String, Object> map) {
                                        if (context instanceof MainActivity) {
                                            MainActivity articleDetailActivity = (MainActivity) context;
//                                            ExecuteHttpManger.executeHttp(articleDetailActivity, articleDetailActivity.bindObs(articleDetailActivity.getRequestService().gShareLog(mArticleID, 2)), null);
                                        }
                                    }

                                    @Override
                                    public void onError(Platform platform, int i, Throwable throwable) {

                                    }

                                    @Override
                                    public void onCancel(Platform platform, int i) {

                                    }
                                });
                                wechatMomentsPlatform.share(wechatMomentsShareParams);
                            }
                            break;
                        case 2:
                            if (isQQClientAvailable(context)) {
                                Platform platform3 = ShareSDK.getPlatform(QQ.NAME);
                                Platform.ShareParams shareParams3 = new Platform.ShareParams();
                                shareParams3.setText(content);
                                shareParams3.setTitle(title);
                                shareParams3.setTitleUrl(url);
//                                if (NetWorkUtil.alertImageUri(imageUrl)) {
                                shareParams3.setImageUrl(imageUrl);
//                                }
                                shareParams3.setShareType(Platform.SHARE_WEBPAGE);
                                platform3.setPlatformActionListener(new PlatformActionListener() {
                                    @Override
                                    public void onComplete(Platform platform, int i, HashMap<String, Object> map) {
                                        if (context instanceof MainActivity) {
                                            MainActivity articleDetailActivity = (MainActivity) context;
//                                            ExecuteHttpManger.executeHttp(articleDetailActivity, articleDetailActivity.bindObs(articleDetailActivity.getRequestService().gShareLog(mArticleID, 3)), null);
                                        }
                                    }

                                    @Override
                                    public void onError(Platform platform, int i, Throwable throwable) {

                                    }

                                    @Override
                                    public void onCancel(Platform platform, int i) {

                                    }
                                });
                                platform3.share(shareParams3);
                            }
                            break;
                        case 3:
                            Platform sinaPlatform = ShareSDK.getPlatform(SinaWeibo.NAME);
                            Platform.ShareParams sinaShareParams = new Platform.ShareParams();
                            sinaShareParams.setText(content + url);
                            sinaShareParams.setTitle(title);
                            sinaShareParams.setTitleUrl(url);
//                            if (NetWorkUtil.alertImageUri(imageUrl)) {
                            sinaShareParams.setImageUrl(imageUrl);
//                            }
                            sinaShareParams.setShareType(Platform.SHARE_WEBPAGE);
                            sinaPlatform.setPlatformActionListener(new PlatformActionListener() {
                                @Override
                                public void onComplete(Platform platform, int i, HashMap<String, Object> map) {
                                    if (context instanceof MainActivity) {
                                        MainActivity articleDetailActivity = (MainActivity) context;
//                                        ExecuteHttpManger.executeHttp(articleDetailActivity, articleDetailActivity.bindObs(articleDetailActivity.getRequestService().gShareLog(mArticleID, 4)), null);
                                    }
                                }

                                @Override
                                public void onError(Platform platform, int i, Throwable throwable) {

                                }

                                @Override
                                public void onCancel(Platform platform, int i) {

                                }
                            });
                            sinaPlatform.share(sinaShareParams);
                            break;
                        default:
                            break;
                    }
                }
            });
            shareDialog.show();
        }

        @SuppressLint("WrongConstant")
        private boolean isValidClient(String akp) {
            PackageInfo pi;
            try {
                pi = MobSDK.getContext().getPackageManager().getPackageInfo(
                        akp, PackageManager.GET_RESOLVED_FILTER);
                return true;
            } catch (Throwable t) {
                ToastCommon.toast(context, R.string.wechat_uninstalled);
            }
            return false;
        }

        /**
         * 判断qq是否可用
         *
         * @param context
         * @return
         */
        private boolean isQQClientAvailable(Context context) {
            final PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.tencent.mobileqq")) {
                        return true;
                    }
                }
            }
            ToastCommon.toast(context, R.string.qq_uninstalled);
            return false;
        }
    }

    public void onResume() {
        if (null != mWebView) {
            mWebView.resumeTimers();
            mWebView.onResume();
        }
    }

    public void onPause() {
        if (null != mWebView) {
            mWebView.onPause();
            mWebView.pauseTimers();
        }
    }

    public void onDestroy() {
        if (null != mWebView) {
            mWebView.destroy();
            mWebView = null;
        }
    }
}

package com.sklk.ticket.mui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mob.MobSDK;
import com.sklk.ticket.R;
import com.sklk.ticket.base.MyApplication;
import com.sklk.ticket.listener.OnClickCommonDialogListener;
import com.sklk.ticket.listener.OnClickShareDialogListener;
import com.sklk.ticket.module.activities.main.MainActivity;
import com.sklk.ticket.network.ExecuteHttpManger;
import com.sklk.ticket.utils.NetWorkUtil;
import com.sklk.ticket.utils.PermissionUtil;
import com.sklk.ticket.utils.SPUtil;
import com.sklk.ticket.utils.ToastCommon;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @package: cn.ssic.tianfangcatering.view
 * @author：JHQ
 * @date： 2018/10/9  16:46
 */
public class ProgressBarWebView extends LinearLayout {

    int mCurrentProgress;
    int mProgress;
    private ThisHandler mHandler = new ThisHandler(this);
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private LinearLayout mLl;
    private String mImageUrl;
    private String mTitle;
    private String mContent;
    private String mUrl;
    private String mArticleID;
    private String mImgName;

    private static class ThisHandler extends Handler {
        private WeakReference<ProgressBarWebView> mainViewWeakReference;

        public ThisHandler(ProgressBarWebView progressBarWebView) {
            mainViewWeakReference = new WeakReference<>(progressBarWebView);
        }

        @Override
        public void handleMessage(Message msg) {
            ProgressBarWebView menuBannerView = mainViewWeakReference.get();
            if (null != menuBannerView) {
                int progress = menuBannerView.mProgress;
                if (progress < 90) {
                    menuBannerView.mProgressBar.setProgress(progress);
                    menuBannerView.openProgressBar(3, 1);
                } else if (menuBannerView.mCurrentProgress < progress) {
                    menuBannerView.mProgressBar.setProgress(progress);
                    menuBannerView.openProgressBar(1, 500);
                } else {
                    menuBannerView.mProgressBar.setProgress(progress);
                    menuBannerView.openProgressBar(3, 1);
                }
            }
        }
    }

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
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb);
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
        //让webView支持JS
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //设置支持缩放
        settings.setBuiltInZoomControls(false);
        mWebView.addJavascriptInterface(new JavaScriptinterface(context),
                "android");

        //这个方法用于让H5调用android方法
//        mWv.addJavascriptInterface(this, "android");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mCurrentProgress = newProgress;
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            //当页面开始加载的时候调用此方法
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgress = 0;
                openProgressBar(3, 1);
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
                mHandler.removeCallbacksAndMessages(null);
                if (NetWorkUtil.hasNetWork()) {
                    errorTv.setText(getResources().getString(R.string.error));
                } else {
                    errorTv.setText(getResources().getString(R.string.error_network));
                }
                // 在这里显示自定义错误页
                mLl.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
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


    private void openProgressBar(int speed, int delayMillis) {
        mWebView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        if (mProgress >= 100) {
            mHandler.removeCallbacksAndMessages(null);
            mProgressBar.setVisibility(View.GONE);
            return;
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mProgress = mProgress + speed;
            mHandler.sendEmptyMessageDelayed(0, delayMillis);
        }
    }

    public void setDownloadH5Image(boolean b) {
        if (b) {
            if (null != mWebView) {
                mWebView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View view) {
                        final WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
                        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                                hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                            CommonDialog commonDialog = new CommonDialog(view.getContext(), view.getContext().getString(R.string.prompt), view.getContext().getString(R.string.save_image_local), view.getContext().getString(R.string.save_to_folder));
                            commonDialog.setOnClickListenerWithCancelAndConfirm(new OnClickCommonDialogListener() {
                                @Override
                                public void onConfirmListener() {
                                    if (!PermissionUtil.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, view.getContext())) {
                                        SingleDialog singleDialog = new SingleDialog(view.getContext(), view.getContext().getString(R.string.permisstion), view.getContext().getString(R.string.permisstion_page));
                                        singleDialog.setOnClickListenerWithCancelAndConfirm(new OnClickCommonDialogListener() {
                                            @Override
                                            public void onConfirmListener() {
                                                Uri packageURI = Uri.parse("package:" + view.getContext().getPackageName());
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                                view.getContext().startActivity(intent);
                                            }
                                        });
                                        singleDialog.show();
                                        return;
                                    }
                                    if (URLUtil.isValidUrl(hitTestResult.getExtra())) {
                                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(hitTestResult.getExtra()));
                                        request.allowScanningByMediaScanner();
                                        //设置图片的保存路径
                                        request.setDestinationInExternalPublicDir(view.getContext().getString(R.string.dirtype), "/" + mImgName + System.currentTimeMillis() + ".png");
                                        DownloadManager downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                        downloadManager.enqueue(request);
                                        ToastCommon.toastSuccessLong(view.getContext(), view.getContext().getString(R.string.file_saved_successfully));
                                    } else {
                                        ToastCommon.toast(view.getContext(), R.string.file_saved_faiure);
                                    }
                                }
                            });
                            commonDialog.show();
                        }
                        return false;
                    }
                });
            }
        }
    }

    public void loadUrl(Context context, String url) {
//        syncCookie(context, url);
        mWebView.loadUrl(url);
    }

    public void loadUrl(MainActivity mainActivity, String url, String imageUrl, String title, String content, String articleID, String imgName) {
        this.mUrl = url;
        this.mImageUrl = imageUrl;
        this.mTitle = title;
        this.mContent = content;
        this.mArticleID = articleID;
        this.mImgName = imgName;
//        syncCookie(mainActivity, url);
        mWebView.loadUrl(url);
        Log.d(TAG, "loadUrl: " + mWebView.getSettings().getUserAgentString());
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

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void toChildInfo() {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }

        @JavascriptInterface
        public void shareWebPage() {
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
                                shareParams.setText(mContent);
                                shareParams.setTitle(mTitle);
                                if (NetWorkUtil.alertImageUri(mImageUrl)) {
                                    shareParams.setImageUrl(mImageUrl);
                                }
                                shareParams.setUrl(mUrl);
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
                                wechatMomentsShareParams.setText(mContent);
                                wechatMomentsShareParams.setTitle(mTitle);
                                wechatMomentsShareParams.setUrl(mUrl);//ResourcesManager.getInstace(MobSDK.getContext()).getUrl()
                                if (NetWorkUtil.alertImageUri(mImageUrl)) {
                                    wechatMomentsShareParams.setImageUrl(mImageUrl);
                                }
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
                                shareParams3.setText(mContent);
                                shareParams3.setTitle(mTitle);
                                shareParams3.setTitleUrl(mUrl);
                                if (NetWorkUtil.alertImageUri(mImageUrl)) {
                                    shareParams3.setImageUrl(mImageUrl);
                                }
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
                            sinaShareParams.setText(mContent + mUrl);
                            sinaShareParams.setTitle(mTitle);
                            sinaShareParams.setTitleUrl(mUrl);
                            if (NetWorkUtil.alertImageUri(mImageUrl)) {
                                sinaShareParams.setImageUrl(mImageUrl);
                            }
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

        @JavascriptInterface
        public void callToFoodSafety(final String msg) {
            CommonDialog commonDialog = new CommonDialog(context, context.getString(R.string.prompt), context.getString(R.string.dial) + msg, "");
            commonDialog.setOnClickListenerWithCancelAndConfirm(new OnClickCommonDialogListener() {
                @Override
                public void onConfirmListener() {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + msg);
                    intent.setData(data);
                    context.startActivity(intent);
                }
            });
            commonDialog.show();
        }

        @JavascriptInterface
        public void logout() {
            CookieManager.getInstance().removeAllCookie();
            SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_DOMAIN);
            SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_EXPIRESAT);
            SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_NAME);
            SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_PATH);
            SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_VALUE);
            SPUtil.remove(MyApplication.getApplication(), SPUtil.STRING_COOKIES);
            ToastCommon.toast(context, R.string.login_invalid);
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.finish();
            }
            context = null;
        }

        @JavascriptInterface
        public void toArticleList() {
//            MyApplication.getApplication().setMainPage(1);
            if (context instanceof Activity) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Activity activity = (Activity) context;
                activity.finish();
            }
            context = null;
        }

        @JavascriptInterface
        public void toArticleDetail(String articleId, String articleImageTitleURL, String articleTitle, String articleContentKeyWords, String ArticleChannelName) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("intent_type", 1);
            intent.putExtra("articleID", articleId);
            intent.putExtra("articleImageTitleURL", articleImageTitleURL);
            intent.putExtra("articleTitle", articleTitle);
            intent.putExtra("articleContentKeyWords", articleContentKeyWords);
            intent.putExtra("articleChannelName", ArticleChannelName);
            context.startActivity(intent);
        }

        @JavascriptInterface
        public void onFinish() {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.finish();
            }
        }
    }

    /**
     * 分享
     */
    @JavascriptInterface
    public void toShare() {
//        Logger.i("hybrid" + "appshare");
//        MyUserInfo cacheData = DataCache.instance.getCacheData("heng", "MyUserInfo");
//        if (cacheData == null) {
//            startActivityForResult(new Intent(WebActivity.this, CheckPhoneActivity.class), LOGIN_INVITE);
//        } else {
//            startActivity(new Intent(WebActivity.this, InviteFriendActivity.class));
//        }
        Log.d(TAG, "appShare() called");
    }

    @JavascriptInterface
    public void toShare(String list) {
//        Gson gs = new Gson();
//        ShareModel shareModel = gs.fromJson(list, ShareModel.class);
//        BottomDialogFragment dialogFragment = new BottomDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("title", shareModel.getTitle());
//        bundle.putString("content", shareModel.getDescription());
//        bundle.putString("url", shareModel.getUrl());
//        bundle.putString("shareImageUrl", shareModel.getUrl());
//        dialogFragment.setArguments(bundle);
//        dialogFragment.show(getSupportFragmentManager(), "dialog");
        Log.d(TAG, "appShare() called with: list = [" + list + "]");
    }

    private static final String TAG = "ProgressBarWebView";

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
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (null != mWebView) {
            mWebView.destroy();
            mWebView = null;
        }
    }
}

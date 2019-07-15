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

    @InjectView(R.id.title_tv)
    TextView mTitleTv;
    @InjectView(R.id.share_iv)
    ImageView mShareIv;
    @InjectView(R.id.pbwv)
    ProgressBarWebView mPbwv;
    private String mArticleID;
    private String mImgurl;
    private String mTitle;
    private String mWords;
    private String mUrl;
    private int mType = 1;
//    https://api.51yiyuangouwu.com/activity/share

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        switch (mType) {
            case 1:
                mShareIv.setVisibility(View.VISIBLE);
//                mArticleID = intent.getStringExtra("articleID");
                mImgurl = intent.getStringExtra("articleImageTitleURL");
                mTitle = intent.getStringExtra("articleTitle");
                mWords = intent.getStringExtra("articleContentKeyWords");
                String articleChannelName = intent.getStringExtra("articleChannelName");
                mTitleTv.setText(articleChannelName);
                mUrl = AppConfig.BASE_IP + "/activity/share" /*+ "?articleId=" + mArticleID*/;
                mPbwv.loadUrl(this, mUrl, mImgurl, mTitle, mWords, mArticleID, articleChannelName);
                mPbwv.setDownloadH5Image(true);
                break;
            case 2:
                mShareIv.setVisibility(View.GONE);
                mTitleTv.setText(getString(R.string.complaints));
                mPbwv.loadUrl(this, AppConfig.BASE_IP + "/pr/complaints.html");
                break;
            case 3:
                mShareIv.setVisibility(View.GONE);
                mTitleTv.setText(getString(R.string.protocol));
                mPbwv.loadUrl(this, AppConfig.BASE_IP + "/pr/agreement.html");
                break;
            case 4:
                int commentType = intent.getIntExtra("commentType", 1);
                mShareIv.setVisibility(View.GONE);
                mTitleTv.setText(getString(R.string.my_comment));
                mPbwv.loadUrl(this, AppConfig.BASE_IP + "/pr/myCommentPreload.html" + "?commentType=" + commentType + "&lang=" + LanguageUtil.getCurrentLanguage(this));
                break;
            case 5:
                mShareIv.setVisibility(View.VISIBLE);
                mArticleID = intent.getStringExtra("articleID");
//                mPresenter.gGetById(bindObs(getRequestService().gGetById(mArticleID)));
                break;
            case 6:
                mShareIv.setVisibility(View.GONE);
                String refUrl = intent.getStringExtra("refUrl");
                String title = intent.getStringExtra("title");
                mTitleTv.setText(title);
                mPbwv.loadUrl(this, refUrl);
                mPbwv.setDownloadH5Image(true);
                break;
            default:
                break;
        }

    }

    @OnClick({R.id.toolbar_left_iv, R.id.share_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_iv:
                finish();
                break;
            case R.id.share_iv:
                SelectShareDialog shareDialog = new SelectShareDialog(MainActivity.this);
                shareDialog.setOnClickListenerWithCancelAndConfirm(new OnClickShareDialogListener() {
                    @Override
                    public void onConfirmListener(int type) {
                        switch (type) {
                            case 0:
                                //微信好友  text
                                if (isValidClient("com.tencent.mm")) {
                                    Platform platform = ShareSDK.getPlatform(Wechat.NAME);
                                    Platform.ShareParams shareParams = new Platform.ShareParams();
                                    shareParams.setText(mWords);
                                    shareParams.setTitle(mTitle);
                                    if (NetWorkUtil.alertImageUri(mImgurl)) {
                                        shareParams.setImageUrl(mImgurl);
                                    }
                                    shareParams.setUrl(mUrl);
                                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                                    platform.setPlatformActionListener(new PlatformActionListener() {
                                        @Override
                                        public void onComplete(Platform platform, int i, HashMap<String, Object> map) {
//                                            ExecuteHttpManger.executeHttp(ArticleDetailActivity.this, bindObs(getRequestService().gShareLog(mArticleID, 1)), null);
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
                                    wechatMomentsShareParams.setText(mWords);
                                    wechatMomentsShareParams.setTitle(mTitle);
                                    wechatMomentsShareParams.setUrl(mUrl);//ResourcesManager.getInstace(MobSDK.getContext()).getUrl()
                                    if (NetWorkUtil.alertImageUri(mImgurl)) {
                                        wechatMomentsShareParams.setImageUrl(mImgurl);
                                    }
                                    wechatMomentsShareParams.setShareType(Platform.SHARE_WEBPAGE);
                                    wechatMomentsPlatform.setPlatformActionListener(new PlatformActionListener() {
                                        @Override
                                        public void onComplete(Platform platform, int i, HashMap<String, Object> map) {
//                                            ExecuteHttpManger.executeHttp(ArticleDetailActivity.this, bindObs(getRequestService().gShareLog(mArticleID, 2)), null);
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
                                if (isQQClientAvailable(MainActivity.this)) {
                                    Platform platform3 = ShareSDK.getPlatform(QQ.NAME);
                                    Platform.ShareParams shareParams3 = new Platform.ShareParams();
                                    shareParams3.setText(mWords);
                                    shareParams3.setTitle(mTitle);
                                    shareParams3.setTitleUrl(mUrl);
                                    if (NetWorkUtil.alertImageUri(mImgurl)) {
                                        shareParams3.setImageUrl(mImgurl);
                                    }
                                    shareParams3.setShareType(Platform.SHARE_WEBPAGE);
                                    platform3.setPlatformActionListener(new PlatformActionListener() {
                                        @Override
                                        public void onComplete(Platform platform, int i, HashMap<String, Object> map) {
//                                            ExecuteHttpManger.executeHttp(ArticleDetailActivity.this, bindObs(getRequestService().gShareLog(mArticleID, 3)), null);
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
                                sinaShareParams.setText(mWords + mUrl);
                                sinaShareParams.setTitle(mTitle);
                                sinaShareParams.setTitleUrl(mUrl);
                                if (NetWorkUtil.alertImageUri(mImgurl)) {
                                    sinaShareParams.setImageUrl(mImgurl);
                                }
                                sinaShareParams.setShareType(Platform.SHARE_WEBPAGE);
                                sinaPlatform.setPlatformActionListener(new PlatformActionListener() {
                                    @Override
                                    public void onComplete(Platform platform, int i, HashMap<String, Object> map) {
//                                        ExecuteHttpManger.executeHttp(ArticleDetailActivity.this, bindObs(getRequestService().gShareLog(mArticleID, 4)), null);
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
                break;
            default:
                break;
        }
    }

    @SuppressLint("WrongConstant")
    private boolean isValidClient(String akp) {
        PackageInfo pi;
        try {
            pi = MobSDK.getContext().getPackageManager().getPackageInfo(
                    akp, PackageManager.GET_RESOLVED_FILTER);
            return true;
        } catch (Throwable t) {
            ToastCommon.toast(MainActivity.this, getString(R.string.unfind_wechat));
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
        ToastCommon.toast(MainActivity.this, getString(R.string.unfind_qq));
        return false;
    }

    @Override
    public void finish() {
        if (mType != 4 && mPbwv.canGoBack()) {
            mPbwv.goBack();
        } else {
            super.finish();
        }
    }

//    @Override
//    public void gGetByIdSuccess(ArticleBean articleBean) {
//        if (articleBean.getCode() == HttpResponseCode.STATUS_OK) {
//            if (null != articleBean.getData()) {
//                ArticleBean.DataBean data = articleBean.getData();
//                mImgurl = data.getArticleImageTitleURL();
//                mTitle = data.getArticleTitle();
//                mWords = data.getArticleContentKeyWords();
//                String articleChannelName = data.getArticleChannelName();
//                mTitleTv.setText(articleChannelName);
//                mUrl = AppConfig.BASE_IP + "/pr/healthDetail.html" + "?articleId=" + mArticleID;
//                mPbwv.loadUrl(this, mUrl, mImgurl, mTitle, mWords, mArticleID, articleChannelName);
//                mPbwv.setDownloadH5Image(true);
//            }
//        } else {
//            StatusCodeUtil.alertStatusCode(null, this, RequestInterface.METHODNAME_NONE, articleBean.getCode());
//        }
//    }

//    @Override
//    public void onFailure(int type, String failureMsg) {
//        ToastCommon.toast(MainActivity.this, failureMsg);
//    }

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

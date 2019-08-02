package com.sklk.ticket.module.activities.main;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.sklk.ticket.R;
import com.sklk.ticket.base.mvp.MVPBaseActivity;
import com.sklk.ticket.config.AppConfig;
import com.sklk.ticket.mui.ProgressBarWebView;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;


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
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private int FILE_CHOOSER_RESULT_CODE = 0;
    private int FILE_CAMERA_RESULT_CODE = 1;
    private String cameraFielPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mUrl = AppConfig.BASE_IP /*+ "?articleId=" + mArticleID*/;
        mPbwv.mWebView.setWebChromeClient(new WebChromeClient() {
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }


        });
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

    private void openImageChooserActivity() {
//        takeCamera();
        takePhoto();
    }

    //选择图片
    private void takePhoto() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    //拍照
    private void takeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (CommonUtil.hasSdcard()) {
        //这里可能需要检查文件夹是否存在
        //File file = new File(Environment.getExternalStorageDirectory() + "/APPNAME/");
        //if (!file.exists()) {
        //  file.mkdirs();
        //}
        cameraFielPath = Environment.getExternalStorageDirectory() + "upload.jpg";
        File outputImage = new File(cameraFielPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
        startActivityForResult(intent, FILE_CAMERA_RESULT_CODE);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == uploadMessage && null == uploadMessageAboveL) {
            return;
        }
        if (resultCode != RESULT_OK) {//同上所说需要回调onReceiveValue方法防止下次无法响应js方法
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }
            return;
        }
        Uri result = null;
        if (requestCode == FILE_CAMERA_RESULT_CODE) {
            if (null != data && null != data.getData()) {
                result = data.getData();
            }
            if (result == null && hasFile(cameraFielPath)) {
                result = Uri.fromFile(new File(cameraFielPath));
            }
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(new Uri[]{result});
                uploadMessageAboveL = null;
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (data != null) {
                result = data.getData();
            }
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    /**
     * 判断文件是否存在
     */
    public static boolean hasFile(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(Intent intent) {
        Uri[] results = null;
        if (intent != null) {
            String dataString = intent.getDataString();
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                results = new Uri[clipData.getItemCount()];
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    results[i] = item.getUri();
                }
            }
            if (dataString != null) {
                results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }
}

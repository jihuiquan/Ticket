package com.sklk.ticket.mui;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.sklk.ticket.R;

import java.lang.ref.WeakReference;



public class LoadDialog extends Dialog {
    private static LoadDialog loadDialog;
    private String mMsg;
    private static final String STRING_DEFAULANIM = "slack_app_loader.json";
    private LottieAnimationView mLottieAnimationView;
    private static ThisHandler mHandler;

    private final static int INT_NULL = 99;
    private final static int INT_BEFOR = 100;
    private final static int INT_IN = 101;
    private final static int INT_LAST = 102;
    private final static String STRING_NULL = "   ";
    private final static String STRING_BEFOR = ".  ";
    private final static String STRING_IN = ".. ";
    private final static String STRING_LAST = "...";
    private int mCurrentInt = 100;
    private TextView mTextView;

    private static class ThisHandler extends Handler {
        private WeakReference<LoadDialog> loadDialogWeakReference;

        public ThisHandler(LoadDialog loadDialog) {
            loadDialogWeakReference = new WeakReference<>(loadDialog);
        }

        @Override
        public void handleMessage(Message msg) {
            LoadDialog loadDialog = loadDialogWeakReference.get();
            if (null != loadDialog) {
                switch (loadDialog.mCurrentInt) {
                    case INT_NULL:
                        loadDialog.mTextView.setText((loadDialog.mMsg == null ? loadDialog.getContext().getResources().getString(R.string.dialog_loading) : loadDialog.mMsg) + STRING_NULL);
                        loadDialog.mCurrentInt++;
                        break;
                    case INT_BEFOR:
                        loadDialog.mTextView.setText((loadDialog.mMsg == null ? loadDialog.getContext().getResources().getString(R.string.dialog_loading) : loadDialog.mMsg) + STRING_BEFOR);
                        loadDialog.mCurrentInt++;
                        break;
                    case INT_IN:
                        loadDialog.mTextView.setText((loadDialog.mMsg == null ? loadDialog.getContext().getResources().getString(R.string.dialog_loading) : loadDialog.mMsg) + STRING_IN);
                        loadDialog.mCurrentInt++;
                        break;
                    case INT_LAST:
                        loadDialog.mTextView.setText((loadDialog.mMsg == null ? loadDialog.getContext().getResources().getString(R.string.dialog_loading) : loadDialog.mMsg) + STRING_LAST);
                        loadDialog.mCurrentInt = INT_NULL;
                        break;
                    default:
                        loadDialog.mCurrentInt = INT_NULL;
                        break;
                }
                startLoadingText(loadDialog);
            }
        }
    }

    public LoadDialog(@NonNull Context context) {
        this(context, R.style.Loadingdialog);
    }

    public LoadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_loading);
        mLottieAnimationView = (LottieAnimationView) findViewById(R.id.lav);
        mTextView = (TextView) findViewById(R.id.tv);
    }


    private void initAnimation(String str) {
        mLottieAnimationView.setAnimation(str);
        mLottieAnimationView.loop(true);
        mLottieAnimationView.playAnimation();
    }

    private void clearAnimation() {
        mLottieAnimationView = null;
    }

    public static void showLoading(Context context) {
        destoryLoading();
        loadDialog = new LoadDialog(context);
        loadDialog.setCanceledOnTouchOutside(true);
        loadDialog.setCancelable(true);
        loadDialog.mMsg = null;
        loadDialog.mTextView.setText(loadDialog.getContext().getResources().getString(R.string.dialog_loading) + STRING_NULL);
        if (!loadDialog.isShowing()) {
            loadDialog.initAnimation(STRING_DEFAULANIM);
            loadDialog.show();
            startLoadingText(loadDialog);
        }
    }

    public static void showLoading(Context context, String msg) {
        destoryLoading();
        loadDialog = new LoadDialog(context);
        loadDialog.setCanceledOnTouchOutside(true);
        loadDialog.setCancelable(true);
        loadDialog.mMsg = msg;
        loadDialog.mTextView.setText((loadDialog.mMsg == null ? loadDialog.getContext().getResources().getString(R.string.dialog_loading) : loadDialog.mMsg) + STRING_NULL);
        if (!loadDialog.isShowing()) {
            loadDialog.initAnimation(STRING_DEFAULANIM);
            loadDialog.show();
            startLoadingText(loadDialog);
        }
    }

    public static void showLoading(Context context, String msg, String animStr) {
        destoryLoading();
        loadDialog = new LoadDialog(context);
        loadDialog.setCanceledOnTouchOutside(true);
        loadDialog.setCancelable(true);
        loadDialog.mMsg = msg;
        loadDialog.mTextView.setText((loadDialog.mMsg == null ? loadDialog.getContext().getResources().getString(R.string.dialog_loading) : loadDialog.mMsg) + STRING_NULL);
        if (!loadDialog.isShowing()) {
            loadDialog.initAnimation(animStr == null ? STRING_DEFAULANIM : animStr);
            loadDialog.show();
            startLoadingText(loadDialog);
        }
    }

    public static void upImageLoading(Context context, String msg) {
        destoryLoading();
        loadDialog = new LoadDialog(context);
        loadDialog.setCanceledOnTouchOutside(false);
        loadDialog.setCancelable(false);
        loadDialog.mMsg = msg;
        loadDialog.mTextView.setText((loadDialog.mMsg == null ? loadDialog.getContext().getResources().getString(R.string.dialog_loading) : loadDialog.mMsg) + STRING_NULL);
        if (!loadDialog.isShowing()) {
            loadDialog.initAnimation(STRING_DEFAULANIM);
            loadDialog.show();
            startLoadingText(loadDialog);
        }
    }

    private static void startLoadingText(LoadDialog loadDialog) {
        if (null != loadDialog && null != getHandler(loadDialog)) {
            getHandler(loadDialog).removeCallbacksAndMessages(null);
            getHandler(loadDialog).sendEmptyMessageDelayed(0, 500);
        }
    }

    private static void endLoadingText() {
        if (null != loadDialog && null != getHandler(loadDialog)) {
            getHandler(loadDialog).removeCallbacksAndMessages(null);
        }
    }

    private static ThisHandler getHandler(LoadDialog loadDialog) {
        if (null == mHandler) {
            mHandler = new ThisHandler(loadDialog);
        }
        return mHandler;
    }

    public static void dismissLoading() {
        if (null != loadDialog) {
            loadDialog.dismiss();
        }
    }

    public static void destoryLoading() {
        if (null != loadDialog) {
            loadDialog.clearAnimation();
            loadDialog.dismiss();
        }
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
        loadDialog = null;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        endLoadingText();
    }
}

package com.sklk.ticket.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sklk.ticket.R;


public class MLoading extends Dialog {
    private static MLoading loadDialog;
//    private static final String STRING_DEFAULANIM = "data.json";
//    private LottieAnimationView mLottieAnimationView;

    public static final int SHOW_LOADING_CENTER = 1;
    public static final int SHOW_LOADING_RIGHT = 2;
    private ImageView mAnimationIv;

    public MLoading(@NonNull Context context) {
        this(context, R.style.Loadingdialog);
    }

    public MLoading(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.mlib_loading);
//        mLottieAnimationView = (LottieAnimationView) findViewById(R.id.lav);
        mAnimationIv = (ImageView) findViewById(R.id.animation_iv);
    }


//    private void initAnimation(String str) {
//        mLottieAnimationView.setAnimation(str);
//        mLottieAnimationView.loop(true);
//        mLottieAnimationView.setProgress(0.2f);
//        mLottieAnimationView.playAnimation();
//    }

    private void clearAnimation() {
//        mLottieAnimationView = null;
        if (null != mAnimationIv) {
            AnimationDrawable animationDrawable = (AnimationDrawable) mAnimationIv.getBackground();
            animationDrawable.stop();
        }
    }

    private void startAnimation() {
        if (null != mAnimationIv) {
            AnimationDrawable animationDrawable = (AnimationDrawable) mAnimationIv.getBackground();
            animationDrawable.start();
        }
    }


    public static void showLoading(Context context) {
        destoryLoading();
        loadDialog = new MLoading(context);
        loadDialog.setCanceledOnTouchOutside(true);
        loadDialog.setCancelable(true);
        loadDialog.startAnimation();
        if (!loadDialog.isShowing()) {
            loadDialog.show();
        }
    }

    public static void dismissLoading() {
        if (null != loadDialog) {
            loadDialog.clearAnimation();
            loadDialog.dismiss();
        }
    }

    public static void destoryLoading() {
        if (null != loadDialog) {
            loadDialog.clearAnimation();
            loadDialog.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (null != loadDialog) {
            loadDialog.clearAnimation();
        }
    }
}

package com.sklk.ticket.mui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.sklk.ticket.R;
import com.sklk.ticket.listener.OnClickShareDialogListener;


/**
 * Created by Administrator on 2017/11/14.
 */

public class SelectShareDialog extends Dialog {

    private OnClickShareDialogListener mOnClickShareDialogListener;

    public SelectShareDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        //获取当前Activity所在的窗体
        Window dialogWindow = getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        findViewById(R.id.cancel_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.wechat_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnClickShareDialogListener) {
                    mOnClickShareDialogListener.onConfirmListener(0);
                }
                dismiss();
            }
        });
        findViewById(R.id.wechat_qz_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnClickShareDialogListener) {
                    mOnClickShareDialogListener.onConfirmListener(1);
                }
                dismiss();
            }
        });
        findViewById(R.id.qq_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnClickShareDialogListener) {
                    mOnClickShareDialogListener.onConfirmListener(2);
                }
                dismiss();
            }
        });
        findViewById(R.id.sina_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnClickShareDialogListener) {
                    mOnClickShareDialogListener.onConfirmListener(3);
                }
                dismiss();
            }
        });
    }


    public void setOnClickListenerWithCancelAndConfirm(OnClickShareDialogListener onClickShareDialogListener) {
        this.mOnClickShareDialogListener = onClickShareDialogListener;
    }

}

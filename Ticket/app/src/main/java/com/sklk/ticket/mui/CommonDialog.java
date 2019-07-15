package com.sklk.ticket.mui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.sklk.ticket.R;
import com.sklk.ticket.listener.OnClickCommonDialogListener;


/**
 * Created by Administrator on 2017/11/14.
 */

public class CommonDialog extends Dialog {

    private final String mTitle;
    private final String mContent;
    private final String mContent2;
    private OnClickCommonDialogListener mOnClickCommonDialogListener;
    private TextView mConfirmTv;

    public CommonDialog(@NonNull Context context, String title, String content, String content2) {
        super(context, R.style.AlertDialogStyle);
        this.mTitle = title;
        this.mContent = content;
        this.mContent2 = content2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText(mTitle);
        TextView contentTv = (TextView) findViewById(R.id.content_tv);
        TextView content02Tv = (TextView) findViewById(R.id.content2_tv);
        contentTv.setText(mContent);
        content02Tv.setText(mContent2);
        mConfirmTv = (TextView) findViewById(R.id.confirm_tv);
        mConfirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (null != mOnClickCommonDialogListener) {
                    mOnClickCommonDialogListener.onConfirmListener();
                }
            }
        });
        findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void setOnClickListenerWithCancelAndConfirm(OnClickCommonDialogListener onClickCommonDialogListener) {
        this.mOnClickCommonDialogListener = onClickCommonDialogListener;
    }

    public void setConfirmText(String confirmText) {
        mConfirmTv.setText(confirmText);
    }
}
